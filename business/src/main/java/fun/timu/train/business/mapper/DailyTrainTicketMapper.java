package fun.timu.train.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import fun.timu.train.business.entity.DailyTrainTicket;
import org.springframework.stereotype.Repository;

/**
 * @author zhengke
 * @description 针对表【daily_train_ticket(余票信息)】的数据库操作Mapper
 * @createDate 2023-07-11 15:21:07
 * @Entity .entity.DailyTrainTicket
 */
@Repository
public interface DailyTrainTicketMapper extends BaseMapper<DailyTrainTicket> {

}




