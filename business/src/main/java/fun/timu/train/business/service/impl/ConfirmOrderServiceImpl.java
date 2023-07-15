package fun.timu.train.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import fun.timu.train.business.entity.ConfirmOrder;
import fun.timu.train.business.entity.DailyTrainCarriage;
import fun.timu.train.business.entity.DailyTrainSeat;
import fun.timu.train.business.entity.DailyTrainTicket;
import fun.timu.train.business.enums.ConfirmOrderStatusEnum;
import fun.timu.train.business.enums.SeatColEnum;
import fun.timu.train.business.enums.SeatTypeEnum;
import fun.timu.train.business.mapper.ConfirmOrderMapper;
import fun.timu.train.business.request.confirm.ConfirmOrderDoVO;
import fun.timu.train.business.request.confirm.ConfirmOrderQueryVO;
import fun.timu.train.business.request.confirm.ConfirmOrderTicketVO;
import fun.timu.train.business.response.ConfirmOrderQueryResponse;
import fun.timu.train.business.service.ConfirmOrderService;
import fun.timu.train.business.service.DailyTrainCarriageService;
import fun.timu.train.business.service.DailyTrainSeatService;
import fun.timu.train.business.service.DailyTrainTicketService;
import fun.timu.train.commo.context.LoginMemberContext;
import fun.timu.train.commo.exception.BusinessException;
import fun.timu.train.commo.exception.BusinessExceptionCode;
import fun.timu.train.commo.response.PageResponse;
import fun.timu.train.commo.utils.SnowUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author zhengke
 * @description 针对表【confirm_order(确认订单)】的数据库操作Service实现
 * @createDate 2023-07-11 15:21:07
 */
