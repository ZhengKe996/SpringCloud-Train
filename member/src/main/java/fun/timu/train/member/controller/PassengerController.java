package fun.timu.train.member.controller;

import fun.timu.train.commo.response.BaseResponse;
import fun.timu.train.member.request.PassengerSaveVO;
import fun.timu.train.member.service.PassengerService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


}
