package com.partner.configuration;

import com.partner.api.CompanyService;
import com.partner.model.Company;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.partner.util.CompanyThreadLocal;
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

    @Bean
    public Connection getConnection() {
        ClassLoader classLoader = getClass().getClassLoader();

        try (InputStream inputStream = classLoader.getResourceAsStream(
                "application.properties")) {

            if (inputStream == null) {
                return _buildDefaultConnection();
            }

            String content = new String(inputStream.readAllBytes());

            Properties props = new Properties();

            Pattern pattern = Pattern.compile(
                "spring\\.datasource\\.(url|username|password|driver-class-name)=(\\s*.*)\\n");

            Matcher matcher = pattern.matcher(content);

            while (matcher.find()) {
                props.setProperty(matcher.group(1), matcher.group(2));
            }

            if (props.isEmpty()) {
                return _buildDefaultConnection();
            }

            return DriverManager.getConnection(
                    props.getProperty("url"), props.getProperty("username"),
                    props.getProperty("password"));
        }
        catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    private Connection _buildDefaultConnection()
        throws SQLException, ClassNotFoundException {

        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3307/associate";
        String username = "associate";
        String password = "password";
        Class.forName(driver);

        return DriverManager.getConnection(
                url, username, password);
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
