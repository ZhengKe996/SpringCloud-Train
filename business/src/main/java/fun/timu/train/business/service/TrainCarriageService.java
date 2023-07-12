package fun.timu.train.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import fun.timu.train.business.entity.TrainCarriage;
import fun.timu.train.business.request.train.TrainCarriageQueryVO;
import fun.timu.train.business.request.train.TrainCarriageSaveVO;
import fun.timu.train.business.response.train.TrainCarriageQueryResponse;
import fun.timu.train.commo.response.PageResponse;

import java.util.List;

/**
 * @author zhengke
 * @description 针对表【train_carriage(火车车厢)】的数据库操作Service
 * @createDate 2023-07-11 15:21:07
 */
public interface TrainCarriageService extends IService<TrainCarriage> {
    void save(TrainCarriageSaveVO saveVO);

    PageResponse<TrainCarriageQueryResponse> queryList(TrainCarriageQueryVO queryVO);

    void delete(Long id);

    List<TrainCarriage> selectByTrainCode(String trainCode);

}
