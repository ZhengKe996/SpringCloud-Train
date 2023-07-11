package fun.timu.train.member.request;

import fun.timu.train.commo.request.PageVO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PassengerQueryVO extends PageVO {
    private Long memberId;
}
