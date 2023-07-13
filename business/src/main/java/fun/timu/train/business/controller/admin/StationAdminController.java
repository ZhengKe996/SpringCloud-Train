package fun.timu.train.business.controller.admin;

import fun.timu.train.business.request.station.StationQueryVO;
import fun.timu.train.business.request.station.StationSaveVO;
import fun.timu.train.business.response.StationQueryResponse;
import fun.timu.train.business.service.StationService;
import fun.timu.train.commo.response.BaseResponse;
import fun.timu.train.commo.response.PageResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/station")
public class StationAdminController {

    private final StationService service;

    public StationAdminController(StationService service) {
        this.service = service;
    }

    @PostMapping("/save")
    public BaseResponse save(@Valid @RequestBody StationSaveVO saveVO) {
        this.service.save(saveVO);
        return new BaseResponse();
    }

    @GetMapping("/query-list")
    public BaseResponse<PageResponse<StationQueryResponse>> queryList(@Valid StationQueryVO queryVO) {
        PageResponse<StationQueryResponse> list = this.service.queryList(queryVO);
        return new BaseResponse<>(list);
    }

    @DeleteMapping("/delete/{id}")
    public BaseResponse delete(@PathVariable Long id) {
        this.service.delete(id);
        return new BaseResponse<>();
    }

    @GetMapping("/query-all")
    public BaseResponse<List<StationQueryResponse>> queryList() {
        List<StationQueryResponse> list = this.service.queryAll();
        return new BaseResponse<>(list);
    }

    @PostMapping("/save-list")
    public BaseResponse saveAll(@Valid @RequestBody List<StationSaveVO> list) {
        for (StationSaveVO info : list) {
            this.service.save(info);
        }
        return new BaseResponse();
    }

}
