package fun.timu.train.member.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.timu.train.member.entity.Member;
import fun.timu.train.member.mapper.MemberMapper;
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
    public long register(String mobile) {
        QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("mobile", mobile);
        List<Member> list = mapper.selectList(queryWrapper);

        // 手机号已被注册
        if (CollUtil.isNotEmpty(list)) {
//            return list.get(0).getId();
            throw new RuntimeException("手机号已注册");
        }

        Member member = new Member();
        member.setId(System.currentTimeMillis());
        member.setMobile(mobile);
        mapper.insert(member);
        return member.getId();
    }
}




