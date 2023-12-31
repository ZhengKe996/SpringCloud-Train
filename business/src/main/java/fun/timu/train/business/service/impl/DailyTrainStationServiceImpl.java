package fun.timu.train.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import fun.timu.train.business.entity.DailyTrainStation;
import fun.timu.train.business.entity.TrainStation;
import fun.timu.train.business.mapper.DailyTrainStationMapper;
import fun.timu.train.business.request.train.DailyTrainStationQueryVO;
import fun.timu.train.business.request.train.DailyTrainStationSaveVO;
import fun.timu.train.business.response.daily.DailyTrainStationQueryResponse;
import fun.timu.train.business.service.DailyTrainStationService;
import fun.timu.train.business.service.TrainStationService;
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
 * @description 针对表【daily_train_station(每日车站)】的数据库操作Service实现
 * @createDate 2023-07-11 15:21:07
 */
@Service
public class DailyTrainStationServiceImpl extends ServiceImpl<DailyTrainStationMapper, DailyTrainStation>
        implements DailyTrainStationService {
    private static final Logger LOG = LoggerFactory.getLogger(DailyTrainStationService.class);

    private final DailyTrainStationMapper mapper;

    private final TrainStationService trainStationService;

    public DailyTrainStationServiceImpl(DailyTrainStationMapper dailyTrainStationMapper, TrainStationService trainStationService) {
        this.mapper = dailyTrainStationMapper;
        this.trainStationService = trainStationService;
    }

    @Override
    public void save(DailyTrainStationSaveVO req) {
        DateTime now = DateTime.now();
        DailyTrainStation dailyTrainStation = BeanUtil.copyProperties(req, DailyTrainStation.class);
        if (ObjectUtil.isNull(dailyTrainStation.getId())) {
            dailyTrainStation.setId(SnowUtil.getSnowflakeNextId());
            dailyTrainStation.setCreateTime(now);
            dailyTrainStation.setUpdateTime(now);
            this.mapper.insert(dailyTrainStation);
        } else {
            dailyTrainStation.setUpdateTime(now);
            this.mapper.updateById(dailyTrainStation);
        }
    }

    @Override
    public PageResponse<DailyTrainStationQueryResponse> queryList(DailyTrainStationQueryVO queryVO) {
        QueryWrapper<DailyTrainStation> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("date");
        queryWrapper.orderByAsc("train_code");
        queryWrapper.orderByAsc("`index`");

        queryWrapper.eq(ObjUtil.isNotNull(queryVO.getDate()), "date", queryVO.getDate());
        queryWrapper.eq(ObjUtil.isNotEmpty(queryVO.getTrainCode()), "train_code", queryVO.getTrainCode());

        PageHelper.startPage(queryVO.getPage(), queryVO.getSize());
        List<DailyTrainStation> trainStations = this.mapper.selectList(queryWrapper);

        PageInfo<DailyTrainStation> pageInfo = new PageInfo<>(trainStations);
        LOG.info("本次查询总行数：{}", pageInfo.getTotal());
        LOG.info("本次查询总页数：{}", pageInfo.getPages());

        List<DailyTrainStationQueryResponse> list = BeanUtil.copyToList(trainStations, DailyTrainStationQueryResponse.class);
        PageResponse<DailyTrainStationQueryResponse> pageResp = new PageResponse<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);
        return pageResp;
    }

    @Override
    public void delete(Long id) {
        this.mapper.deleteById(id);
    }

    @Override
    @Transactional
    public void genDaily(Date date, String trainCode) {
        LOG.info("生成日期【{}】车次【{}】的车站信息开始", DateUtil.formatDate(date), trainCode);

        // 删除某日某车次的车站信息
        QueryWrapper<DailyTrainStation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("date", date);
        queryWrapper.eq("train_code", trainCode);
        this.mapper.delete(queryWrapper);

        // 查出某车次的所有的车站信息
        List<TrainStation> stationList = trainStationService.selectByTrainCode(trainCode);
        if (CollUtil.isEmpty(stationList)) {
            LOG.info("该车次没有车站基础数据，生成该车次的车站信息结束");
            return;
        }

        for (TrainStation trainStation : stationList) {
            DateTime now = DateTime.now();
            DailyTrainStation dailyTrainStation = BeanUtil.copyProperties(trainStation, DailyTrainStation.class);
            dailyTrainStation.setId(SnowUtil.getSnowflakeNextId());
            dailyTrainStation.setCreateTime(now);
            dailyTrainStation.setUpdateTime(now);
            dailyTrainStation.setDate(date);
            this.mapper.insert(dailyTrainStation);
        }
        LOG.info("生成日期【{}】车次【{}】的车站信息结束", DateUtil.formatDate(date), trainCode);
    }
}




