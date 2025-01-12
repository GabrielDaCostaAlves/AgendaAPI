package com.agendaapi.agendaapi.util;

import java.lang.reflect.Field;

public class DtoConverter {

    public static <T> T convert(Object source, Class<T> targetClass) throws IllegalAccessException, InstantiationException {
        T target = targetClass.newInstance();

        Field[] sourceFields = source.getClass().getDeclaredFields();
        Field[] targetFields = targetClass.getDeclaredFields();

        for (Field sourceField : sourceFields) {
            sourceField.setAccessible(true);

            for (Field targetField : targetFields) {
                targetField.setAccessible(true);

                if (sourceField.getName().equals(targetField.getName()) && sourceField.getType().equals(targetField.getType())) {
                    targetField.set(target, sourceField.get(source));
                }
            }
        }

        return target;
    }
}
