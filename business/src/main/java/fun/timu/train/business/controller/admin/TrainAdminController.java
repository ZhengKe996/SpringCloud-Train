package fun.timu.train.business.controller.admin;

import fun.timu.train.business.request.train.TrainQueryVO;
import fun.timu.train.business.request.train.TrainSaveVO;
import fun.timu.train.business.response.TrainQueryResponse;
import fun.timu.train.business.service.TrainService;
import fun.timu.train.commo.response.BaseResponse;
import fun.timu.train.commo.response.PageResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/train")
public class TrainAdminController {
    private final TrainService service;

    public TrainAdminController(TrainService service) {
        this.service = service;
    }

    @PostMapping("/save")
    public BaseResponse save(@Valid @RequestBody TrainSaveVO saveVO) {
        this.service.save(saveVO);
        return new BaseResponse<>();
    }

    @GetMapping("/query-all")
    public BaseResponse<List<TrainQueryResponse>> queryList() {
        List<TrainQueryResponse> list = this.service.queryAll();
        return new BaseResponse<>(list);
    }

    @GetMapping("/query-list")
    public BaseResponse<PageResponse<TrainQueryResponse>> queryList(@Valid TrainQueryVO queryVO) {
        PageResponse<TrainQueryResponse> list = this.service.queryList(queryVO);
        return new BaseResponse<>(list);
    }

    @DeleteMapping("/delete/{id}")
    public BaseResponse delete(@PathVariable Long id) {
        this.service.delete(id);
        return new BaseResponse<>();
    }

    @GetMapping("/gen-seat/{trainCode}")
    public BaseResponse genSeat(@PathVariable String trainCode) {

        return new BaseResponse<>();
    }
}
