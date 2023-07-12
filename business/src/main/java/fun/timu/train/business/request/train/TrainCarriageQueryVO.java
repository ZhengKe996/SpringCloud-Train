package fun.timu.train.business.request.train;

import fun.timu.train.commo.request.PageVO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrainCarriageQueryVO extends PageVO {
    private String trainCode;
}
