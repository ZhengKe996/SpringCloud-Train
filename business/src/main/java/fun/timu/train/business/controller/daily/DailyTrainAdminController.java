package fun.timu.train.business.controller.daily;

import fun.timu.train.business.request.daily.DailyTrainQueryVO;
import fun.timu.train.business.request.daily.DailyTrainSaveVO;
import fun.timu.train.business.response.daily.DailyTrainQueryResponse;
import fun.timu.train.business.service.DailyTrainService;
import fun.timu.train.commo.response.BaseResponse;
import fun.timu.train.commo.response.PageResponse;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/admin/daily-train")
public class DailyTrainAdminController {
    private final DailyTrainService dailyTrainService;

    public DailyTrainAdminController(DailyTrainService dailyTrainService) {
        this.dailyTrainService = dailyTrainService;
    }

    @PostMapping("/save")
    public BaseResponse save(@Valid @RequestBody DailyTrainSaveVO saveVO) {
        dailyTrainService.save(saveVO);
        return new BaseResponse();
    }

    @GetMapping("/query-list")
    public BaseResponse<PageResponse<DailyTrainQueryResponse>> queryList(@Valid DailyTrainQueryVO req) {
        PageResponse<DailyTrainQueryResponse> list = dailyTrainService.queryList(req);
        return new BaseResponse<>(list);
    }

    @DeleteMapping("/delete/{id}")
    public BaseResponse delete(@PathVariable Long id) {
        dailyTrainService.delete(id);
        return new BaseResponse<>();
    }

    @GetMapping("/gen-daily/{date}")
    public BaseResponse genDaily(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        dailyTrainService.genDaily(date);
        return new BaseResponse<>();
    }

}
