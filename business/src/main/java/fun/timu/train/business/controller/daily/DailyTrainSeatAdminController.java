package fun.timu.train.business.controller.daily;

import fun.timu.train.business.request.daily.DailyTrainSeatQueryVO;
import fun.timu.train.business.request.daily.DailyTrainSeatSaveVO;
import fun.timu.train.business.response.daily.DailyTrainSeatQueryResponse;
import fun.timu.train.business.service.DailyTrainSeatService;
import fun.timu.train.commo.response.BaseResponse;
import fun.timu.train.commo.response.PageResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/daily-train-seat")
public class DailyTrainSeatAdminController {
    private final DailyTrainSeatService service;

    public DailyTrainSeatAdminController(DailyTrainSeatService service) {
        this.service = service;
    }

    @PostMapping("/save")
    public BaseResponse save(@Valid @RequestBody DailyTrainSeatSaveVO saveVO) {
        return new BaseResponse<>();
    }

    @GetMapping("/query-list")
    public BaseResponse<PageResponse<DailyTrainSeatQueryResponse>> queryList(@Valid DailyTrainSeatQueryVO queryVO) {
        return new BaseResponse<>();
    }

    @DeleteMapping("/delete/{id}")
    public BaseResponse delete(@PathVariable Long id) {
        return new BaseResponse<>();
    }
}
