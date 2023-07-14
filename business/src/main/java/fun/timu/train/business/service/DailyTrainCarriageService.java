package fun.timu.train.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import fun.timu.train.business.entity.DailyTrainCarriage;
import fun.timu.train.business.request.daily.DailyTrainCarriageQueryVO;
import fun.timu.train.business.request.daily.DailyTrainCarriageSaveVO;
import fun.timu.train.business.response.daily.DailyTrainCarriageQueryResponse;
import fun.timu.train.commo.response.PageResponse;

import java.util.Date;

/**
 * @author zhengke
 * @description 针对表【daily_train_carriage(每日车厢)】的数据库操作Service
 * @createDate 2023-07-11 15:21:07
 */
public interface DailyTrainCarriageService extends IService<DailyTrainCarriage> {
    void save(DailyTrainCarriageSaveVO saveVO);

    PageResponse<DailyTrainCarriageQueryResponse> queryList(DailyTrainCarriageQueryVO queryVO);

    void delete(Long id);

    void genDaily(Date date, String trainCode);
}
