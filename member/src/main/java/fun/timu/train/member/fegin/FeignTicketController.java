package fun.timu.train.member.fegin;

import fun.timu.train.commo.response.BaseResponse;
import fun.timu.train.member.service.TicketService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/feign/ticket")
public class FeignTicketController {

    private final TicketService service;

    public FeignTicketController(TicketService service) {
        this.service = service;
    }

    @PostMapping("/save")
    public BaseResponse save() {
        return new BaseResponse();
    }
}
