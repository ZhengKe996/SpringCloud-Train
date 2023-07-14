package fun.timu.train.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import fun.timu.train.business.entity.DailyTrainStation;
import fun.timu.train.business.request.train.DailyTrainStationQueryVO;
import fun.timu.train.business.request.train.DailyTrainStationSaveVO;
import fun.timu.train.business.response.daily.DailyTrainStationQueryResponse;
import fun.timu.train.commo.response.PageResponse;

import java.util.Date;

/**
 * @author zhengke
 * @description 针对表【daily_train_station(每日车站)】的数据库操作Service
 * @createDate 2023-07-11 15:21:07
 */
public interface DailyTrainStationService extends IService<DailyTrainStation> {
    void save(DailyTrainStationSaveVO req);

    PageResponse<DailyTrainStationQueryResponse> queryList(DailyTrainStationQueryVO queryVO);

    void delete(Long id);
    void genDaily(Date date, String trainCode);
}
