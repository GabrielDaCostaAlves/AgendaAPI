package com.agendaapi.agendaapi.util.conversor;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class ConverterClass {

    public static <T> T convert(Object source, Class<T> targetClass) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException, NoSuchFieldException {
        T target = targetClass.getDeclaredConstructor().newInstance();

        Field[] sourceFields = source.getClass().getDeclaredFields();
        Field[] targetFields = targetClass.getDeclaredFields();

        for (Field targetField : targetFields) {
            targetField.setAccessible(true);

            for (Field sourceField : sourceFields) {
                sourceField.setAccessible(true);

                // Verifica se o nome e tipo são compatíveis
                if (sourceField.getName().equals(targetField.getName()) && sourceField.getType().equals(targetField.getType())) {
                    targetField.set(target, sourceField.get(source));
                }

                // Tratamento específico para o campo "role"
                if (targetField.getName().equals("role") && sourceField.getName().equals("role")) {
                    Object role = sourceField.get(source); // Obtem a entidade Role
                    if (role != null) {
                        // Obtém o campo "name" de Role
                        Field roleNameField = role.getClass().getDeclaredField("name");
                        roleNameField.setAccessible(true);
                        targetField.set(target, roleNameField.get(role)); // Define o valor em UsuarioVO
                    }
                }

                // Tratamento específico para o campo "id" (agora key)
                if (targetField.getName().equals("key") && sourceField.getName().equals("id")) {
                    Long id = (Long) sourceField.get(source); // Obtem o id da entidade Usuario
                    if (id != null) {
                        targetField.set(target, id.toString()); // Define o valor de key como String
                    }
                }
            }
        }

        return target;
    }
}
