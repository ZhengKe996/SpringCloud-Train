package fun.timu.train.business.controller.daily;

import fun.timu.train.business.request.train.DailyTrainStationQueryVO;
import fun.timu.train.business.request.train.DailyTrainStationSaveVO;
import fun.timu.train.business.response.daily.DailyTrainStationQueryResponse;
import fun.timu.train.business.service.DailyTrainStationService;
import fun.timu.train.commo.response.BaseResponse;
import fun.timu.train.commo.response.PageResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/daily-train-station")
public class DailyTrainStationAdminController {
    private final DailyTrainStationService service;

    public DailyTrainStationAdminController(DailyTrainStationService service) {
        this.service = service;
    }

    @PostMapping("/save")
    public BaseResponse save(@Valid @RequestBody DailyTrainStationSaveVO saveVO) {
        return new BaseResponse<>();
    }

    @GetMapping("/query-list")
    public BaseResponse<PageResponse<DailyTrainStationQueryResponse>> queryList(@Valid DailyTrainStationQueryVO queryVO) {
        return new BaseResponse<>();
    }

    @DeleteMapping("/delete/{id}")
    public BaseResponse delete(@PathVariable Long id) {
        return new BaseResponse<>();
    }

}
