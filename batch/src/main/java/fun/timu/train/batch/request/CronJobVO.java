package fun.timu.train.batch.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CronJobVO {
    private String group;

    private String name;

    private String description;

    private String cronExpression;

}
