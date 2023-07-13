package fun.timu.train.business.request.daily;

import fun.timu.train.commo.request.PageVO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
public class DailyTrainQueryVO extends PageVO {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;

    private String code;

}
