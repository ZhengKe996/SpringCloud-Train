package fun.timu.train.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import fun.timu.train.business.entity.DailyTrain;
import fun.timu.train.business.entity.DailyTrainTicket;
import fun.timu.train.business.entity.TrainStation;
import fun.timu.train.business.enums.SeatTypeEnum;
import fun.timu.train.business.enums.TrainTypeEnum;
import fun.timu.train.business.mapper.DailyTrainTicketMapper;
import fun.timu.train.business.request.daily.DailyTrainTicketQueryVO;
import fun.timu.train.business.request.daily.DailyTrainTicketSaveVO;
import fun.timu.train.business.response.daily.DailyTrainTicketQueryResponse;
import fun.timu.train.business.service.DailyTrainSeatService;
import fun.timu.train.business.service.DailyTrainTicketService;
import fun.timu.train.business.service.TrainStationService;
import fun.timu.train.commo.response.PageResponse;
import fun.timu.train.commo.utils.SnowUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

/**
 * @author zhengke
 * @description 针对表【daily_train_ticket(余票信息)】的数据库操作Service实现
 * @createDate 2023-07-11 15:21:07
 */
@Service
public class DailyTrainTicketServiceImpl extends ServiceImpl<DailyTrainTicketMapper, DailyTrainTicket>
        implements DailyTrainTicketService {
    private static final Logger LOG = LoggerFactory.getLogger(DailyTrainTicketService.class);

    private final DailyTrainTicketMapper mapper;

    private final TrainStationService trainStationService;

    private final DailyTrainSeatService dailyTrainSeatService;

    public DailyTrainTicketServiceImpl(DailyTrainTicketMapper dailyTrainTicketMapper, TrainStationService trainStationService, DailyTrainSeatService dailyTrainSeatService) {
        this.mapper = dailyTrainTicketMapper;
        this.trainStationService = trainStationService;
        this.dailyTrainSeatService = dailyTrainSeatService;
    }

    @Override
    public void save(DailyTrainTicketSaveVO saveVO) {
        DateTime now = DateTime.now();
        DailyTrainTicket dailyTrainTicket = BeanUtil.copyProperties(saveVO, DailyTrainTicket.class);
        if (ObjectUtil.isNull(dailyTrainTicket.getId())) {
            dailyTrainTicket.setId(SnowUtil.getSnowflakeNextId());
            dailyTrainTicket.setCreateTime(now);
            dailyTrainTicket.setUpdateTime(now);
            this.mapper.insert(dailyTrainTicket);
        } else {
            dailyTrainTicket.setUpdateTime(now);
            this.mapper.updateById(dailyTrainTicket);
        }
    }

    @Override
    public PageResponse<DailyTrainTicketQueryResponse> queryList(DailyTrainTicketQueryVO queryVO) {
        QueryWrapper<DailyTrainTicket> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        queryWrapper.eq(ObjUtil.isNotNull(queryVO.getDate()), "date", queryVO.getDate());
        queryWrapper.eq(ObjUtil.isNotNull(queryVO.getTrainCode()), "train_code", queryVO.getTrainCode());
        queryWrapper.eq(ObjUtil.isNotNull(queryVO.getStart()), "start", queryVO.getStart());
        queryWrapper.eq(ObjUtil.isNotNull(queryVO.getEnd()), "end", queryVO.getEnd());


        PageHelper.startPage(queryVO.getPage(), queryVO.getSize());
        List<DailyTrainTicket> trainTickets = this.mapper.selectList(queryWrapper);

        PageInfo<DailyTrainTicket> pageInfo = new PageInfo<>(trainTickets);
        LOG.info("本次查询总行数：{}", pageInfo.getTotal());
        LOG.info("本次查询总页数：{}", pageInfo.getPages());

        List<DailyTrainTicketQueryResponse> list = BeanUtil.copyToList(trainTickets, DailyTrainTicketQueryResponse.class);

        PageResponse<DailyTrainTicketQueryResponse> response = new PageResponse<>();
        response.setTotal(pageInfo.getTotal());
        response.setList(list);
        return response;
    }

    @Override
    public void delete(Long id) {
        this.mapper.deleteById(id);
    }

    @Override
    @Transactional
    public void genDaily(DailyTrain dailyTrain, Date date, String trainCode) {
        LOG.info("生成日期【{}】车次【{}】的余票信息开始", DateUtil.formatDate(date), trainCode);

        // 删除某日某车次的余票信息
        QueryWrapper<DailyTrainTicket> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("date", date);
        queryWrapper.eq("train_code", trainCode);
        this.mapper.delete(queryWrapper);

        // 查出某车次的所有的车站信息
        List<TrainStation> stationList = trainStationService.selectByTrainCode(trainCode);
        if (CollUtil.isEmpty(stationList)) {
            LOG.info("该车次没有车站基础数据，生成该车次的余票信息结束");
            return;
        }

        DateTime now = DateTime.now();
        for (int i = 0; i < stationList.size(); i++) {
            // 得到出发站
            TrainStation trainStationStart = stationList.get(i);
            BigDecimal sumKM = BigDecimal.ZERO;
            for (int j = (i + 1); j < stationList.size(); j++) {
                TrainStation trainStationEnd = stationList.get(j);
                sumKM = sumKM.add(trainStationEnd.getKm());

                DailyTrainTicket dailyTrainTicket = new DailyTrainTicket();
                dailyTrainTicket.setId(SnowUtil.getSnowflakeNextId());
                dailyTrainTicket.setDate(date);
                dailyTrainTicket.setTrainCode(trainCode);
                dailyTrainTicket.setStart(trainStationStart.getName());
                dailyTrainTicket.setStartPinyin(trainStationStart.getNamePinyin());
                dailyTrainTicket.setStartTime(trainStationStart.getOutTime());
                dailyTrainTicket.setStartIndex(trainStationStart.getIndex());
                dailyTrainTicket.setEnd(trainStationEnd.getName());
                dailyTrainTicket.setEndPinyin(trainStationEnd.getNamePinyin());
                dailyTrainTicket.setEndTime(trainStationEnd.getInTime());
                dailyTrainTicket.setEndIndex(trainStationEnd.getIndex());
                int ydz = dailyTrainSeatService.countSeat(date, trainCode, SeatTypeEnum.YDZ.getCode());
                int edz = dailyTrainSeatService.countSeat(date, trainCode, SeatTypeEnum.EDZ.getCode());
                int rw = dailyTrainSeatService.countSeat(date, trainCode, SeatTypeEnum.RW.getCode());
                int yw = dailyTrainSeatService.countSeat(date, trainCode, SeatTypeEnum.YW.getCode());
                // 票价 = 里程之和 * 座位单价 * 车次类型系数
                String trainType = dailyTrain.getType();
                // 计算票价系数：TrainTypeEnum.priceRate
                BigDecimal priceRate = EnumUtil.getFieldBy(TrainTypeEnum::getPriceRate, TrainTypeEnum::getCode, trainType);
                BigDecimal ydzPrice = sumKM.multiply(SeatTypeEnum.YDZ.getPrice()).multiply(priceRate).setScale(2, RoundingMode.HALF_UP);
                BigDecimal edzPrice = sumKM.multiply(SeatTypeEnum.EDZ.getPrice()).multiply(priceRate).setScale(2, RoundingMode.HALF_UP);
                BigDecimal rwPrice = sumKM.multiply(SeatTypeEnum.RW.getPrice()).multiply(priceRate).setScale(2, RoundingMode.HALF_UP);
                BigDecimal ywPrice = sumKM.multiply(SeatTypeEnum.YW.getPrice()).multiply(priceRate).setScale(2, RoundingMode.HALF_UP);
                dailyTrainTicket.setYdz(ydz);
                dailyTrainTicket.setYdzPrice(ydzPrice);
                dailyTrainTicket.setEdz(edz);
                dailyTrainTicket.setEdzPrice(edzPrice);
                dailyTrainTicket.setRw(rw);
                dailyTrainTicket.setRwPrice(rwPrice);
                dailyTrainTicket.setYw(yw);
                dailyTrainTicket.setYwPrice(ywPrice);
                dailyTrainTicket.setCreateTime(now);
                dailyTrainTicket.setUpdateTime(now);
                this.mapper.insert(dailyTrainTicket);
            }
        }
        LOG.info("生成日期【{}】车次【{}】的余票信息结束", DateUtil.formatDate(date), trainCode);
    }

    @Override
    public DailyTrainTicket selectByUnique(Date date, String trainCode, String start, String end) {
        QueryWrapper<DailyTrainTicket> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("date", date);
        queryWrapper.eq("start", start);
        queryWrapper.eq("end", end);
        queryWrapper.eq("train_code", trainCode);
        List<DailyTrainTicket> list = this.mapper.selectList(queryWrapper);

        if (CollUtil.isNotEmpty(list)) {
            return list.get(0);
        } else {
            return null;
        }
    }
}




