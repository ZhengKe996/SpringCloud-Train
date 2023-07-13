package fun.timu.train.batch.controller;

import fun.timu.train.batch.request.CronJobVO;
import fun.timu.train.batch.response.CronJobResponse;
import fun.timu.train.commo.response.BaseResponse;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/admin/job")
public class JobController {

    private static Logger LOG = LoggerFactory.getLogger(JobController.class);

    private final SchedulerFactoryBean schedulerFactoryBean;

    public JobController(SchedulerFactoryBean schedulerFactoryBean) {
        this.schedulerFactoryBean = schedulerFactoryBean;
    }

    @RequestMapping(value = "/run")
    public BaseResponse run(@RequestBody CronJobVO jobVO) throws SchedulerException {
        String jobClassName = jobVO.getName();
        String jobGroupName = jobVO.getGroup();
        schedulerFactoryBean.getScheduler().triggerJob(JobKey.jobKey(jobClassName, jobGroupName));
        LOG.info("手动执行任务开始：{}, {}", jobClassName, jobGroupName);
        return new BaseResponse();
    }

    @RequestMapping(value = "/add")
    public BaseResponse add(@RequestBody CronJobVO jobVO) {
        String jobClassName = jobVO.getName();
        String jobGroupName = jobVO.getGroup();
        String cronExpression = jobVO.getCronExpression();
        String description = jobVO.getDescription();
        LOG.info("创建定时任务开始：{}，{}，{}，{}", jobClassName, jobGroupName, cronExpression, description);

        BaseResponse response = new BaseResponse();
        try {
            // 通过SchedulerFactory获取一个调度器实例
            Scheduler sched = schedulerFactoryBean.getScheduler();

            // 启动调度器
            sched.start();

            //构建job信息
            JobDetail jobDetail = JobBuilder.newJob((Class<? extends Job>) Class.forName(jobClassName)).withIdentity(jobClassName, jobGroupName).build();

            //表达式调度构建器(即任务执行的时间)
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);

            //按新的cronExpression表达式构建一个新的trigger
            CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobClassName, jobGroupName).withDescription(description).withSchedule(scheduleBuilder).build();

