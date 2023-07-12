package fun.timu.train.business.controller.admin;

import fun.timu.train.business.request.train.TrainCarriageQueryVO;
import fun.timu.train.business.request.train.TrainCarriageSaveVO;
import fun.timu.train.business.response.train.TrainCarriageQueryResponse;
import fun.timu.train.business.service.TrainCarriageService;
import fun.timu.train.commo.response.BaseResponse;
import fun.timu.train.commo.response.PageResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/train-carriage")
public class TrainCarriageAdminController {
    private final TrainCarriageService service;

    public TrainCarriageAdminController(TrainCarriageService service) {
        this.service = service;
    }

    @PostMapping("/save")
    public BaseResponse save(@Valid @RequestBody TrainCarriageSaveVO saveVO) {
        this.service.save(saveVO);
        return new BaseResponse<>();
    }

    @GetMapping("/query-list")
    public BaseResponse<PageResponse<TrainCarriageQueryResponse>> queryList(@Valid TrainCarriageQueryVO queryVO) {
        PageResponse<TrainCarriageQueryResponse> list = this.service.queryList(queryVO);
        return new BaseResponse<>(list);
    }

    @DeleteMapping("/delete/{id}")
    public BaseResponse delete(@PathVariable Long id) {
        this.service.delete(id);
        return new BaseResponse<>();
    }
}
