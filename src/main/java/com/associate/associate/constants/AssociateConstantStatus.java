package com.associate.associate.constants;

import java.util.List;

/**
 * @author Albert Gomes Cabral
 */
public class AssociateConstantStatus {

    public static final String APPROVED = "approved";

    public static final String PENDING = "pending";

    public static final String REJECTED = "rejected";

    public static final String SUSPEND = "suspend";

    public static List<String> getAvailableStatusList() {
        return List.of(APPROVED, PENDING, REJECTED, SUSPEND);
    }

}
