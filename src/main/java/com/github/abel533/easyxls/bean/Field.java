package com.github.abel533.easyxls.bean;

/**
 * @author liuzh
 */
public class Field {
    private Object field;

    public Field(Object field) {
        this.field = field;
    }

    public void set(Object object, Object value) {
        if (field instanceof MapField) {
            ((MapField) field).set(object, value);
        } else if (field instanceof java.lang.reflect.Field) {
            try {
                ((java.lang.reflect.Field) field).set(object, value);
            } catch (IllegalAccessException e) {
                //ignore
            }
        }
    }

    public Object get(Object object) {
        if (field instanceof MapField) {
            return ((MapField) field).get(object);
        } else if (field instanceof java.lang.reflect.Field) {
            try {
                return ((java.lang.reflect.Field) field).get(object);
            } catch (IllegalAccessException e) {
                //ignore
            }
        }
        return null;
    }

    public String getCanonicalName() {
        if (field instanceof java.lang.reflect.Field) {
            return ((java.lang.reflect.Field) field).getType().getCanonicalName();
        }
        return Object.class.getCanonicalName();
    }
}
