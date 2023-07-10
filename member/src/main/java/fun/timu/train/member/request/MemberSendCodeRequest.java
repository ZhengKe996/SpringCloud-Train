package fun.timu.train.member.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberSendCodeRequest {
    @NotBlank(message = "【手机号】不能为空")
    @Pattern(regexp = "^1[3-9][0-9]{9}$", message = "【手机号】手机号错误请检查")
    private String mobile;
}
