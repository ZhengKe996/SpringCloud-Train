package fun.timu.train.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import fun.timu.train.business.entity.TrainSeat;
import fun.timu.train.business.request.train.TrainSeatQueryVO;
import fun.timu.train.business.request.train.TrainSeatSaveVO;
import fun.timu.train.business.response.train.TrainSeatQueryResponse;
import fun.timu.train.commo.response.PageResponse;

import java.util.List;

/**
 * @author zhengke
 * @description 针对表【train_seat(座位)】的数据库操作Service
 * @createDate 2023-07-11 15:21:07
 */
public interface TrainSeatService extends IService<TrainSeat> {
    void save(TrainSeatSaveVO saveVO);

    PageResponse<TrainSeatQueryResponse> queryList(TrainSeatQueryVO queryVO);

    void delete(Long id);

    void genTrainSeat(String trainCode);

    List<TrainSeat> selectByTrainCode(String trainCode);

}
