package space.obminyashka.items_exchange.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JsonConverter {
    private static ObjectMapper objectMapper;

    @SneakyThrows
    public static String asJsonString(final Object obj) {
        if (objectMapper == null) {
            objectMapperInit();
        }
        return objectMapper.writeValueAsString(obj);
    }

    @SneakyThrows
    public static <T> T jsonToObject(final String json, Class<T> tClass) {
        if (objectMapper == null) {
            objectMapperInit();
        }
        return objectMapper.readValue(json, tClass);
    }

    private static void objectMapperInit() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }
}
