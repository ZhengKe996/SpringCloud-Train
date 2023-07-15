package fun.timu.train.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import fun.timu.train.business.entity.ConfirmOrder;
import fun.timu.train.business.request.confirm.ConfirmOrderDoVO;
import fun.timu.train.business.request.confirm.ConfirmOrderQueryVO;
import fun.timu.train.business.response.ConfirmOrderQueryResponse;
import fun.timu.train.commo.response.PageResponse;

/**
 * @author zhengke
 * @description 针对表【confirm_order(确认订单)】的数据库操作Service
 * @createDate 2023-07-11 15:21:07
 */
public interface ConfirmOrderService extends IService<ConfirmOrder> {
    void save(ConfirmOrderDoVO doVO);
    PageResponse<ConfirmOrderQueryResponse> queryList(ConfirmOrderQueryVO queryVO);
    void delete(Long id);
    void doConfirm(ConfirmOrderDoVO req);
}
