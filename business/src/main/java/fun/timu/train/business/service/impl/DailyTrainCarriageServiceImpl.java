package fun.timu.train.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import fun.timu.train.business.entity.DailyTrainCarriage;
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
    public void genDaily(Date date, String trainCode) {

    }
}




