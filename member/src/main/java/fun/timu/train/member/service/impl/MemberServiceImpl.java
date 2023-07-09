package fun.timu.train.member.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.timu.train.member.entity.Member;
import fun.timu.train.member.mapper.MemberMapper;
import fun.timu.train.member.service.MemberService;
import org.springframework.stereotype.Service;

/**
 * @author zhengke
 * @description 针对表【member(会员)】的数据库操作Service实现
 * @createDate 2023-07-09 21:05:19
 */
@Service
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member>
        implements MemberService {
    private final MemberMapper memberMapper;


    public MemberServiceImpl(MemberMapper mapper) {
        this.memberMapper = mapper;
    }


    @Override
    public long count() {
        return memberMapper.selectCount(null);
    }
}




