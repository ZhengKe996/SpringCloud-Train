package fun.timu.train.business.controller;

import fun.timu.train.business.response.StationQueryResponse;
import fun.timu.train.business.service.StationService;
import fun.timu.train.commo.response.BaseResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/station")
public class StationController {
    private final StationService service;

    public StationController(StationService service) {
        this.service = service;
    }

    @GetMapping("/query-all")
    public BaseResponse<List<StationQueryResponse>> queryList() {
        List<StationQueryResponse> list = this.service.queryAll();
        return new BaseResponse<>(list);
    }
}
