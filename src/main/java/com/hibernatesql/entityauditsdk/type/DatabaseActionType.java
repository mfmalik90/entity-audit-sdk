package com.hibernatesql.entityauditsdk.type;

/**
 * @author faizanmalik
 * creation date 2019-05-05
 */
public enum DatabaseActionType {
    INSERT("INSERT"),
    UPDATE("UPDATE"),
    DELETE("DELETE");


    /**
     * Value for this DatabaseActionType
     */
    public final String code;

    DatabaseActionType(String code)
    {
        this.code = code;
    }

}
