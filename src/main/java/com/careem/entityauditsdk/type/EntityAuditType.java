package com.careem.entityauditsdk.type;

/**
 * @author faizanmalik
 * creation date 2019-05-05
 * Enum to select which type of hibernate event will be saved in the audit log table.
 */
public enum EntityAuditType {
    ALL("ALL"),
    INSERT("INSERT"),
    UPDATE("UPDATE"),
    DELETE("DELETE");

    /**
     * Value for this EntityAuditType
     */
    public final String code;

    EntityAuditType(String code)
    {
        this.code = code;
    }

}
