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
import fun.timu.train.business.entity.DailyTrainCarriage;
import fun.timu.train.business.entity.TrainCarriage;
import fun.timu.train.business.enums.SeatColEnum;
import fun.timu.train.business.mapper.DailyTrainCarriageMapper;
import fun.timu.train.business.request.daily.DailyTrainCarriageQueryVO;
import fun.timu.train.business.request.daily.DailyTrainCarriageSaveVO;
import fun.timu.train.business.response.daily.DailyTrainCarriageQueryResponse;
import fun.timu.train.business.service.DailyTrainCarriageService;
import fun.timu.train.business.service.TrainCarriageService;
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
 * @description 针对表【daily_train_carriage(每日车厢)】的数据库操作Service实现
 * @createDate 2023-07-11 15:21:07
 */
@Service
public class DailyTrainCarriageServiceImpl extends ServiceImpl<DailyTrainCarriageMapper, DailyTrainCarriage>
        implements DailyTrainCarriageService {
    private static final Logger LOG = LoggerFactory.getLogger(DailyTrainCarriageService.class);
    private final DailyTrainCarriageMapper mapper;
    private final TrainCarriageService trainCarriageService;

    public DailyTrainCarriageServiceImpl(DailyTrainCarriageMapper mapper, TrainCarriageService trainCarriageService) {
        this.mapper = mapper;
        this.trainCarriageService = trainCarriageService;
    }

    @Override
    public void save(DailyTrainCarriageSaveVO saveVO) {
        DateTime now = DateTime.now();

        // 自动计算出列数和总座位数
        List<SeatColEnum> seatColEnums = SeatColEnum.getColsByType(saveVO.getSeatType());
        saveVO.setColCount(seatColEnums.size());
        saveVO.setSeatCount(saveVO.getColCount() * saveVO.getRowCount());

        DailyTrainCarriage dailyTrainCarriage = BeanUtil.copyProperties(saveVO, DailyTrainCarriage.class);

        if (ObjectUtil.isNull(dailyTrainCarriage.getId())) {
            dailyTrainCarriage.setId(SnowUtil.getSnowflakeNextId());
            dailyTrainCarriage.setCreateTime(now);
            dailyTrainCarriage.setUpdateTime(now);
            this.mapper.insert(dailyTrainCarriage);
        } else {
            dailyTrainCarriage.setUpdateTime(now);
            this.mapper.updateById(dailyTrainCarriage);
        }

    }

    @Override
    public PageResponse<DailyTrainCarriageQueryResponse> queryList(DailyTrainCarriageQueryVO queryVO) {
        QueryWrapper<DailyTrainCarriage> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("date");
        queryWrapper.orderByAsc("`train_code`");
        queryWrapper.orderByAsc("`index`");
        queryWrapper.eq(ObjUtil.isNotNull(queryVO.getDate()), "date", queryVO.getDate());
        queryWrapper.eq(ObjUtil.isNotNull(queryVO.getTrainCode()), "train_code", queryVO.getTrainCode());

        PageHelper.startPage(queryVO.getPage(), queryVO.getSize());
        List<DailyTrainCarriage> trainCarriages = this.mapper.selectList(queryWrapper);
        PageInfo<DailyTrainCarriage> pageInfo = new PageInfo<>(trainCarriages);
        LOG.info("本次查询总行数：{}", pageInfo.getTotal());
        LOG.info("本次查询总页数：{}", pageInfo.getPages());

        List<DailyTrainCarriageQueryResponse> list = BeanUtil.copyToList(trainCarriages, DailyTrainCarriageQueryResponse.class);
        PageResponse<DailyTrainCarriageQueryResponse> response = new PageResponse<>();
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
    public void genDaily(Date date, String trainCode) {
        LOG.info("生成日期【{}】车次【{}】的车厢信息开始", DateUtil.formatDate(date), trainCode);
        // 删除某日某车次的车厢信息
        QueryWrapper<DailyTrainCarriage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("date", date);
        queryWrapper.eq("train_code", trainCode);
        this.mapper.delete(queryWrapper);

        // 查出某车次的所有的车厢信息
        List<TrainCarriage> carriageList = trainCarriageService.selectByTrainCode(trainCode);
        if (CollUtil.isEmpty(carriageList)) {
            LOG.info("该车次没有车厢基础数据，生成该车次的车厢信息结束");
            return;
        }

        // 生成数据
        for (TrainCarriage trainCarriage : carriageList) {
            DateTime now = DateTime.now();
            DailyTrainCarriage dailyTrainCarriage = BeanUtil.copyProperties(trainCarriage, DailyTrainCarriage.class);
            dailyTrainCarriage.setId(SnowUtil.getSnowflakeNextId());
            dailyTrainCarriage.setCreateTime(now);
            dailyTrainCarriage.setUpdateTime(now);
            dailyTrainCarriage.setDate(date);
            this.mapper.insert(dailyTrainCarriage);
        }
        LOG.info("生成日期【{}】车次【{}】的车厢信息结束", DateUtil.formatDate(date), trainCode);
    }
}




