package com.partner.util;

import com.partner.model.Company;

/**
 * @author Albert Gomes Cabral
 */
public class CompanyThreadLocal {

    public static void setCompanyThreadLocal(Company company) {
        companyThreadLocal.set(company);
    }

    public static Company getCompanyThreadLocal() {
        return companyThreadLocal.get();
    }

    private static final ThreadLocal<Company> companyThreadLocal =
            ThreadLocal.withInitial(Company::new);

}
