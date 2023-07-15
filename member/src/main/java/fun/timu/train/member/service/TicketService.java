package fun.timu.train.member.service;


import com.baomidou.mybatisplus.extension.service.IService;
import fun.timu.train.commo.request.TicketSaveVO;
import fun.timu.train.member.entity.Ticket;

/**
 * @author zhengke
 * @description 针对表【ticket(车票)】的数据库操作Service
 * @createDate 2023-07-15 19:57:05
 */
public interface TicketService extends IService<Ticket> {
    void save(TicketSaveVO saveVO);

}
