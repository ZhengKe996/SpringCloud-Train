package fun.timu.train.commo.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberLoginResponse {
    private Long id;

    private String mobile;

    private String token;
}


