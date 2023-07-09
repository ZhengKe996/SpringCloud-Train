package fun.timu.train.member.mapper;

import fun.timu.train.member.entity.Member;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * @author zhengke
 * @description 针对表【member(会员)】的数据库操作Mapper
 * @createDate 2023-07-09 21:05:19
 * @Entity generator.entity.Member
 */
@Repository
public interface MemberMapper extends BaseMapper<Member> {
    int countMember();
}




