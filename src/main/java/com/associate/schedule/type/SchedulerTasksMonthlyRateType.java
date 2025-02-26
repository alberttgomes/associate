package com.associate.schedule.type;

import com.associate.associate.api.AssociateService;
import com.associate.associate.model.Associate;
import com.associate.company.model.Company;
import com.associate.schedule.api.SchedulerTasks;

import java.time.LocalDateTime;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * @author Albert Gomes Cabral
 */
@Component
public class SchedulerTasksMonthlyRateType implements SchedulerTasks {

    @Autowired
    public SchedulerTasksMonthlyRateType(
            AssociateService associateService, @Qualifier("company") Company company) {

        this._associateService = associateService;
        this._company = company;
    }

    @Override
    public String cronExpression() {
        LocalDateTime localDateTime = LocalDateTime.now();

        int dayOfMonth = localDateTime.getDayOfMonth();
        int hour = localDateTime.getHour();
        int minute = localDateTime.getMinute();

        return String.format("0 %d %d %d * ?", minute, hour, dayOfMonth);
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.printf("%s job started. \n".formatted(getName()));

        for (Associate associate :
                _associateService.fetchAllAssociatesByCompanyId(
                        _company.getCompanyId())) {

            System.out.printf(
                    "Associate %s%n", associate.getAssociateName());
        }

        System.out.printf("Last execution %s\n", context.getPreviousFireTime());
        System.out.printf("Next execution %s\n", context.getNextFireTime());
    }

    @Override
    public String getName() {
        return "Monthly rate calculation";
    }

    private final AssociateService _associateService;

    private final Company _company;

}
