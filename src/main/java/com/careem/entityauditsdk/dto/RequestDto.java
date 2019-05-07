package com.careem.entityauditsdk.dto;

import lombok.Data;
import org.springframework.http.HttpMethod;

import java.util.HashMap;
import java.util.Map;

/**
 * @author faizanmalik
 * creation date 2019-05-06
 */
@Data
public class RequestDto {

    private String url;
    private String ipAddress;
    private HttpMethod method;
    private Map<String, String> headers = new HashMap<>(0);

    public void addHeader(String key, String value) {
        this.headers.put(key, value);
    }

    public void setMethod(String method) {
        this.method = HttpMethod.valueOf(method);
    }

    public String getHeader(String key){
        return this.headers.get(key);
    }
}

