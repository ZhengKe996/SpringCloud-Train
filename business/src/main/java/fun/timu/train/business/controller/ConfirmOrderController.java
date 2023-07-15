package fun.timu.train.business.controller;

import fun.timu.train.business.request.confirm.ConfirmOrderDoVO;
import fun.timu.train.business.service.ConfirmOrderService;
import fun.timu.train.commo.response.BaseResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/confirm-order")
public class ConfirmOrderController {

    private final ConfirmOrderService service;

    public ConfirmOrderController(ConfirmOrderService confirmOrderService) {
        this.service = confirmOrderService;
    }

    @PostMapping("/do")
    public BaseResponse doConfirm(@Valid @RequestBody ConfirmOrderDoVO doVO) {
        this.service.doConfirm(doVO);
        return new BaseResponse<>();
    }
}
