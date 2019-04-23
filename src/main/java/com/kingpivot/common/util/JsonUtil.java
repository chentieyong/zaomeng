package com.kingpivot.common.util;

import com.google.gson.Gson;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

import java.io.IOException;

public class JsonUtil {
    public static String writeValueAsString(Object o) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS);
        try {
            return mapper.writeValueAsString(o);
        } catch (IOException e) {
        }
        return null;
    }

    public static <T> T readObject(String json, Class<T> clazz) {
        return (new Gson()).fromJson(json, clazz);
    }

    public static String toJson(Object o){
        return (new Gson()).toJson(o);
    }
}
