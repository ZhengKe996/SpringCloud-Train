package fun.timu.train.business.controller.daily;

import fun.timu.train.business.request.daily.DailyTrainTicketQueryVO;
import fun.timu.train.business.request.daily.DailyTrainTicketSaveVO;
import fun.timu.train.business.response.daily.DailyTrainTicketQueryResponse;
import fun.timu.train.business.service.DailyTrainTicketService;
import fun.timu.train.commo.response.BaseResponse;
import fun.timu.train.commo.response.PageResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/daily-train-ticket")
public class DailyTrainTicketAdminController {
    private final DailyTrainTicketService service;

    public DailyTrainTicketAdminController(DailyTrainTicketService service) {
        this.service = service;
    }

    @PostMapping("/save")
    public BaseResponse save(@Valid @RequestBody DailyTrainTicketSaveVO saveVO) {

        return new BaseResponse<>();
    }

    @GetMapping("/query-list")
    public BaseResponse<PageResponse<DailyTrainTicketQueryResponse>> queryList(@Valid DailyTrainTicketQueryVO queryVO) {
        return new BaseResponse<>();
    }

    @DeleteMapping("/delete/{id}")
    public BaseResponse delete(@PathVariable Long id) {
        return new BaseResponse<>();
    }
}
