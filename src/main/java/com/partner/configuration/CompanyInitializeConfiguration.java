package com.partner.configuration;

import com.partner.api.CompanyService;
import com.partner.model.Company;
import com.partner.util.CompanyThreadLocal;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
                    _readCompanyProperties(content);

            String companyEmail = companyProperties.get("company.email.props");
            String companyName = companyProperties.get("company.name.props");
            String companyPhone = companyProperties.get("company.phone.props");

            if (_companyService.fetchCompanyByEmailOrName(
                    companyEmail, companyName) != null) {

                return _companyService.fetchCompanyByEmailOrName(
                        companyEmail, companyName);
            }

            Company company = _companyService.addCompany(
                0L, companyEmail, companyName, companyPhone);

            CompanyThreadLocal.setCompanyThreadLocal(company);

            return company;
        }
        catch (Exception exception) {
            throw new Exception(exception);
        }

    }

    private Map<String, String> _readCompanyProperties(String content) {
        Pattern pattern = Pattern.compile(
                "(company\\.(email|phone|name)\\.props)=((\\w*.*)+)");

        Matcher matcher = pattern.matcher(content);

        Map<String, String> companyProperties = new HashMap<>();

        while (matcher.find()) {
            companyProperties.put(matcher.group(1), matcher.group(3));
        }

        return companyProperties;
    }

    private final CompanyService _companyService;

}
