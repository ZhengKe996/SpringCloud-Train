package fun.timu.train.member.controller;

import fun.timu.train.member.service.MemberService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/count")
    public long count() {
        return this.memberService.count();
    }

    @PostMapping("/register")
    public long register(String mobile) {
        return this.memberService.register(mobile);
    }
}
