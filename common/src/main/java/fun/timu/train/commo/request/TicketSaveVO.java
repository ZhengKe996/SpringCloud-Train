package fun.timu.train.commo.request;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class TicketSaveVO {
    /**
     * id
     */
    private Long id;

    /**
     * 会员id
     */
    private Long memberId;

    /**
     * 乘客id
     */
    private Long passengerId;

    /**
     * 乘客姓名
     */
    private String passengerName;

    /**
     * 日期
     */
    private Date date;

    /**
     * 车次编号
     */
    private String trainCode;

    /**
     * 厢序
     */
    private Integer carriageIndex;

    /**
     * 排号|01, 02
     */
    @TableField(value = "`row`")
    private String row;

    /**
     * 列号|枚举[SeatColEnum]
     */
    @TableField(value = "`col`")
    private String col;

    /**
     * 始发站
     */
    private String start;

    /**
     * 出发时间
     */
    private Date startTime;

    /**
     * 终点站
     */
    private String end;

    /**
     * 到站时间
     */
    private Date endTime;

    /**
     * 座位类型|枚举[SeatTypeEnum]
     */
    private String seatType;

    /**
     * 新增时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

}
