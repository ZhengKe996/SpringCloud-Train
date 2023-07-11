package fun.timu.train.business.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.timu.train.business.entity.Train;
import fun.timu.train.business.mapper.TrainMapper;
import fun.timu.train.business.service.TrainService;
import org.springframework.stereotype.Service;

/**
 * @author zhengke
 * @description 针对表【train(车次)】的数据库操作Service实现
 * @createDate 2023-07-11 15:21:07
 */
@Service
public class TrainServiceImpl extends ServiceImpl<TrainMapper, Train>
        implements TrainService {

}