@Service
public class ConfirmOrderServiceImpl extends ServiceImpl<ConfirmOrderMapper, ConfirmOrder>
        implements ConfirmOrderService {
    private static final Logger LOG = LoggerFactory.getLogger(ConfirmOrderService.class);

    private final ConfirmOrderMapper mapper;

    private final DailyTrainTicketService dailyTrainTicketService;
    private final DailyTrainCarriageService dailyTrainCarriageService;
    private final DailyTrainSeatService dailyTrainSeatService;

    private final AfterConfirmOrderServiceImpl afterConfirmOrderService;

    public ConfirmOrderServiceImpl(ConfirmOrderMapper mapper, DailyTrainTicketService dailyTrainTicketService, DailyTrainCarriageService dailyTrainCarriageService, DailyTrainSeatService dailyTrainSeatService, AfterConfirmOrderServiceImpl afterConfirmOrderService) {
        this.mapper = mapper;
        this.dailyTrainTicketService = dailyTrainTicketService;
        this.dailyTrainCarriageService = dailyTrainCarriageService;
        this.dailyTrainSeatService = dailyTrainSeatService;
        this.afterConfirmOrderService = afterConfirmOrderService;
    }

    @Override
    public void save(ConfirmOrderDoVO doVO) {
        DateTime now = DateTime.now();
        ConfirmOrder confirmOrder = BeanUtil.copyProperties(doVO, ConfirmOrder.class);
        if (ObjectUtil.isNull(confirmOrder.getId())) {
            confirmOrder.setId(SnowUtil.getSnowflakeNextId());
            confirmOrder.setCreateTime(now);
            confirmOrder.setUpdateTime(now);
            this.mapper.insert(confirmOrder);
        } else {
            confirmOrder.setUpdateTime(now);
            this.mapper.updateById(confirmOrder);
        }
    }

    @Override
    public PageResponse<ConfirmOrderQueryResponse> queryList(ConfirmOrderQueryVO queryVO) {
        QueryWrapper<ConfirmOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        PageHelper.startPage(queryVO.getPage(), queryVO.getSize());
        List<ConfirmOrder> confirmOrders = this.mapper.selectList(queryWrapper);
        PageInfo<ConfirmOrder> pageInfo = new PageInfo<>(confirmOrders);
        LOG.info("本次查询总行数：{}", pageInfo.getTotal());
        LOG.info("本次查询总页数：{}", pageInfo.getPages());

        List<ConfirmOrderQueryResponse> list = BeanUtil.copyToList(confirmOrders, ConfirmOrderQueryResponse.class);
        PageResponse<ConfirmOrderQueryResponse> response = new PageResponse<>();
        response.setTotal(pageInfo.getTotal());
        response.setList(list);
        return response;
    }

    @Override
    public void delete(Long id) {
        this.mapper.deleteById(id);
    }

    @Override
    public void doConfirm(ConfirmOrderDoVO req) {
        // 省略业务数据校验，如：车次是否存在，余票是否存在，车次是否在有效期内，tickets条数>0，同乘客同车次是否已买过

        // 保存确认订单表，状态初始
        DateTime now = DateTime.now();
        ConfirmOrder confirmOrder = new ConfirmOrder();

        Date date = req.getDate();
        String trainCode = req.getTrainCode();
        String start = req.getStart();
        String end = req.getEnd();
        confirmOrder.setId(SnowUtil.getSnowflakeNextId());
        confirmOrder.setCreateTime(now);
        confirmOrder.setUpdateTime(now);
        confirmOrder.setMemberId(LoginMemberContext.getId());
        confirmOrder.setDate(date);
        confirmOrder.setTrainCode(trainCode);
        confirmOrder.setStart(req.getStart());
        confirmOrder.setEnd(req.getEnd());
        confirmOrder.setDailyTrainTicketId(req.getDailyTrainTicketId());
        confirmOrder.setStatus(ConfirmOrderStatusEnum.INIT.getCode());

        List<ConfirmOrderTicketVO> tickets = req.getTickets();
        confirmOrder.setTickets(JSON.toJSONString(tickets));
        this.mapper.insert(confirmOrder);

        // 查出余票记录，需要得到真实的库存
        DailyTrainTicket dailyTrainTicket = this.dailyTrainTicketService.selectByUnique(date, trainCode, start, end);
        LOG.info("查出余票记录:{}", dailyTrainTicket);
        // 扣减余票数量，并判断余票是否足够
        reduceTickets(req, dailyTrainTicket);

        // 最终的选票结果
        ArrayList<DailyTrainSeat> finalSeatList = new ArrayList<>();

        ConfirmOrderTicketVO ticketVO0 = tickets.get(0);
        if (StrUtil.isNotBlank(ticketVO0.getSeat())) {
            LOG.info("本次购票有选座");
            // 查询本次选座的座位类型都有哪些列，计算所选座位与第一个座位的偏离值
            List<SeatColEnum> cols = SeatColEnum.getColsByType(ticketVO0.getSeatTypeCode());
            LOG.info("本次选座的座位包含的列:{}", cols);

            ArrayList<String> referSeatList = new ArrayList<>();
            for (int i = 1; i < 2; i++) {
                for (SeatColEnum col : cols) {
                    referSeatList.add(col.getCode() + i);
                }
            }
            LOG.info("初始化用于参照的两排座位：{}", referSeatList);

            // 计算绝对偏移值：在参照座位中的位置
            List<Integer> absoluteOffsetList = new ArrayList<>();
            for (ConfirmOrderTicketVO ticketVO : tickets) {
                int index = referSeatList.indexOf(ticketVO.getSeat());
                absoluteOffsetList.add(index);
            }
            LOG.info("计算得到所有座位的绝对偏移值：{}", absoluteOffsetList);

            List<Integer> offsetList = new ArrayList<>();

            for (Integer index : absoluteOffsetList) {
                int offset = index - absoluteOffsetList.get(0);
                offsetList.add(offset);
            }
            LOG.info("计算得到所有座位的相对第一个座位的偏移值：{}", absoluteOffsetList);

            this.getSeat(date, trainCode, ticketVO0.getSeatTypeCode(), ticketVO0.getSeat().split("")[0], offsetList, dailyTrainTicket.getStartIndex(), dailyTrainTicket.getEndIndex(), finalSeatList);
        } else {
            LOG.info("本次购票无选座");

            for (ConfirmOrderTicketVO ticketVO : tickets) {
                this.getSeat(date, trainCode, ticketVO.getSeatTypeCode(), null, null, dailyTrainTicket.getStartIndex(), dailyTrainTicket.getEndIndex(), finalSeatList);
            }
        }

        LOG.info("最终选座", finalSeatList);
        // 选中座位后事务处理：
        afterConfirmOrderService.AfterDoConfirm(dailyTrainTicket, finalSeatList);
    }


    /**
     * 挑座位：如果有选中，一次性挑完，无选座一个个挑
     *
     * @param date
     * @param trainCode
     * @param seatType
     * @param column
     * @param offsetList
     */
    private void getSeat(Date date, String trainCode, String seatType, String column, List<Integer> offsetList, Integer startIndex, Integer endIndex, ArrayList<DailyTrainSeat> finalSeatList) {
        ArrayList<DailyTrainSeat> getSeatList = new ArrayList<>();
        List<DailyTrainCarriage> trainCarriages = this.dailyTrainCarriageService.selectByType(date, trainCode, seatType);
        LOG.info("共查出{}个符合条件的车厢", trainCarriages.size());

        // 一个车厢一个车厢的获取数据
        for (DailyTrainCarriage trainCarriage : trainCarriages) {
            LOG.info("开始从车厢{}选座", trainCarriage.getIndex());
            getSeatList = new ArrayList<>();

            List<DailyTrainSeat> seatList = this.dailyTrainSeatService.selectByCarriage(date, trainCode, trainCarriage.getIndex());
            LOG.info("车厢{}的座位数：{}", trainCarriage.getIndex(), seatList.size());

            for (int i = 0; i < seatList.size(); i++) {
                DailyTrainSeat seat = seatList.get(i);
                String col = seat.getCol();
                Integer seatIndex = seat.getCarriageSeatIndex();

                // 判断当前座位不能被选中过
                boolean alreadyChooseFlag = false;
                for (DailyTrainSeat finalSeat : finalSeatList) {
                    if (finalSeat.getId().equals(seat.getId())) {
                        alreadyChooseFlag = true;
                        break;
                    }
                }
                if (alreadyChooseFlag) {
                    continue;
                }
                // 判断column，有值要对比列号
                if (StrUtil.isBlank(col)) {
                    LOG.info("无选座");
                } else {
                    if (!column.equals(column)) {
                        LOG.info("座位{}列值不对，极限判断下一个座位，当前列值:{},目标列值:{}", seatIndex, col, column);
                        continue; // 拦截 座位列值不对
                    }
                }
                boolean isChoose = this.calSell(seat, startIndex, endIndex);
                if (isChoose) {
                    getSeatList.add(seat);
                    LOG.info("选中座位");
                } else {
                    continue;
                }

                // 根据offset选剩下的座位
                boolean isGetAllOffsetSeat = true;
                if (CollUtil.isNotEmpty(offsetList)) {
                    LOG.info("有偏移值:{},校验偏移的座位是否可选", offsetList);
                    for (int j = 1; j < offsetList.size(); j++) {
                        Integer offset = offsetList.get(j);
                        int nextIndex = i + offset - 1;

                        // 有选座时，一定是在同一个车厢
                        if (nextIndex >= seatList.size()) {
                            LOG.info("座位{}不可选,偏移后的索引超出车厢的座位数", nextIndex);
                            isGetAllOffsetSeat = false;
                            break;
                        }

                        DailyTrainSeat nextSeat = seatList.get(nextIndex);

                        boolean isNextChoose = this.calSell(nextSeat, startIndex, endIndex);
                        if (isNextChoose) {
                            getSeatList.add(nextSeat);
                            LOG.info("座位{}被选中", nextSeat.getCarriageSeatIndex());
                            return;
                        } else {
                            LOG.info("座位{}不可选", nextSeat.getCarriageSeatIndex());
                            isGetAllOffsetSeat = false;
                            break;
                        }
                    }
                }

                if (!isGetAllOffsetSeat) {
                    getSeatList = new ArrayList<>();
                    continue;
                }

                // 保存选好的座位
                finalSeatList.addAll(getSeatList);
                return;
            }
        }
    }

    /**
     * 计算某座位在区间内是否可卖
     * 例子：sell=10001，本次购买区间站1-4 则区间已售000
     * 全部是0表示区间可买，只要1就表示区间内已经售过票了
     * <p>
     * 选中后，要计算购票后sell，比如原本10001，本次购买1-4
     * 方案：构造本次购票造成的售卖信息:01110 与 原sell 10001 按位与，最终得到11111
     */
    private boolean calSell(DailyTrainSeat seat, Integer startIndex, Integer endIndex) {
        String sell = seat.getSell();
        String sellPart = sell.substring(startIndex, endIndex);
        if (Integer.parseInt(sellPart) > 0) {
            LOG.info("座位{}在本站区间{}-{}已售卖，不可选中该座位", seat.getCarriageSeatIndex(), startIndex, endIndex);
            return false;
        } else {
            LOG.info("座位{}在本站区间{}-{}未售卖，可选中该座位", seat.getCarriageSeatIndex(), startIndex, endIndex);
            String curSell = sellPart.replace('0', '1'); // 111
            curSell = StrUtil.fillBefore(curSell, '0', endIndex);// 0111
            curSell = StrUtil.fillAfter(curSell, '0', sell.length());

            // 当前区间售票信息与已售sell 按位与运算
            int newSellInt = NumberUtil.binaryToInt(curSell) | NumberUtil.binaryToInt(sell);

            String newSell = NumberUtil.getBinaryStr(newSellInt);
            newSell = StrUtil.fillBefore(newSell, '0', endIndex);
            LOG.info("座位{}被选中，原售票信息{}，车站区间{}-{}，即：{},最终售票信息:{}", seat.getCarriageSeatIndex(), sell, startIndex, endIndex, curSell, newSell);
            seat.setSell(newSell);
            return true;
        }
    }

    private static void reduceTickets(ConfirmOrderDoVO req, DailyTrainTicket dailyTrainTicket) {
        for (ConfirmOrderTicketVO ticketVO : req.getTickets()) {
            String seatTypeCode = ticketVO.getSeatTypeCode();
            SeatTypeEnum seatTypeEnum = EnumUtil.getBy(SeatTypeEnum::getCode, seatTypeCode);

            switch (seatTypeEnum) {
                case YDZ -> {
                    int countLeft = dailyTrainTicket.getYdz() - 1;
                    if (countLeft < 0) {
                        throw new BusinessException(BusinessExceptionCode.CONFIRM_ORDER_TICKET_COUNT_ERROR);
                    }
                    dailyTrainTicket.setYdz(countLeft);
                }
                case EDZ -> {
                    int countLeft = dailyTrainTicket.getEdz() - 1;
                    if (countLeft < 0) {
                        throw new BusinessException(BusinessExceptionCode.CONFIRM_ORDER_TICKET_COUNT_ERROR);
                    }
                    dailyTrainTicket.setEdz(countLeft);
                }
                case RW -> {
                    int countLeft = dailyTrainTicket.getRw() - 1;
                    if (countLeft < 0) {
                        throw new BusinessException(BusinessExceptionCode.CONFIRM_ORDER_TICKET_COUNT_ERROR);
                    }
                    dailyTrainTicket.setRw(countLeft);
                }
                case YW -> {
                    int countLeft = dailyTrainTicket.getYw() - 1;
                    if (countLeft < 0) {
                        throw new BusinessException(BusinessExceptionCode.CONFIRM_ORDER_TICKET_COUNT_ERROR);
                    }
                    dailyTrainTicket.setYw(countLeft);
                }
            }
        }
    }
}




