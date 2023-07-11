package fun.timu.train.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import fun.timu.train.business.entity.Train;
import fun.timu.train.business.request.train.TrainQueryVO;
import fun.timu.train.business.request.train.TrainSaveVO;
import fun.timu.train.business.response.TrainQueryResponse;
import fun.timu.train.commo.response.PageResponse;

import java.util.List;

/**
 * @author zhengke
 * @description 针对表【train(车次)】的数据库操作Service
 * @createDate 2023-07-11 15:21:07
 */
public interface TrainService extends IService<Train> {
    void save(TrainSaveVO saveVO);

    PageResponse<TrainQueryResponse> queryList(TrainQueryVO queryVO);

    void delete(Long id);

    List<TrainQueryResponse> queryAll();

    List<Train> selectAll();

}
