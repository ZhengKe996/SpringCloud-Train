package fun.timu.train.business.controller;

import fun.timu.train.business.response.train.TrainQueryResponse;
import fun.timu.train.business.service.TrainService;
import fun.timu.train.commo.response.BaseResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/train")
public class TrainController {
    private final TrainService service;

    public TrainController(TrainService service) {
        this.service = service;
    }

    @GetMapping("/query-all")
    public BaseResponse<List<TrainQueryResponse>> queryList() {
        List<TrainQueryResponse> list = this.service.queryAll();

        return new BaseResponse<>(list);
    }
}
