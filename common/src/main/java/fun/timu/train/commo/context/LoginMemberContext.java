package fun.timu.train.commo.context;

import fun.timu.train.commo.response.MemberLoginResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @LoginMemberContext 线程本地变量
 */
public class LoginMemberContext {
    private static final Logger LOG = LoggerFactory.getLogger(LoginMemberContext.class);

    private static ThreadLocal<MemberLoginResponse> member = new ThreadLocal<>();

    public static MemberLoginResponse getMember() {
        return member.get();
    }

    public static void setMember(MemberLoginResponse member) {
        LoginMemberContext.member.set(member);
    }

    public static Long getId() {
        try {
            return member.get().getId();
        } catch (Exception e) {
            LOG.error("获取登录会员信息异常", e);
            throw e;
        }
    }
}