            sched.scheduleJob(jobDetail, trigger);

        } catch (SchedulerException e) {
            LOG.error("创建定时任务失败:" + e);
            response.setSuccess(false);
            response.setMessage("创建定时任务失败:调度异常");
        } catch (ClassNotFoundException e) {
            LOG.error("创建定时任务失败:" + e);
            response.setSuccess(false);
            response.setMessage("创建定时任务失败：任务类不存在");
        }
        LOG.info("创建定时任务结束：{}", response);
        return response;
    }

    @RequestMapping(value = "/pause")
    public BaseResponse pause(@RequestBody CronJobVO jobVO) {
        String jobClassName = jobVO.getName();
        String jobGroupName = jobVO.getGroup();
        LOG.info("暂停定时任务开始：{}，{}", jobClassName, jobGroupName);
        BaseResponse response = new BaseResponse();
        try {
            Scheduler sched = schedulerFactoryBean.getScheduler();
            sched.pauseJob(JobKey.jobKey(jobClassName, jobGroupName));
        } catch (SchedulerException e) {
            LOG.error("暂停定时任务失败:" + e);
            response.setSuccess(false);
            response.setMessage("暂停定时任务失败:调度异常");
        }
        LOG.info("暂停定时任务结束：{}", response);
        return response;
    }

    @RequestMapping(value = "/resume")
    public BaseResponse resume(@RequestBody CronJobVO jobVO) {
        String jobClassName = jobVO.getName();
        String jobGroupName = jobVO.getGroup();
        LOG.info("重启定时任务开始：{}，{}", jobClassName, jobGroupName);
        BaseResponse response = new BaseResponse();
        try {
            Scheduler sched = schedulerFactoryBean.getScheduler();
            sched.resumeJob(JobKey.jobKey(jobClassName, jobGroupName));
        } catch (SchedulerException e) {
            LOG.error("重启定时任务失败:" + e);
            response.setSuccess(false);
            response.setMessage("重启定时任务失败:调度异常");
        }
        LOG.info("重启定时任务结束：{}", response);
        return response;
    }

    @RequestMapping(value = "/reschedule")
    public BaseResponse reschedule(@RequestBody CronJobVO jobVO) {
        String jobClassName = jobVO.getName();
        String jobGroupName = jobVO.getGroup();
        String cronExpression = jobVO.getCronExpression();
        String description = jobVO.getDescription();
        LOG.info("更新定时任务开始：{}，{}，{}，{}", jobClassName, jobGroupName, cronExpression, description);
        BaseResponse response = new BaseResponse();
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            TriggerKey triggerKey = TriggerKey.triggerKey(jobClassName, jobGroupName);
            // 表达式调度构建器
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
            CronTriggerImpl trigger1 = (CronTriggerImpl) scheduler.getTrigger(triggerKey);
            trigger1.setStartTime(new Date()); // 重新设置开始时间
            CronTrigger trigger = trigger1;

            // 按新的cronExpression表达式重新构建trigger
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withDescription(description).withSchedule(scheduleBuilder).build();

            // 按新的trigger重新设置job执行
            scheduler.rescheduleJob(triggerKey, trigger);
        } catch (Exception e) {
            LOG.error("更新定时任务失败:" + e);
            response.setSuccess(false);
            response.setMessage("更新定时任务失败:调度异常");
        }
        LOG.info("更新定时任务结束：{}", response);
        return response;
    }

    @RequestMapping(value = "/delete")
    public BaseResponse delete(@RequestBody CronJobVO jobVO) {
        String jobClassName = jobVO.getName();
        String jobGroupName = jobVO.getGroup();
        LOG.info("删除定时任务开始：{}，{}", jobClassName, jobGroupName);
        BaseResponse response = new BaseResponse();
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            scheduler.pauseTrigger(TriggerKey.triggerKey(jobClassName, jobGroupName));
            scheduler.unscheduleJob(TriggerKey.triggerKey(jobClassName, jobGroupName));
            scheduler.deleteJob(JobKey.jobKey(jobClassName, jobGroupName));
        } catch (SchedulerException e) {
            LOG.error("删除定时任务失败:" + e);
            response.setSuccess(false);
            response.setMessage("删除定时任务失败:调度异常");
        }
        LOG.info("删除定时任务结束：{}", response);
        return response;
    }

    @RequestMapping(value = "/query")
    public BaseResponse query() {
        LOG.info("查看所有定时任务开始");
        BaseResponse response = new BaseResponse();
        List<CronJobResponse> cronJobDtoList = new ArrayList();
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            for (String groupName : scheduler.getJobGroupNames()) {
                for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
                    CronJobResponse jobResponse = new CronJobResponse();
                    jobResponse.setName(jobKey.getName());
                    jobResponse.setGroup(jobKey.getGroup());

                    //get job's trigger
                    List<Trigger> triggers = (List<Trigger>) scheduler.getTriggersOfJob(jobKey);
                    CronTrigger cronTrigger = (CronTrigger) triggers.get(0);
                    jobResponse.setNextFireTime(cronTrigger.getNextFireTime());
                    jobResponse.setPreFireTime(cronTrigger.getPreviousFireTime());
                    jobResponse.setCronExpression(cronTrigger.getCronExpression());
                    jobResponse.setDescription(cronTrigger.getDescription());
                    Trigger.TriggerState triggerState = scheduler.getTriggerState(cronTrigger.getKey());
                    jobResponse.setState(triggerState.name());

                    cronJobDtoList.add(jobResponse);
                }

            }
        } catch (SchedulerException e) {
            LOG.error("查看定时任务失败:" + e);
            response.setSuccess(false);
            response.setMessage("查看定时任务失败:调度异常");
        }
        response.setData(cronJobDtoList);
        LOG.info("查看定时任务结束：{}", response);
        return response;
    }
}
