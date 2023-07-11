package fun.timu.train.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import fun.timu.train.business.entity.Station;
import fun.timu.train.business.request.station.StationQueryVO;
import fun.timu.train.business.request.station.StationSaveVO;
import fun.timu.train.business.response.StationQueryResponse;
import fun.timu.train.commo.response.PageResponse;

import java.util.List;

/**
 * @author zhengke
 * @description 针对表【station(车站)】的数据库操作Service
 * @createDate 2023-07-11 15:21:07
 */
public interface StationService extends IService<Station> {

    void save(StationSaveVO saveVO);

    PageResponse<StationQueryResponse> queryList(StationQueryVO queryVO);

    void delete(Long id);

    List<StationQueryResponse> queryAll();
}
