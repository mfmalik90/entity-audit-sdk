package com.careem.entityauditsdk.util.jsontype;

import org.hibernate.type.AbstractSingleColumnStandardBasicType;
import org.hibernate.usertype.DynamicParameterizedType;

import java.util.Properties;

/**
 * @author faizanmalik
 * creation date 2019-05-05
 */
public class JsonStringType extends AbstractSingleColumnStandardBasicType<Object>
        implements DynamicParameterizedType {

    public JsonStringType() {
        super(JsonStringSqlTypeDescriptor.INSTANCE, new JsonTypeDescriptor());
    }

    @Override
    public String getName() {
        return "json";
    }

    @Override
    protected boolean registerUnderJavaType() {
        return true;
    }

    @Override
    public void setParameterValues(Properties properties) {
        ((JsonTypeDescriptor) getJavaTypeDescriptor())
                .setParameterValues(properties);
    }
}
