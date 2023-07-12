package fun.timu.train.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import fun.timu.train.business.entity.Train;
import fun.timu.train.business.mapper.TrainMapper;
import fun.timu.train.business.request.train.TrainQueryVO;
import fun.timu.train.business.request.train.TrainSaveVO;
import fun.timu.train.business.response.train.TrainQueryResponse;
import fun.timu.train.business.service.TrainService;
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
 * @description 针对表【train(车次)】的数据库操作Service实现
 * @createDate 2023-07-11 15:21:07
 */
@Service
public class TrainServiceImpl extends ServiceImpl<TrainMapper, Train>
        implements TrainService {
    private static final Logger LOG = LoggerFactory.getLogger(TrainService.class);

    private final TrainMapper mapper;

    public TrainServiceImpl(TrainMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void save(TrainSaveVO saveVO) {
        DateTime now = DateTime.now();
        Train train = BeanUtil.copyProperties(saveVO, Train.class);
        if (ObjectUtil.isNull(train.getId())) {
            Train trainDB = selectByUnique(saveVO.getCode());
            if (ObjectUtil.isNotEmpty(trainDB)) {
                throw new BusinessException(BusinessExceptionCode.BUSINESS_TRAIN_CODE_UNIQUE_ERROR);
            }

            train.setId(SnowUtil.getSnowflakeNextId());
            train.setCreateTime(now);
            train.setUpdateTime(now);
            mapper.insert(train);
        } else {
            train.setUpdateTime(now);
            mapper.updateById(train);
        }
    }

    private Train selectByUnique(String code) {
        QueryWrapper<Train> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("code", code);
        List<Train> trains = mapper.selectList(queryWrapper);
        if (CollUtil.isNotEmpty(trains)) {
            return trains.get(0);
        } else {
            return null;
        }
    }

    @Override
    public PageResponse<TrainQueryResponse> queryList(TrainQueryVO queryVO) {
        PageHelper.startPage(queryVO.getPage(), queryVO.getSize());
        QueryWrapper<Train> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("code");
        List<Train> trains = mapper.selectList(queryWrapper);

        PageInfo<Train> pageInfo = new PageInfo<>(trains);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());

        List<TrainQueryResponse> list = BeanUtil.copyToList(trains, TrainQueryResponse.class);

        PageResponse<TrainQueryResponse> response = new PageResponse<>();
        response.setTotal(pageInfo.getTotal());
        response.setList(list);
        return response;
    }

    @Override
    public void delete(Long id) {
        mapper.deleteById(id);
    }

    @Override
    public List<TrainQueryResponse> queryAll() {
        List<Train> trains = selectAll();
        return BeanUtil.copyToList(trains, TrainQueryResponse.class);
    }

    @Override
    public List<Train> selectAll() {
        QueryWrapper<Train> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("code");
        return mapper.selectList(queryWrapper);
    }
}




