package fun.timu.train.member.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.timu.train.commo.exception.BusinessException;
import fun.timu.train.commo.exception.BusinessExceptionCode;
import fun.timu.train.member.entity.Member;
import fun.timu.train.member.mapper.MemberMapper;
import fun.timu.train.member.request.MemberRegisterRequest;
import fun.timu.train.member.service.MemberService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zhengke
 * @description 针对表【member(会员)】的数据库操作Service实现
 * @createDate 2023-07-09 21:05:19
 */
@Service
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member>
        implements MemberService {
    private final MemberMapper mapper;


    public MemberServiceImpl(MemberMapper mapper) {
        this.mapper = mapper;
    }


    @Override
    public long count() {
        return mapper.selectCount(null);
    }

    @Override
    public long register(MemberRegisterRequest mobile) {
        QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("mobile", mobile);
        List<Member> list = mapper.selectList(queryWrapper);
        // 手机号已被注册
        if (CollUtil.isEmpty(list)) {
            throw new BusinessException(BusinessExceptionCode.MEMBER_MOBILE_EXIST);
        }

        Member member = new Member();
        member.setId(System.currentTimeMillis());
        member.setMobile(mobile.getMobile());
        mapper.insert(member);
        return member.getId();
    }
}



