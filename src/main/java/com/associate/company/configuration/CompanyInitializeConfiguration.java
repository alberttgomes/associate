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

            if (_companyService.fetchCompanyByEmailOrName(
                    companyEmail, companyName) != null) {

                return _companyService.fetchCompanyByEmailOrName(
                        companyEmail, companyName);
            }

            return _companyService.addCompany(
                0L, companyEmail, companyName, companyPhone);
        }
        catch (Exception exception) {
            throw new Exception(exception);
        }

    }

    private final CompanyService _companyService;

}
