package fun.timu.train.member.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import fun.timu.train.member.entity.Ticket;
import org.springframework.stereotype.Repository;

/**
 * @author zhengke
 * @description 针对表【ticket(车票)】的数据库操作Mapper
 * @createDate 2023-07-15 19:57:05
 * @Entity .entity.fun.timu.train.member.entity.Ticket
 */
@Repository
public interface TicketMapper extends BaseMapper<Ticket> {

}




