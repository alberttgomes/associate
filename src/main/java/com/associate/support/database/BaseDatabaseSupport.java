package com.associate.support.database;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Albert Gomes Cabral
 */
public abstract class BaseDatabaseSupport {

    protected Connection getConnection() {
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

}
