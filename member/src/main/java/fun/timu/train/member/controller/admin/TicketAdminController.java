package fun.timu.train.member.controller.admin;

import fun.timu.train.commo.response.BaseResponse;
import fun.timu.train.member.service.TicketService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/ticket")
public class TicketAdminController {
    private final TicketService ticketService;


    public TicketAdminController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping("/query-list")
    public BaseResponse queryList() {
        return new BaseResponse();
    }
}
