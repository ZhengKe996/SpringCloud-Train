package fun.timu.train.member.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import fun.timu.train.commo.request.TicketSaveVO;
import fun.timu.train.commo.utils.SnowUtil;
import fun.timu.train.member.entity.Ticket;
import fun.timu.train.member.mapper.TicketMapper;
import fun.timu.train.member.service.TicketService;
import org.springframework.stereotype.Service;

/**
 * @author zhengke
 * @description 针对表【ticket(车票)】的数据库操作Service实现
 * @createDate 2023-07-15 19:57:05
 */
@Service
public class TicketServiceImpl extends ServiceImpl<TicketMapper, Ticket>
        implements TicketService {

    private final TicketMapper mapper;

    public TicketServiceImpl(TicketMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void save(TicketSaveVO saveVO) {
        DateTime now = DateTime.now();
        Ticket ticket = BeanUtil.copyProperties(saveVO, Ticket.class);
        ticket.setId(SnowUtil.getSnowflakeNextId());
        ticket.setCreateTime(now);
        ticket.setUpdateTime(now);
        this.mapper.insert(ticket);
    }
}




