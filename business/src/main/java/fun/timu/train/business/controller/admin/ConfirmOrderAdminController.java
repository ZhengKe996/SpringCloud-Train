package fun.timu.train.business.controller.admin;

import fun.timu.train.business.request.confirm.ConfirmOrderDoVO;
import fun.timu.train.business.request.confirm.ConfirmOrderQueryVO;
import fun.timu.train.business.response.ConfirmOrderQueryResponse;
import fun.timu.train.business.service.ConfirmOrderService;
import fun.timu.train.commo.response.BaseResponse;
import fun.timu.train.commo.response.PageResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/confirm-order")
public class ConfirmOrderAdminController {
    private final ConfirmOrderService service;

    public ConfirmOrderAdminController(ConfirmOrderService service) {
        this.service = service;
    }


    @PostMapping("/save")
    public BaseResponse save(@Valid @RequestBody ConfirmOrderDoVO doVO) {
        this.service.save(doVO);
        return new BaseResponse<>();
    }

    @GetMapping("/query-list")
    public BaseResponse<PageResponse<ConfirmOrderQueryResponse>> queryList(@Valid ConfirmOrderQueryVO queryVO) {
        PageResponse<ConfirmOrderQueryResponse> list = this.service.queryList(queryVO);
        return new BaseResponse<>(list);
    }

    @DeleteMapping("/delete/{id}")
    public BaseResponse<Object> delete(@PathVariable Long id) {
        this.service.delete(id);
        return new BaseResponse<>();
    }
}
