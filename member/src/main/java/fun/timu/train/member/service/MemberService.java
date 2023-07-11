package fun.timu.train.member.service;

import fun.timu.train.member.entity.Member;
import com.baomidou.mybatisplus.extension.service.IService;
import fun.timu.train.member.request.member.MemberLoginVO;
import fun.timu.train.member.request.member.MemberRegisterVO;
import fun.timu.train.member.request.member.MemberSendCodeVO;
import fun.timu.train.member.response.MemberLoginResponse;

/**
 * @author zhengke
 * @description 针对表【member(会员)】的数据库操作Service
 * @createDate 2023-07-09 21:05:19
 */
public interface MemberService extends IService<Member> {
    long count();

    long register(MemberRegisterVO mobile);

    void sendCode(MemberSendCodeVO request);

    MemberLoginResponse login(MemberLoginVO request);
}
