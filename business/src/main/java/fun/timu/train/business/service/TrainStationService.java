package fun.timu.train.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import fun.timu.train.business.entity.TrainStation;
import fun.timu.train.business.request.train.TrainStationQueryVO;
import fun.timu.train.business.request.train.TrainStationSaveVO;
import fun.timu.train.business.response.train.TrainStationQueryResponse;
import fun.timu.train.commo.response.PageResponse;

import java.util.List;

/**
 * @author zhengke
 * @description 针对表【train_station(火车车站)】的数据库操作Service
 * @createDate 2023-07-11 15:21:07
 */
public interface TrainStationService extends IService<TrainStation> {
    void save(TrainStationSaveVO saveVO);

    PageResponse<TrainStationQueryResponse> queryList(TrainStationQueryVO queryVO);

    void delete(Long id);

    List<TrainStation> selectByTrainCode(String trainCode);
}
