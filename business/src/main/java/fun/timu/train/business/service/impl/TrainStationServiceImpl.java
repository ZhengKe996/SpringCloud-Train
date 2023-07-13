package fun.timu.train.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import fun.timu.train.business.entity.TrainStation;
import fun.timu.train.business.mapper.TrainStationMapper;
import fun.timu.train.business.request.train.TrainStationQueryVO;
import fun.timu.train.business.request.train.TrainStationSaveVO;
import fun.timu.train.business.response.train.TrainStationQueryResponse;
import fun.timu.train.business.service.TrainStationService;
import fun.timu.train.commo.exception.BusinessException;
import fun.timu.train.commo.exception.BusinessExceptionCode;
import fun.timu.train.commo.response.PageResponse;
import fun.timu.train.commo.utils.SnowUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zhengke
 * @description 针对表【train_station(火车车站)】的数据库操作Service实现
 * @createDate 2023-07-11 15:21:07
 */
@Service
public class TrainStationServiceImpl extends ServiceImpl<TrainStationMapper, TrainStation> implements TrainStationService {
    private static final Logger LOG = LoggerFactory.getLogger(TrainStationService.class);

    private final TrainStationMapper mapper;

    public TrainStationServiceImpl(TrainStationMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void save(TrainStationSaveVO saveVO) {
        DateTime now = DateTime.now();
        TrainStation trainStation = BeanUtil.copyProperties(saveVO, TrainStation.class);

        if (ObjectUtil.isNull(trainStation.getId())) {
            TrainStation station = this.selectByUnique(saveVO.getTrainCode(), saveVO.getIndex());
            if (ObjectUtil.isNotEmpty(station)) {
                throw new BusinessException(BusinessExceptionCode.BUSINESS_TRAIN_STATION_INDEX_UNIQUE_ERROR);
            }

            trainStation.setId(SnowUtil.getSnowflakeNextId());
            trainStation.setCreateTime(now);
            trainStation.setUpdateTime(now);
            mapper.insert(trainStation);
        } else {
            trainStation.setUpdateTime(now);
            mapper.updateById(trainStation);
        }
    }

    private TrainStation selectByUnique(String trainCode, Integer index) {
        QueryWrapper<TrainStation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(ObjectUtil.isNotNull(trainCode), "train_code", trainCode);
        queryWrapper.eq(ObjectUtil.isNotNull(index), "`index`", index);

        List<TrainStation> stations = this.mapper.selectList(queryWrapper);

        if (CollUtil.isNotEmpty(stations)) {
            return stations.get(0);
        } else {
            return null;
        }
    }

    private TrainStation selectByUnique(String trainCode, String name) {
        QueryWrapper<TrainStation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(ObjectUtil.isNotNull(trainCode), "train_code", trainCode);
        queryWrapper.eq(ObjectUtil.isNotNull(name), "name", name);

        List<TrainStation> stations = this.mapper.selectList(queryWrapper);

        if (CollUtil.isNotEmpty(stations)) {
            return stations.get(0);
        } else {
            return null;
        }
    }

    @Override
    public PageResponse<TrainStationQueryResponse> queryList(TrainStationQueryVO queryVO) {
        QueryWrapper<TrainStation> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("train_code");
        queryWrapper.orderByAsc("index");
        queryWrapper.eq(ObjectUtil.isNotEmpty(queryVO.getTrainCode()), "train_code", queryVO.getTrainCode());

        PageHelper.startPage(queryVO.getPage(), queryVO.getSize());
        List<TrainStation> stations = this.mapper.selectList(queryWrapper);

        PageInfo<TrainStation> pageInfo = new PageInfo<>(stations);
        LOG.info("本次查询总行数：{}", pageInfo.getTotal());
        LOG.info("本次查询总页数：{}", pageInfo.getPages());

        List<TrainStationQueryResponse> list = BeanUtil.copyToList(stations, TrainStationQueryResponse.class);

        PageResponse<TrainStationQueryResponse> response = new PageResponse<>();
        response.setTotal(pageInfo.getTotal());
        response.setList(list);
        return response;
    }

    @Override
    public void delete(Long id) {
        this.mapper.deleteById(id);
    }

    @Override
    public List<TrainStation> selectByTrainCode(String trainCode) {
        QueryWrapper<TrainStation> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("index");
        queryWrapper.eq(ObjectUtil.isNotNull(trainCode), "train_code", trainCode);

        return this.mapper.selectList(queryWrapper);
    }
}




