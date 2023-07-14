package fun.timu.train.business.request.daily;

import fun.timu.train.commo.request.PageVO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DailyTrainSeatQueryVO extends PageVO {
    private String trainCode;
}
