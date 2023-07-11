package fun.timu.train.member.controller;

import fun.timu.train.commo.context.LoginMemberContext;
import fun.timu.train.commo.response.BaseResponse;
import fun.timu.train.commo.response.PageResponse;
import fun.timu.train.member.request.passenger.PassengerQueryVO;
import fun.timu.train.member.request.passenger.PassengerSaveVO;
import fun.timu.train.member.response.PassengerQueryResponse;
import fun.timu.train.member.service.PassengerService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/passenger")
public class PassengerController {

    private final PassengerService service;

    public PassengerController(PassengerService service) {
        this.service = service;
    }

    @PostMapping("/save")
    public BaseResponse save(@Valid @RequestBody PassengerSaveVO passengerSaveVO) {
        service.save(passengerSaveVO);
        return new BaseResponse<>();
    }

    @GetMapping("/query-list")
    public BaseResponse<PageResponse<PassengerQueryResponse>> queryList(@Valid PassengerQueryVO queryVO) {
        queryVO.setMemberId(LoginMemberContext.getId());
        PageResponse<PassengerQueryResponse> list = this.service.queryList(queryVO);
        return new BaseResponse<>(list);
    }

    @DeleteMapping("/delete/{id}")
    public BaseResponse delete(@PathVariable Long id) {
        this.service.delete(id);
        return new BaseResponse<>();
    }


}
