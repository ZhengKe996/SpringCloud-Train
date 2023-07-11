package fun.timu.train.business.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.timu.train.business.entity.Station;
import fun.timu.train.business.mapper.StationMapper;
import fun.timu.train.business.service.StationService;
import org.springframework.stereotype.Service;

/**
 * @author zhengke
 * @description 针对表【station(车站)】的数据库操作Service实现
 * @createDate 2023-07-11 15:21:07
 */
@Service
public class StationServiceImpl extends ServiceImpl<StationMapper, Station>
        implements StationService {

}




