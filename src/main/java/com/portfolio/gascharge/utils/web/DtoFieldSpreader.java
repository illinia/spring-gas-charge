package com.portfolio.gascharge.utils.web;

import org.springframework.util.ObjectUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class DtoFieldSpreader {

    public static Map<String, Object> of(Object object) {
        Map<String, Object> attributesMap = new HashMap<>();

        for (Field field : object.getClass().getDeclaredFields()) {
            field.setAccessible(true);

            try {
                Object value = field.get(object);

                if (!ObjectUtils.isEmpty(value)) {
                    String name = field.getName();
                    attributesMap.put(name, value);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        return attributesMap;
    }
}
