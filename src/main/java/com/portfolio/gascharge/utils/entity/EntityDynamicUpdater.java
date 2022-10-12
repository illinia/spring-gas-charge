package com.portfolio.gascharge.utils.entity;

import java.lang.reflect.Field;
import java.util.Map;

public class EntityDynamicUpdater {

    public static void update(Map<String, Object> attributesMap, Object charge) {
        attributesMap.forEach((key, value) -> {
            try {
                Field field = charge.getClass().getDeclaredField(key);
                System.out.println("updateDynamicField field = " + field + " and value = " + value);
                field.setAccessible(true);
                field.set(charge, value);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(printErrorMessage(e, charge, key, value));
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(printErrorMessage(e, charge, key, value));
            }
        });
    }

    private static String printErrorMessage(Exception e, Object clazz, String key, Object value) {
        return "EntityDynamicUpdater has " + e.getClass() + " error in updateDynamicField method when try to update" + clazz.getClass() + " entity field key : " + key + ", value : " + value.toString();
    }
}
