package fun.timu.train.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import fun.timu.train.business.entity.DailyTrain;
import fun.timu.train.business.entity.Train;
import fun.timu.train.business.mapper.DailyTrainMapper;
import fun.timu.train.business.request.daily.DailyTrainQueryVO;
import fun.timu.train.business.request.daily.DailyTrainSaveVO;
import fun.timu.train.business.response.daily.DailyTrainQueryResponse;
import fun.timu.train.business.service.*;
import fun.timu.train.commo.response.PageResponse;
import fun.timu.train.commo.utils.SnowUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author zhengke
 * @description 针对表【daily_train(每日车次)】的数据库操作Service实现
 * @createDate 2023-07-11 15:21:07
 */
@Service
public class DailyTrainServiceImpl extends ServiceImpl<DailyTrainMapper, DailyTrain> implements DailyTrainService {

    private static final Logger LOG = LoggerFactory.getLogger(DailyTrainService.class);
    private final DailyTrainMapper dailyTrainMapper;
    private final TrainService trainService;
    private final DailyTrainStationService dailyTrainStationService;
    private final DailyTrainCarriageService dailyTrainCarriageService;
    private final DailyTrainSeatService dailyTrainSeatService;
    private final DailyTrainTicketService dailyTrainTicketService;

    public DailyTrainServiceImpl(DailyTrainMapper dailyTrainMapper, TrainService trainService, DailyTrainStationService dailyTrainStationService, DailyTrainCarriageService dailyTrainCarriageService, DailyTrainSeatService dailyTrainSeatService, DailyTrainTicketService dailyTrainTicketService) {
        this.dailyTrainMapper = dailyTrainMapper;
        this.trainService = trainService;
        this.dailyTrainStationService = dailyTrainStationService;
        this.dailyTrainCarriageService = dailyTrainCarriageService;
        this.dailyTrainSeatService = dailyTrainSeatService;
        this.dailyTrainTicketService = dailyTrainTicketService;
    }

    @Override
    public void save(DailyTrainSaveVO saveVO) {
        DateTime now = DateTime.now();
        DailyTrain dailyTrain = BeanUtil.copyProperties(saveVO, DailyTrain.class);

        if (ObjectUtil.isNull(dailyTrain.getId())) {
            dailyTrain.setId(SnowUtil.getSnowflakeNextId());
            dailyTrain.setCreateTime(now);
            dailyTrain.setUpdateTime(now);
            this.dailyTrainMapper.insert(dailyTrain);
        } else {
            dailyTrain.setUpdateTime(now);
            this.dailyTrainMapper.updateById(dailyTrain);
        }
    }

    @Override
    public PageResponse<DailyTrainQueryResponse> queryList(DailyTrainQueryVO queryVO) {
        QueryWrapper<DailyTrain> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("date");
        queryWrapper.orderByAsc("code");
        queryWrapper.eq(ObjectUtil.isNotNull(queryVO.getDate()), "date", queryVO.getDate());
        queryWrapper.eq(ObjectUtil.isNotNull(queryVO.getCode()), "code", queryVO.getCode());

        PageHelper.startPage(queryVO.getPage(), queryVO.getSize());
        List<DailyTrain> dailyTrains = this.dailyTrainMapper.selectList(queryWrapper);

        PageInfo<DailyTrain> pageInfo = new PageInfo<>(dailyTrains);
        LOG.info("本次查询总行数：{}", pageInfo.getTotal());
        LOG.info("本次查询总页数：{}", pageInfo.getPages());

        List<DailyTrainQueryResponse> list = BeanUtil.copyToList(dailyTrains, DailyTrainQueryResponse.class);

        PageResponse<DailyTrainQueryResponse> response = new PageResponse<>();
        response.setTotal(pageInfo.getTotal());
        response.setList(list);
        return response;
    }

    @Override
    public void delete(Long id) {
        this.dailyTrainMapper.deleteById(id);
    }

    /**
     * 生成某日所有车次信息，包括车次、车站、车厢、座位
     *
     * @param date
     */
    @Override
    public void genDaily(Date date) {
        List<Train> trainList = trainService.selectAll();
        if (CollUtil.isEmpty(trainList)) {
            LOG.info("没有车次基础数据，任务结束！");
            return;
        }
        for (Train train : trainList) {
            genDailyTrain(date, train);
        }
    }

    @Override
    @Transactional
    public void genDailyTrain(Date date, Train train) {
        LOG.info("生成日期【{}】车次【{}】的信息开始", DateUtil.formatDate(date), train.getCode());
        // 删除该车次已有的数据
        QueryWrapper<DailyTrain> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("date", date);
        queryWrapper.eq("code", train.getCode());
        this.dailyTrainMapper.delete(queryWrapper);
        // 生成该车次的数据
        DateTime now = DateTime.now();
        DailyTrain dailyTrain = BeanUtil.copyProperties(train, DailyTrain.class);
        dailyTrain.setId(SnowUtil.getSnowflakeNextId());
        dailyTrain.setCreateTime(now);
        dailyTrain.setUpdateTime(now);
        dailyTrain.setDate(date);

        this.dailyTrainMapper.insert(dailyTrain);
        // 生成该车次的车站数据
        this.dailyTrainStationService.genDaily(date, train.getCode());

        // 生成该车次的车厢数据
        this.dailyTrainCarriageService.genDaily(date, train.getCode());

        // 生成该车次的座位数据
        this.dailyTrainSeatService.genDaily(date, train.getCode());

        // 生成该车次的余票数据
        this.dailyTrainTicketService.genDaily(dailyTrain, date, train.getCode());

        LOG.info("生成日期【{}】车次【{}】的信息结束", DateUtil.formatDate(date), train.getCode());
    }
}




