package com.associate.company.configuration;

import com.associate.company.api.CompanyService;
import com.associate.support.batch.BatchTaskSupportEngineer;
import com.associate.company.model.Company;

import java.io.InputStream;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Albert Gomes Cabral
 */
@Configuration
public class CompanyInitializeConfiguration {

    @Autowired
    public CompanyInitializeConfiguration(CompanyService companyService) {
        this._companyService = companyService;
    }

    @Bean
    public Company initCompany() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();

        try (InputStream inputStream = classLoader.getResourceAsStream(
                "company.properties")) {

            if (inputStream == null) {
                return new Company();
            }

            String content = new String(inputStream.readAllBytes());

            Map<String, String> companyProperties =
                    BatchTaskSupportEngineer.readCompanyProperties(content);

            String companyEmail = companyProperties.get("company.email.props");
            String companyName = companyProperties.get("company.name.props");
            String companyPhone = companyProperties.get("company.phone.props");

            Company company;

            if (_companyService.fetchCompanyByEmailOrName(
                    companyEmail, companyName) != null) {

                company = _companyService.fetchCompanyByEmailOrName(
                        companyEmail, companyName);

                _setCompany(company);

                return company;
            }

            company =  _companyService.addCompany(
                0L, companyEmail, companyName, companyPhone);

            _setCompany(company);

            return company;
        }
        catch (Exception exception) {
            throw new Exception(exception);
        }

    }

    @Bean
    public Company company() throws Exception {
        if (_getCompany() == null) return initCompany();

        return _getCompany();
    }

    private Company _getCompany() throws Exception {
        return _company;
    }

    private void _setCompany(Company company) throws Exception {
        this._company = company;
    }

    private Company _company;

    private final CompanyService _companyService;

}
