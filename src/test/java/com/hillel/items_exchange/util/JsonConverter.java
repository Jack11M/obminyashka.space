package com.hillel.items_exchange.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.TimeZone;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JsonConverter {
    private static ObjectMapper objectMapper;

    public static String asJsonString(final Object obj) {
        try {
            if (objectMapper == null) {
                objectMapperInit();
            }
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T jsonToObject(final String json, Class<T> tClass) {
        try {
            if (objectMapper == null) {
                objectMapperInit();
            }
            return objectMapper.readValue(json, tClass);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void objectMapperInit() {
        objectMapper = new ObjectMapper();
        objectMapper.setTimeZone(TimeZone.getDefault());
        objectMapper.registerModule(new JavaTimeModule());
    }
}
