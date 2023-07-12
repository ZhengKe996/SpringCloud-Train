package fun.timu.train.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import fun.timu.train.business.entity.TrainCarriage;
import fun.timu.train.business.entity.TrainSeat;
import fun.timu.train.business.enums.SeatColEnum;
import fun.timu.train.business.mapper.TrainSeatMapper;
import fun.timu.train.business.request.train.TrainSeatQueryVO;
import fun.timu.train.business.request.train.TrainSeatSaveVO;
import fun.timu.train.business.response.train.TrainSeatQueryResponse;
import fun.timu.train.business.service.TrainCarriageService;
import fun.timu.train.business.service.TrainSeatService;
import fun.timu.train.commo.response.PageResponse;
import fun.timu.train.commo.utils.SnowUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author zhengke
 * @description 针对表【train_seat(座位)】的数据库操作Service实现
 * @createDate 2023-07-11 15:21:07
 */
@Service
public class TrainSeatServiceImpl extends ServiceImpl<TrainSeatMapper, TrainSeat>
        implements TrainSeatService {

    private static final Logger LOG = LoggerFactory.getLogger(TrainSeatService.class);

    private final TrainSeatMapper mapper;
    private final TrainCarriageService carriageService;

    public TrainSeatServiceImpl(TrainSeatMapper mapper, TrainCarriageService carriageService) {
        this.mapper = mapper;
        this.carriageService = carriageService;
    }

    @Override
    public void save(TrainSeatSaveVO saveVO) {
        DateTime now = DateTime.now();
        TrainSeat trainSeat = BeanUtil.copyProperties(saveVO, TrainSeat.class);
        if (ObjectUtil.isNull(trainSeat.getId())) {
            trainSeat.setId(SnowUtil.getSnowflakeNextId());
            trainSeat.setCreateTime(now);
            trainSeat.setUpdateTime(now);
            this.mapper.insert(trainSeat);
        } else {
            trainSeat.setUpdateTime(now);
            this.mapper.updateById(trainSeat);
        }

    }

    @Override
    public PageResponse<TrainSeatQueryResponse> queryList(TrainSeatQueryVO queryVO) {
        QueryWrapper<TrainSeat> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("train_code");
        queryWrapper.orderByAsc("carriage_index");
        queryWrapper.orderByAsc("carriage_seat_index");

        queryWrapper.eq(ObjectUtil.isNotEmpty(queryVO.getTrainCode()), "train_code", queryVO.getTrainCode());
        PageHelper.startPage(queryVO.getPage(), queryVO.getSize());
        List<TrainSeat> trainSeats = this.mapper.selectList(queryWrapper);
        PageInfo<TrainSeat> pageInfo = new PageInfo<>(trainSeats);
        LOG.info("本次查询总行数：{}", pageInfo.getTotal());
        LOG.info("本次查询总页数：{}", pageInfo.getPages());

        List<TrainSeatQueryResponse> list = BeanUtil.copyToList(trainSeats, TrainSeatQueryResponse.class);

        PageResponse<TrainSeatQueryResponse> response = new PageResponse<>();
        response.setTotal(pageInfo.getTotal());
        response.setList(list);
        return null;
    }

    @Override
    public void delete(Long id) {
        this.mapper.deleteById(id);
    }

    @Override
    @Transactional
    public void genTrainSeat(String trainCode) {
        DateTime now = DateTime.now();
        // 清空当前车次下的所有的座位记录
        QueryWrapper<TrainSeat> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(ObjectUtil.isNotNull(trainCode), "train_code", trainCode);
        this.mapper.delete(queryWrapper);

        // 查找当前车次下的所有的车厢
        List<TrainCarriage> trainCarriageList = this.carriageService.selectByTrainCode(trainCode);
        LOG.info("当前车次下的车厢数：{}", trainCarriageList.size());

        // 循环生成每个车厢的座位
        for (TrainCarriage trainCarriage : trainCarriageList) {
            // 拿到车厢数据：行数、座位类型(得到列数)
            Integer rowCount = trainCarriage.getRowCount();
            String seatType = trainCarriage.getSeatType();
            int seatIndex = 1;

            // 根据车厢的座位类型，筛选出所有的列，比如车箱类型是一等座，则筛选出columnList={ACDF}
            List<SeatColEnum> colEnumList = SeatColEnum.getColsByType(seatType);
            LOG.info("根据车厢的座位类型，筛选出所有的列：{}", colEnumList);

            // 循环行数
            for (int row = 1; row <= rowCount; row++) {
                // 循环列数
                for (SeatColEnum seatColEnum : colEnumList) {
                    // 构造座位数据并保存数据库
                    TrainSeat trainSeat = new TrainSeat();
                    trainSeat.setId(SnowUtil.getSnowflakeNextId());
                    trainSeat.setTrainCode(trainCode);
                    trainSeat.setCarriageIndex(trainCarriage.getIndex());
                    trainSeat.setRow(StrUtil.fillBefore(String.valueOf(row), '0', 2));
                    trainSeat.setCol(seatColEnum.getCode());
                    trainSeat.setSeatType(seatType);
                    trainSeat.setCarriageSeatIndex(seatIndex++);
                    trainSeat.setCreateTime(now);
                    trainSeat.setUpdateTime(now);
                    this.mapper.insert(trainSeat);
                }
            }
        }

    }

    @Override
    public List<TrainSeat> selectByTrainCode(String trainCode) {
        QueryWrapper<TrainSeat> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("id");
        queryWrapper.eq(ObjectUtil.isNotNull(trainCode), "train_code", trainCode);
        return this.mapper.selectList(queryWrapper);
    }
}




