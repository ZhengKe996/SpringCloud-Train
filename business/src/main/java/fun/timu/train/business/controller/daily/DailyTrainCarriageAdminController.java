package fun.timu.train.business.controller.daily;

import fun.timu.train.business.request.daily.DailyTrainCarriageQueryVO;
import fun.timu.train.business.request.daily.DailyTrainCarriageSaveVO;
import fun.timu.train.business.response.daily.DailyTrainCarriageQueryResponse;
import fun.timu.train.business.service.DailyTrainCarriageService;
import fun.timu.train.commo.response.BaseResponse;
import fun.timu.train.commo.response.PageResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/daily-train-carriage")
public class DailyTrainCarriageAdminController {
    private final DailyTrainCarriageService service;

    public DailyTrainCarriageAdminController(DailyTrainCarriageService service) {
        this.service = service;
    }

    @PostMapping("/save")
    public BaseResponse save(@Valid @RequestBody DailyTrainCarriageSaveVO saveVO) {
        this.service.save(saveVO);
        return new BaseResponse<>();
    }

    @GetMapping("/query-list")
    public BaseResponse<PageResponse<DailyTrainCarriageQueryResponse>> queryList(@Valid DailyTrainCarriageQueryVO queryVO
    ) {
        PageResponse<DailyTrainCarriageQueryResponse> list = this.service.queryList(queryVO);
        return new BaseResponse<>(list);
    }

    @DeleteMapping("/delete/{id}")
    public BaseResponse delete(@PathVariable Long id) {
        this.service.delete(id);
        return new BaseResponse<>();
    }
}
