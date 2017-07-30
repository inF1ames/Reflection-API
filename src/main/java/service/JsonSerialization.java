package service;

import annotation.CustomDateFormat;
import annotation.JsonValue;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class JsonSerialization {
    private Map<String, Object> fields = new HashMap<>();

    public String toJson(Object object) {
        Field[] declaredFields = object.getClass().getDeclaredFields();
        for (Field declaredField : declaredFields) {
            declaredField.setAccessible(true);
            try {
                if (declaredField.get(object) != null)
                    addElement(declaredField, object);
                declaredField.setAccessible(false);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return getString();
    }


    private void addElement(Field field, Object object) throws IllegalAccessException {
        if (field.isAnnotationPresent(JsonValue.class)) {
            fields.put(field.getAnnotation(JsonValue.class).name(), field.get(object));
        } else if (field.isAnnotationPresent(CustomDateFormat.class)) {
            DateTimeFormatter formatter = DateTimeFormatter.
                    ofPattern(field.getAnnotation(CustomDateFormat.class).format());
            fields.put(field.getName(), ((LocalDate) field.get(object)).format(formatter));
        } else {
            fields.put(field.getName(), field.get(object));
        }
    }

    private String getString() {
        StringBuilder json = new StringBuilder("{");
        int elementsCount = 0;
        for (Map.Entry<String, Object> objectEntry : fields.entrySet()) {
            json.append("\"")
                    .append(objectEntry.getKey())
                    .append("\":\"")
                    .append(objectEntry.getValue());
            if (elementsCount != fields.size() - 1) {
                json.append("\",");
            } else {
                json.append("\"}");
            }
            elementsCount++;
        }
        fields.clear();
        return json.toString();
    }

}
