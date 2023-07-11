package fun.timu.train.business.mapper;

import fun.timu.train.business.entity.Station;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * @author zhengke
 * @description 针对表【station(车站)】的数据库操作Mapper
 * @createDate 2023-07-11 15:21:07
 * @Entity .entity.Station
 */
@Repository
public interface StationMapper extends BaseMapper<Station> {

}




