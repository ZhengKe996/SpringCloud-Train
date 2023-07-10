package fun.timu.train.member.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.timu.train.commo.exception.BusinessException;
import fun.timu.train.commo.exception.BusinessExceptionCode;
import fun.timu.train.commo.utils.SnowUtil;
import fun.timu.train.member.entity.Member;
import fun.timu.train.member.mapper.MemberMapper;
import fun.timu.train.member.request.MemberRegisterRequest;
import fun.timu.train.member.request.MemberSendCodeRequest;
import fun.timu.train.member.service.MemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger LOG = LoggerFactory.getLogger(MemberService.class);

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
        Member select = this.selectByMobile(mobile.getMobile());
        // 手机号已被注册
        if (ObjectUtil.isNotNull(select)) {
            throw new BusinessException(BusinessExceptionCode.MEMBER_MOBILE_EXIST);
        }

        Member member = new Member();
        member.setId(IdUtil.getSnowflakeNextId());
        member.setMobile(mobile.getMobile());
        mapper.insert(member);
        return member.getId();
    }

    @Override
    public void sendCode(MemberSendCodeRequest request) {
        String mobile = request.getMobile();
        Member select = this.selectByMobile(mobile);
        if (ObjectUtil.isNull(select)) {
            LOG.info("手机号不存在，插入一条记录");
            Member member = new Member();
            member.setId(SnowUtil.getSnowflakeNextId());
            member.setMobile(mobile);
            mapper.insert(member);
        }
        String code = RandomUtil.randomNumbers(4);
        LOG.info("生成短信验证码：{}", code);// 保存短信记录表：手机号、短信验证码、有效期、是否已使用、业务类型、发送时间、使用时间
    }

    private Member selectByMobile(String mobile) {
        QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("mobile", mobile);
        List<Member> list = mapper.selectList(queryWrapper);
        if (CollUtil.isEmpty(list)) {
            return null;
        } else {
            return list.get(0);
        }
    }
}




