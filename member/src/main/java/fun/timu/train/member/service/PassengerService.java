package fun.timu.train.member.service;

import fun.timu.train.commo.response.PageResponse;
import fun.timu.train.member.entity.Passenger;
import com.baomidou.mybatisplus.extension.service.IService;
import fun.timu.train.member.request.passenger.PassengerQueryVO;
import fun.timu.train.member.request.passenger.PassengerSaveVO;
import fun.timu.train.member.response.PassengerQueryResponse;

/**
 * @author zhengke
 * @description 针对表【passenger(乘车人)】的数据库操作Service
 * @createDate 2023-07-09 21:05:20
 */
public interface PassengerService extends IService<Passenger> {
    void save(PassengerSaveVO passengerSaveVO);

    PageResponse<PassengerQueryResponse> queryList(PassengerQueryVO queryVO);
     void delete(Long id);
}
