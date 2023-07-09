package fun.timu.train.member.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.timu.train.member.entity.Passenger;
import fun.timu.train.member.mapper.PassengerMapper;
import fun.timu.train.member.service.PassengerService;
import org.springframework.stereotype.Service;

/**
 * @author zhengke
 * @description 针对表【passenger(乘车人)】的数据库操作Service实现
 * @createDate 2023-07-09 21:05:19
 */
@Service
public class PassengerServiceImpl extends ServiceImpl<PassengerMapper, Passenger>
        implements PassengerService {

}




