package fun.timu.train.member.controller;

import fun.timu.train.commo.response.BaseResponse;
import fun.timu.train.member.request.MemberLoginRequest;
import fun.timu.train.member.request.MemberRegisterRequest;
import fun.timu.train.member.request.MemberSendCodeRequest;
import fun.timu.train.member.response.MemberLoginResponse;
import fun.timu.train.member.service.MemberService;
import jakarta.validation.Valid;
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
    public BaseResponse register(@Valid @RequestBody MemberRegisterRequest mobile) {
        long id = this.memberService.register(mobile);

        return new BaseResponse<>(id);
    }

    @PostMapping("/send-code")
    public BaseResponse sendCode(@Valid @RequestBody MemberSendCodeRequest request) {
        this.memberService.sendCode(request);
        return new BaseResponse();
    }

    @PostMapping("/login")
    public BaseResponse<MemberLoginResponse> login(@Valid @RequestBody MemberLoginRequest request) {
        MemberLoginResponse response = memberService.login(request);
        return new BaseResponse<>(response);
    }
}
