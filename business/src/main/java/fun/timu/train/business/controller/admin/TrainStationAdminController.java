package fun.timu.train.business.controller.admin;

import fun.timu.train.business.request.train.TrainStationQueryVO;
import fun.timu.train.business.request.train.TrainStationSaveVO;
import fun.timu.train.business.response.train.TrainStationQueryResponse;
import fun.timu.train.business.service.TrainStationService;
import fun.timu.train.commo.response.BaseResponse;
import fun.timu.train.commo.response.PageResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/train-station")
public class TrainStationAdminController {

    private final TrainStationService service;

    public TrainStationAdminController(TrainStationService service) {
        this.service = service;
    }

    @PostMapping("/save")
    public BaseResponse save(@Valid @RequestBody TrainStationSaveVO saveVO) {
        this.service.save(saveVO);
        return new BaseResponse<>();
    }

    @GetMapping("/query-list")
    public BaseResponse<PageResponse<TrainStationQueryResponse>> queryList(@Valid TrainStationQueryVO queryVO) {
        PageResponse<TrainStationQueryResponse> list = this.service.queryList(queryVO);
        return new BaseResponse<>(list);
    }

    @DeleteMapping("/delete/{id}")
    public BaseResponse delete(@PathVariable Long id) {
        this.service.delete(id);
        return new BaseResponse<>();
    }
}
