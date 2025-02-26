package com.associate.schedule.configuration;

import com.associate.schedule.api.SchedulerTasks;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Albert Gomes Cabral
 */
@Configuration
public class SchedulersInitializationConfiguration {

    @Bean
    protected List<SchedulerTasks> schedulerTasksTracker() {
        Map<String, SchedulerTasks> beans =
                _applicationContext.getBeansOfType(SchedulerTasks.class);

        beans.forEach(
            (name, bean) -> {
                try {
                    if (name.equalsIgnoreCase(bean.getClass().getName())) {
                        return;
                    }

                    JobDetail jobDetail = _jobDetailBuilder(bean.getClass());

                    Trigger trigger = _triggerBuilder(bean.cronExpression(), jobDetail);

                    if (_scheduler.checkExists(jobDetail.getKey())) {
                        System.out.printf(
                            "Job %s already exists, skipping to next...\n", bean.getName());
                    }
                    else {
                        Date schedulerFireDate = _scheduler.scheduleJob(jobDetail, trigger);

                        System.out.println("==================================");
                        System.out.printf("key: %s\n", jobDetail.getKey());
                        System.out.printf("scheduler fire date: %s\n", schedulerFireDate);
                        System.out.println("==================================");
                    }
                }
                catch (SchedulerException schedulerException) {
                    throw new RuntimeException(schedulerException.getCause());
                }
            }
        );

        return new ArrayList<>(beans.values());
    }

    @Autowired
    public SchedulersInitializationConfiguration(
            ApplicationContext applicationContext, Scheduler scheduler) {

        this._applicationContext = applicationContext;
        this._scheduler = scheduler;
    }

    private JobDetail _jobDetailBuilder(Class<?> clazz) {
        String identity = clazz.getName();

        return JobBuilder.newJob(clazz.asSubclass(Job.class))
                .withIdentity(identity)
                .storeDurably()
                .build();
    }

    private Trigger _triggerBuilder(String cronExpression, JobDetail jobDetail) {
        String identity = jobDetail.getKey().getName();

        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity(identity)
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
                .build();
    }

    private final ApplicationContext _applicationContext;

    private final Scheduler _scheduler;

}
