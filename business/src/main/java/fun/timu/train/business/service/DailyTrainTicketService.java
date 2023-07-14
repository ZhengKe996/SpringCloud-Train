package fun.timu.train.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import fun.timu.train.business.entity.DailyTrain;
import fun.timu.train.business.entity.DailyTrainTicket;
import fun.timu.train.business.request.daily.DailyTrainTicketQueryVO;
import fun.timu.train.business.request.daily.DailyTrainTicketSaveVO;
import fun.timu.train.business.response.daily.DailyTrainTicketQueryResponse;
import fun.timu.train.commo.response.PageResponse;

import java.util.Date;

/**
 * @author zhengke
 * @description 针对表【daily_train_ticket(余票信息)】的数据库操作Service
 * @createDate 2023-07-11 15:21:07
 */
public interface DailyTrainTicketService extends IService<DailyTrainTicket> {
    void save(DailyTrainTicketSaveVO saveVO);

    PageResponse<DailyTrainTicketQueryResponse> queryList(DailyTrainTicketQueryVO queryVO);

    void delete(Long id);

    void genDaily(DailyTrain dailyTrain, Date date, String trainCode);
}
