package com.hibernatesql.entityauditsdk.util.jsontype;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * @author faizanmalik
 * creation date 2019-05-05
 */
public final class JsonUtil {

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static String objectToJsonString(Object o){
        try {
            return objectMapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static <T> T jsonStringToObject(String json, Class<T> toObjectClass){
        try {
            return objectMapper.readValue(json, toObjectClass);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static <T> T convert(Object fromValue , Class<T> toObjectClass){
        return objectMapper.convertValue(fromValue, toObjectClass);
    }

    public static JsonNode jsonStringToNode(String json) {
        try {
            return objectMapper.readTree(json);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static <T> T clone(T value) {
        return jsonStringToObject(objectToJsonString(value), (Class<T>) value.getClass());
    }

}

