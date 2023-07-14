package fun.timu.train.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import fun.timu.train.business.entity.DailyTrainSeat;
import fun.timu.train.business.mapper.DailyTrainSeatMapper;
import fun.timu.train.business.request.daily.DailyTrainSeatQueryVO;
import fun.timu.train.business.request.daily.DailyTrainSeatSaveVO;
import fun.timu.train.business.response.daily.DailyTrainSeatQueryResponse;
import fun.timu.train.business.service.DailyTrainSeatService;
import fun.timu.train.business.service.TrainSeatService;
import fun.timu.train.business.service.TrainStationService;
import fun.timu.train.commo.response.PageResponse;
import fun.timu.train.commo.utils.SnowUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author zhengke
 * @description 针对表【daily_train_seat(每日座位)】的数据库操作Service实现
 * @createDate 2023-07-11 15:21:07
 */
@Service
public class DailyTrainSeatServiceImpl extends ServiceImpl<DailyTrainSeatMapper, DailyTrainSeat>
        implements DailyTrainSeatService {
    private static final Logger LOG = LoggerFactory.getLogger(DailyTrainSeatService.class);

    private final DailyTrainSeatMapper mapper;

    private final TrainSeatService trainSeatService;

    private final TrainStationService trainStationService;

    public DailyTrainSeatServiceImpl(DailyTrainSeatMapper dailyTrainSeatMapper, DailyTrainSeatMapper mapper, TrainSeatService trainSeatService, TrainStationService trainStationService) {
        this.mapper = mapper;
        this.trainSeatService = trainSeatService;
        this.trainStationService = trainStationService;
    }

    @Override
    public void save(DailyTrainSeatSaveVO saveVO) {
        DateTime now = DateTime.now();
        DailyTrainSeat dailyTrainSeat = BeanUtil.copyProperties(saveVO, DailyTrainSeat.class);

        if (ObjectUtil.isNull(dailyTrainSeat.getId())) {
            dailyTrainSeat.setId(SnowUtil.getSnowflakeNextId());
            dailyTrainSeat.setCreateTime(now);
            dailyTrainSeat.setUpdateTime(now);
            mapper.insert(dailyTrainSeat);
        } else {
            dailyTrainSeat.setUpdateTime(now);
            mapper.updateById(dailyTrainSeat);
        }
    }

    @Override
    public PageResponse<DailyTrainSeatQueryResponse> queryList(DailyTrainSeatQueryVO queryVO) {
        QueryWrapper<DailyTrainSeat> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("date");
        queryWrapper.orderByAsc("train_code");
        queryWrapper.orderByAsc("carriage_index");
        queryWrapper.orderByAsc("carriage_seat_index");

        queryWrapper.eq(ObjectUtil.isNotEmpty(queryVO.getTrainCode()), "train_code", queryVO.getTrainCode());
        PageHelper.startPage(queryVO.getPage(), queryVO.getSize());
        List<DailyTrainSeat> trainSeats = this.mapper.selectList(queryWrapper);
        PageInfo<DailyTrainSeat> pageInfo = new PageInfo<>(trainSeats);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());

        List<DailyTrainSeatQueryResponse> list = BeanUtil.copyToList(trainSeats, DailyTrainSeatQueryResponse.class);

        PageResponse<DailyTrainSeatQueryResponse> response = new PageResponse<>();
        response.setTotal(pageInfo.getTotal());
        response.setList(list);
        return response;
    }

    @Override
    public void delete(Long id) {
        this.mapper.deleteById(id);
    }

    @Override
    public void genDaily(Date date, String trainCode) {

    }

    @Override
    public int countSeat(Date date, String trainCode, String seatType) {
        return 0;
    }
}




