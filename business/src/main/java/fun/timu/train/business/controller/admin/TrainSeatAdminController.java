package fun.timu.train.business.controller.admin;

import fun.timu.train.business.request.train.TrainSeatQueryVO;
import fun.timu.train.business.request.train.TrainSeatSaveVO;
import fun.timu.train.business.response.train.TrainSeatQueryResponse;
import fun.timu.train.business.service.TrainSeatService;
import fun.timu.train.commo.response.BaseResponse;
import fun.timu.train.commo.response.PageResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/train-seat")
public class TrainSeatAdminController {
    private final TrainSeatService service;

    public TrainSeatAdminController(TrainSeatService trainSeatService) {
        this.service = trainSeatService;
    }

    @PostMapping("/save")
    public BaseResponse save(@Valid @RequestBody TrainSeatSaveVO saveVO) {
        this.service.save(saveVO);
        return new BaseResponse<>();
    }

    @GetMapping("/query-list")
    public BaseResponse<PageResponse<TrainSeatQueryResponse>> queryList(@Valid TrainSeatQueryVO queryVO) {
        PageResponse<TrainSeatQueryResponse> list = this.service.queryList(queryVO);
        return new BaseResponse<>(list);
    }

    @DeleteMapping("/delete/{id}")
    public BaseResponse<Object> delete(@PathVariable Long id) {
        this.service.delete(id);
        return new BaseResponse<>();
    }

}
