package com.partner.service.util;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.springframework.stereotype.Component;

/**
 * @author Albert Gomes Cabral
 */
@Component
public class CompanyDynamicQuery extends BaseDatabaseEngineer {

    public boolean hasCompany(long companyId) {
        String query = "select count(*) from company where company_id = ?";

        try(PreparedStatement preparedStatement =
                    getConnection().prepareStatement(query)) {

            preparedStatement.setLong(1, companyId);

            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }

            preparedStatement.addBatch();

            preparedStatement.executeQuery();
        }
        catch (Exception exception) {
            throw new RuntimeException(exception);
        }

        return false;
    }

}
