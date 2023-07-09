package fun.timu.train.member.controller;

import fun.timu.train.commo.response.BaseResponse;
import fun.timu.train.member.request.MemberRegisterRequest;
import fun.timu.train.member.service.MemberService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/count")
    public BaseResponse count() {
        long count = this.memberService.count();
        return new BaseResponse(count);
    }

    @PostMapping("/register")
    public BaseResponse register(@RequestBody MemberRegisterRequest mobile) {
        long id = this.memberService.register(mobile);

        return new BaseResponse<>(id);
    }
}
