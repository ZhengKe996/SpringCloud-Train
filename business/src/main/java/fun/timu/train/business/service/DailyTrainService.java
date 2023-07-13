package fun.timu.train.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import fun.timu.train.business.entity.DailyTrain;
import fun.timu.train.business.entity.Train;
import fun.timu.train.business.request.daily.DailyTrainQueryVO;
import fun.timu.train.business.request.daily.DailyTrainSaveVO;
import fun.timu.train.business.response.daily.DailyTrainQueryResponse;
import fun.timu.train.commo.response.PageResponse;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import java.util.Date;
import java.util.List;

/**
 * @author zhengke
 * @description 针对表【daily_train(每日车次)】的数据库操作Service
 * @createDate 2023-07-11 15:21:07
 */
public interface DailyTrainService extends IService<DailyTrain> {
    void save(DailyTrainSaveVO saveVO);

    PageResponse<DailyTrainQueryResponse> queryList(DailyTrainQueryVO queryVO);

    void delete(Long id);

    void genDaily(Date date);
    void genDailyTrain(Date date, Train train);
}
