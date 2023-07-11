package fun.timu.train.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import fun.timu.train.business.entity.Station;
import fun.timu.train.business.mapper.StationMapper;
import fun.timu.train.business.request.station.StationQueryVO;
import fun.timu.train.business.request.station.StationSaveVO;
import fun.timu.train.business.response.StationQueryResponse;
import fun.timu.train.business.service.StationService;
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
 * @description 针对表【station(车站)】的数据库操作Service实现
 * @createDate 2023-07-11 15:21:07
 */
@Service
public class StationServiceImpl extends ServiceImpl<StationMapper, Station> implements StationService {


    private static final Logger LOG = LoggerFactory.getLogger(StationService.class);
    private final StationMapper mapper;

    public StationServiceImpl(StationMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void save(StationSaveVO saveVO) {
        DateTime now = DateTime.now();
        Station station = BeanUtil.copyProperties(saveVO, Station.class);

        if (ObjectUtil.isNull(station.getId())) {
            // 保存之前，先校验唯一键是否存在
            Station stationDB = this.selectByUnique(saveVO.getName());
            if (ObjectUtil.isNotEmpty(stationDB)) {
                throw new BusinessException(BusinessExceptionCode.BUSINESS_STATION_NAME_UNIQUE_ERROR);
            }

            station.setId(SnowUtil.getSnowflakeNextId());
            station.setCreateTime(now);
            station.setUpdateTime(now);
            mapper.insert(station);
        } else {
            station.setUpdateTime(now);
            mapper.updateById(station);
        }

    }


    @Override
    public PageResponse<StationQueryResponse> queryList(StationQueryVO queryVO) {

        QueryWrapper<Station> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        PageHelper.startPage(queryVO.getPage(), queryVO.getSize());
        List<Station> stations = mapper.selectList(queryWrapper);
        PageInfo<Station> pageInfo = new PageInfo<>(stations);
        LOG.info("本次查询总行数：{}", pageInfo.getTotal());
        LOG.info("本次查询总页数：{}", pageInfo.getPages());

        List<StationQueryResponse> list = BeanUtil.copyToList(stations, StationQueryResponse.class);

        PageResponse<StationQueryResponse> response = new PageResponse<>();
        response.setTotal(pageInfo.getTotal());
        response.setList(list);
        return response;
    }

    @Override
    public void delete(Long id) {
        mapper.deleteById(id);
    }

    @Override
    public List<StationQueryResponse> queryAll() {
        QueryWrapper<Station> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("name_pinyin");
        List<Station> stations = mapper.selectList(queryWrapper);
        System.out.println(stations.size() + "----------");
        return BeanUtil.copyToList(stations, StationQueryResponse.class);
    }

    private Station selectByUnique(String name) {
        QueryWrapper<Station> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", name);
        List<Station> stations = mapper.selectList(queryWrapper);
        if (CollUtil.isNotEmpty(stations)) {
            return stations.get(0);
        } else {
            return null;
        }
    }
}




