package com.github.abel533.easyxls.common;

import com.github.abel533.easyxls.bean.Field;
import com.github.abel533.easyxls.bean.MapField;

import java.util.Map;

/**
 * @author liuzh
 */
public class FieldUtil {
    /**
     * 获取字段中的Field
     *
     * @param source    对象
     * @param fieldName 字段名
     * @return 返回字段对象
     */
    public static Field getField(Object source, String fieldName) {
        if (source == null) {
            return null;
        }
        return getField(source.getClass(), fieldName);
    }

    /**
     * 获取字段中的Field
     *
     * @param clazz     类
     * @param fieldName 字段名
     * @return 返回字段对象
     */
    public static Field getField(Class clazz, String fieldName) {
        if (Map.class.isAssignableFrom(clazz)) {
            return new Field(new MapField(fieldName));
        }
        java.lang.reflect.Field field = null;
        while (field == null && clazz != null) {
            try {
                field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
            } catch (Exception e) {
                clazz = clazz.getSuperclass();
            }
        }
        return new Field(field);
    }
}
