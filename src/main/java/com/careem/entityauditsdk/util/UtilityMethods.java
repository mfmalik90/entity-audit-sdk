package com.careem.entityauditsdk.util;

import com.careem.entityauditsdk.dto.RequestDto;
import com.careem.entityauditsdk.type.EntityAuditType;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.persistence.Table;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.careem.entityauditsdk.util.Constants.*;

/**
 * @author faizanmalik
 * creation date 2019-05-05
 */
public final class UtilityMethods {

    private UtilityMethods(){}

    /*
    read the table annotation from the object and then return the name of the table
     */
    @NotNull
    public static String getTableName(Object entity) {
        return (entity.getClass().getAnnotation(Table.class)).name();
    }

    /*
    checks and object if it is marked with EnableEntityAuditing annotation
     */
    public static boolean entityAuditingEnabled(Object object, EntityAuditType auditType) {
        if (object == null) {
            return false;
        } else {
            EnableEntityAuditing entityAuditing = (EnableEntityAuditing)object.getClass()
                    .getAnnotation(EnableEntityAuditing.class);
            if (entityAuditing == null) {
                return false;
            } else {
                List<EntityAuditType> auditTypeArray = Arrays.asList(entityAuditing.auditType());
                return auditTypeArray.contains(EntityAuditType.ALL) || auditTypeArray.contains(auditType);
            }
        }
    }

    public static String getSourceService(){
        try {
            return (new MavenXpp3Reader()).read(new FileReader(POM)).getArtifactId();
        } catch (IOException e) {
            return null;
        } catch (XmlPullParserException e) {
            return null;
        }
    }

    public static String getSourceServiceVersion(){
        try {
            return (new MavenXpp3Reader()).read(new FileReader(POM)).getVersion();
        } catch (IOException e) {
            return null;
        } catch (XmlPullParserException e) {
            return null;
        }
    }

    public static RequestDto getRequestDto() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if(requestAttributes instanceof ServletRequestAttributes) {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                    .getRequest();
            RequestDto requestDto = new RequestDto();
            requestDto.setUrl(request.getRequestURL().toString());
            requestDto.setIpAddress(getIpAddress(request));
            requestDto.setMethod(request.getMethod());
            setHeaders(requestDto, request);
            return requestDto;
        } else {
            return null;
        }
    }

    private static String getIpAddress(HttpServletRequest request) {
        String ipAddress = request.getHeader(X_FORWARDED_FOR);
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }

    private static void setHeaders(RequestDto requestDto, HttpServletRequest request) {
        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            requestDto.addHeader(key, value);
        }
    }

    /*
    returns the request user info modifiedByType and modifiedBy
     */
    public static String getRequestUserInfo(RequestDto requestDto, String key) {
        Object userInfo;
        if(requestDto == null || requestDto.getHeader(key) == null) {
            if(RequestContextHolder.getRequestAttributes() == null){
                userInfo = null;
            } else {
                userInfo = RequestContextHolder.getRequestAttributes()
                        .getAttribute(key, RequestAttributes.SCOPE_REQUEST);
            }
        } else {
            return requestDto.getHeader(key);
        }
        if (userInfo == null) {
            return null;
        } else {
            return String.valueOf(userInfo);
        }
    }

    /*
    this method can be used to set the requestUser in the RequestContextHolder in case it doesn't exist
     and we want to persist in the audit log
     */
    public static void setRequestUserId(String requestUser) {
        RequestContextHolder.getRequestAttributes()
                .setAttribute(USER_ID, requestUser, RequestAttributes.SCOPE_REQUEST);
    }

    /*
    this method can be used to set the requestUserType in the RequestContextHolder in case it doesn't exist
     and we want to persist in the audit log
     */
    public static void setRequestUserType(String requestUserType) {
        RequestContextHolder.getRequestAttributes()
                .setAttribute(USER_TYPE, requestUserType, RequestAttributes.SCOPE_REQUEST);
    }

    public static String censorRequestDto(String request) {
        final String searchRegex = "\"password\":\".*?\"";
        final String replaceString = String.format("\"password\":\"%s\"", CENSORED);
        if (request.contains(PASSWORD)) {
            Pattern p = Pattern.compile(searchRegex);
            Matcher m = p.matcher(request);
            request = m.replaceAll(replaceString);
        }
        return request;
    }
}
