package fun.timu.train.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import fun.timu.train.business.entity.DailyTrainSeat;
import fun.timu.train.business.request.daily.DailyTrainSeatQueryVO;
import fun.timu.train.business.request.daily.DailyTrainSeatSaveVO;
import fun.timu.train.business.response.daily.DailyTrainSeatQueryResponse;
import fun.timu.train.commo.response.PageResponse;

import java.util.Date;

/**
 * @author zhengke
 * @description 针对表【daily_train_seat(每日座位)】的数据库操作Service
 * @createDate 2023-07-11 15:21:07
 */
public interface DailyTrainSeatService extends IService<DailyTrainSeat> {
    void save(DailyTrainSeatSaveVO saveVO);

    PageResponse<DailyTrainSeatQueryResponse> queryList(DailyTrainSeatQueryVO queryVO);

    void delete(Long id);

    void genDaily(Date date, String trainCode);

    int countSeat(Date date, String trainCode, String seatType);
}
