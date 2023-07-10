package fun.timu.train.member.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.timu.train.commo.context.LoginMemberContext;
import fun.timu.train.commo.utils.SnowUtil;
import fun.timu.train.member.entity.Passenger;
import fun.timu.train.member.mapper.PassengerMapper;
import fun.timu.train.member.request.PassengerSaveVO;
import fun.timu.train.member.service.PassengerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author zhengke
 * @description 针对表【passenger(乘车人)】的数据库操作Service实现
 * @createDate 2023-07-09 21:05:19
 */
@Service
public class PassengerServiceImpl extends ServiceImpl<PassengerMapper, Passenger>
        implements PassengerService {
    private static final Logger LOG = LoggerFactory.getLogger(PassengerService.class);
    private final PassengerMapper mapper;

    public PassengerServiceImpl(PassengerMapper mapper) {
        this.mapper = mapper;
    }


    @Override
    public void save(PassengerSaveVO passengerSaveVO) {
        DateTime now = DateTime.now();
        Passenger passenger = BeanUtil.copyProperties(passengerSaveVO, Passenger.class);

        if (ObjectUtil.isNull(passenger.getId())) {
            passenger.setMemberId(LoginMemberContext.getId());
            passenger.setId(SnowUtil.getSnowflakeNextId());
            passenger.setCreateTime(now);
            passenger.setUpdateTime(now);
            mapper.insert(passenger);
        } else {
            passenger.setUpdateTime(now);
            mapper.updateById(passenger);
        }
    }
}




