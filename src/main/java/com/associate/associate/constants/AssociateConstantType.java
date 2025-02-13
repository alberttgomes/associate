package com.associate.associate.constants;

import java.util.List;

/**
 * @author Albert Gomes Cabral
 */
public class AssociateConstantType {

    public static final String BLACK = "black";

    public static final String BRONZE = "bronze";

    public static final String GOLD = "gold";

    public static final String PLATINUM = "platinum";

    public static final String MASTER = "master";

    public static final String SILVER = "silver";

    public static List<String> getAssociateConstantsTypeList() {
        return List.of(BRONZE, GOLD, SILVER);
    }

}
