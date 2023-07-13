package fun.timu.train.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
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
import fun.timu.train.business.service.DailyTrainService;
import fun.timu.train.business.service.TrainService;
import fun.timu.train.commo.response.PageResponse;
import fun.timu.train.commo.utils.SnowUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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

    public DailyTrainServiceImpl(DailyTrainMapper dailyTrainMapper, TrainService trainService) {
        this.dailyTrainMapper = dailyTrainMapper;
        this.trainService = trainService;
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

    @Override
    public void genDaily(Date date) {

    }

    @Override
    public void genDailyTrain(Date date, Train train) {

    }
}




