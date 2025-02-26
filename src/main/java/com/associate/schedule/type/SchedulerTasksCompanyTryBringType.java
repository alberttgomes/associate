package com.associate.schedule.type;

import com.associate.associate.api.AssociateService;
import com.associate.associate.constants.AssociateConstantStatus;
import com.associate.associate.model.Associate;
import com.associate.company.api.CompanyService;
import com.associate.company.model.Company;
import com.associate.notify.api.NotifyService;
import com.associate.schedule.api.SchedulerTasks;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Albert Gomes Cabral
 */
@Component
public class SchedulerTasksCompanyTryBringType implements SchedulerTasks {

    @Autowired
    public SchedulerTasksCompanyTryBringType(
            AssociateService associateService, CompanyService companyService,
            NotifyService notifyService) {

        this._associateService = associateService;
        this._companyService = companyService;
        this._notifyService = notifyService;
    }

    @Override
    public String cronExpression() {
        return "0 0 8-17/5 * * ?";
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            System.out.printf("%s job started. \n".formatted(getName()));

            for (Company company : _companyService.fetchAllCompanies()) {
                System.out.printf(
                    "Company %s %s\n", company.getCompanyName(), company.getCompanyId());

                for (Associate associate : _associateService.fetchAllAssociatesByCompanyId(
                        company.getCompanyId())) {

                    String phone = "+" + associate.getAssociatePhoneNumber();

                    String status = associate.getAssociateStatus();

                    if (status.equals(AssociateConstantStatus.SUSPEND) ||
                            status.equals(AssociateConstantStatus.PENDING)) {

                        String messageText = String.format(
                                "Hi %s . Please, let us that happened, how we can help you?",
                                associate.getAssociateName());

                        _notifyService.sendWhatsAppMessageNotify(messageText, phone);
                    }
                }
            }

            System.out.printf(
                "Last execution %s\n", jobExecutionContext.getPreviousFireTime());
            System.out.printf(
                "Next execution %s\n", jobExecutionContext.getNextFireTime());
        }
        catch (Exception exception) {
            throw new RuntimeException(exception.getCause());
        }
    }

    @Override
    public String getName() {
            return "Company trying bring olds associates";
    }

    private final AssociateService _associateService;

    private final CompanyService _companyService;

    private final NotifyService _notifyService;

}
