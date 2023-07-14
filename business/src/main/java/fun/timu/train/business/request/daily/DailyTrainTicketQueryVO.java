package fun.timu.train.business.request.daily;

import fun.timu.train.commo.request.PageVO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
public class DailyTrainTicketQueryVO extends PageVO {
    /**
     * 日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;

    /**
     * 车次编号
     */
    private String trainCode;

    /**
     * 出发站
     */
    private String start;

    /**
     * 到达站
     */
    private String end;

}
