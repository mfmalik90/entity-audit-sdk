package com.hibernatesql.entityauditsdk.util.jsontype;

import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.AbstractTypeDescriptor;
import org.hibernate.type.descriptor.java.MutableMutabilityPlan;
import org.hibernate.usertype.DynamicParameterizedType;

import java.util.Properties;

/**
 * @author faizanmalik
 * creation date 2019-05-05
 */
public class JsonTypeDescriptor extends AbstractTypeDescriptor<Object>
        implements DynamicParameterizedType {

    private Class<?> jsonObjectClass;

    protected JsonTypeDescriptor() {
        super( Object.class, new MutableMutabilityPlan<Object>() {
            @Override
            protected Object deepCopyNotNull(Object value) {
                return JsonUtil.clone(value);
            }
        });
    }

    @Override
    public String toString(Object o) {
        return JsonUtil.objectToJsonString(o);
    }

    @Override
    public Object fromString(String s) {
        return JsonUtil.jsonStringToObject(s, jsonObjectClass);
    }

    @Override
    public <X> X unwrap(Object value, Class<X> type, WrapperOptions wrapperOptions) {
        if ( value == null ) {
            return null;
        }
        if ( String.class.isAssignableFrom( type ) ) {
            return (X) toString(value);
        }
        if ( Object.class.isAssignableFrom( type ) ) {
            return (X) JsonUtil.jsonStringToNode(toString(value));
        }
        throw unknownUnwrap( type );
    }

    @Override
    public <X> Object wrap(X value, WrapperOptions wrapperOptions) {
        if ( value == null ) {
            return null;
        }
        return fromString(value.toString());
    }

    @Override
    public void setParameterValues(Properties properties) {
        jsonObjectClass = ( (ParameterType) properties.get( PARAMETER_TYPE ) )
                .getReturnedClass();
    }

    @Override
    public boolean areEqual(Object one, Object another) {
        if ( one == another ) {
            return true;
        }
        if ( one == null || another == null ) {
            return false;
        }
        return JsonUtil.jsonStringToNode(JsonUtil.objectToJsonString(one)).equals(
                JsonUtil.jsonStringToNode(JsonUtil.objectToJsonString(another)));
    }
}

