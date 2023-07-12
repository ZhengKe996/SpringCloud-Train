package fun.timu.train.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import fun.timu.train.business.entity.TrainCarriage;
import fun.timu.train.business.enums.SeatColEnum;
import fun.timu.train.business.mapper.TrainCarriageMapper;
import fun.timu.train.business.request.train.TrainCarriageQueryVO;
import fun.timu.train.business.request.train.TrainCarriageSaveVO;
import fun.timu.train.business.response.train.TrainCarriageQueryResponse;
import fun.timu.train.business.service.TrainCarriageService;
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
 * @description 针对表【train_carriage(火车车厢)】的数据库操作Service实现
 * @createDate 2023-07-11 15:21:07
 */
@Service
public class TrainCarriageServiceImpl extends ServiceImpl<TrainCarriageMapper, TrainCarriage>
        implements TrainCarriageService {
    private static final Logger LOG = LoggerFactory.getLogger(TrainCarriageService.class);
    private final TrainCarriageMapper mapper;

    public TrainCarriageServiceImpl(TrainCarriageMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void save(TrainCarriageSaveVO saveVO) {
        DateTime now = DateTime.now();

        // 自动计算出列数和总座位数
        List<SeatColEnum> seatColEnums = SeatColEnum.getColsByType(saveVO.getSeatType());

        saveVO.setColCount(seatColEnums.size());
        saveVO.setSeatCount(saveVO.getColCount() * saveVO.getRowCount());

        TrainCarriage trainCarriage = BeanUtil.copyProperties(saveVO, TrainCarriage.class);

        if (ObjectUtil.isNull(trainCarriage.getId())) {
            TrainCarriage select = selectByUnique(saveVO.getTrainCode(), saveVO.getIndex());
            if (ObjectUtil.isNotEmpty(select)) {
                throw new BusinessException(BusinessExceptionCode.BUSINESS_TRAIN_CARRIAGE_INDEX_UNIQUE_ERROR);
            }

            trainCarriage.setId(SnowUtil.getSnowflakeNextId());
            trainCarriage.setCreateTime(now);
            trainCarriage.setUpdateTime(now);
            this.mapper.insert(trainCarriage);
        } else {
            trainCarriage.setUpdateTime(now);
            this.mapper.updateById(trainCarriage);
        }
    }

    private TrainCarriage selectByUnique(String trainCode, Integer index) {
        QueryWrapper<TrainCarriage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(ObjectUtil.isNotNull(trainCode), "train_code", trainCode);
        queryWrapper.eq(ObjectUtil.isNotNull(index), "index", index);

        List<TrainCarriage> list = this.mapper.selectList(queryWrapper);

        if (CollUtil.isNotEmpty(list)) {
            return list.get(0);
        } else {
            return null;
        }
    }

    @Override
    public PageResponse<TrainCarriageQueryResponse> queryList(TrainCarriageQueryVO queryVO) {
        QueryWrapper<TrainCarriage> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("train_code");
        queryWrapper.orderByAsc("index");

        queryWrapper.eq(ObjectUtil.isNotEmpty(queryVO.getTrainCode()), "train_code", queryVO.getTrainCode());

        PageHelper.startPage(queryVO.getPage(), queryVO.getSize());
        List<TrainCarriage> trainCarriageList = this.mapper.selectList(queryWrapper);

        PageInfo<TrainCarriage> pageInfo = new PageInfo<>(trainCarriageList);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());

        List<TrainCarriageQueryResponse> list = BeanUtil.copyToList(trainCarriageList, TrainCarriageQueryResponse.class);

        PageResponse<TrainCarriageQueryResponse> response = new PageResponse<>();
        response.setTotal(pageInfo.getTotal());
        response.setList(list);
        return response;
    }

    @Override
    public void delete(Long id) {
        this.mapper.deleteById(id);
    }

    @Override
    public List<TrainCarriage> selectByTrainCode(String trainCode) {
        QueryWrapper<TrainCarriage> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("index");
        queryWrapper.eq(ObjectUtil.isNotNull(trainCode), "train_code", trainCode);
        return this.mapper.selectList(queryWrapper);
    }
}




