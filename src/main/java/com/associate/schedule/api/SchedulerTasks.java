package com.associate.schedule.api;

import org.quartz.Job;

/**
 * @author Albert Gomes Cabral
 */
public interface SchedulerTasks extends Job {

    String cronExpression();

    String getName();

}
