package fun.timu.train.batch.job;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import fun.timu.train.batch.feign.BusinessFeign;
import fun.timu.train.commo.response.BaseResponse;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

@DisallowConcurrentExecution
public class DailyTrainJob implements Job {
    private static final Logger LOG = LoggerFactory.getLogger(DailyTrainJob.class);

    private final BusinessFeign businessFeign;

    public DailyTrainJob(BusinessFeign businessFeign) {
        this.businessFeign = businessFeign;
    }


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        // 增加日志流水号
        LOG.info("生成5天后的车次数据开始");
        Date date = new Date();
        DateTime dateTime = DateUtil.offsetDay(date, 5);
        Date offsetDate = dateTime.toJdkDate();
        BaseResponse response = businessFeign.genDaily(offsetDate);
        LOG.info("生成5天后的车次数据结束，结果：{}", response.toString());
    }
}