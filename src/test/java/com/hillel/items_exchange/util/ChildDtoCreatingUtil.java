package com.hillel.items_exchange.util;

import com.hillel.items_exchange.dto.ChildDto;

import java.time.LocalDate;
import java.util.List;

import static com.hillel.items_exchange.util.JsonConverter.asJsonString;

public class ChildDtoCreatingUtil {
    public static String getValidChildDtoForCreate() {
        ChildDto dto = new ChildDto();
        dto.setSex("male");
        dto.setBirthDate(LocalDate.of(2019, 3, 3));
        ChildDto dto2 = new ChildDto();
        dto2.setSex("female");
        dto2.setBirthDate(LocalDate.of(2019, 4, 4));
        return asJsonString(List.of(dto, dto2));
    }

    public static String getNotValidChildDtoForCreate() {
        ChildDto dto = new ChildDto();
        dto.setId(111L);
        dto.setSex("male");
        dto.setBirthDate(LocalDate.of(2019, 3, 3));
        ChildDto dto2 = new ChildDto();
        dto.setId(222L);
        dto2.setSex("female");
        dto2.setBirthDate(LocalDate.of(2019, 3, 3));
        return asJsonString(List.of(dto, dto2));
    }

    public static String getValidChildDtoForDelete() {
        return asJsonString(List.of(1L));
    }

    public static String getNotValidChildDtoForDelete() {
        ChildDto dto = new ChildDto();
        dto.setId(999L);
        dto.setSex("male");
        dto.setBirthDate(LocalDate.of(2019, 3, 3));
        return asJsonString(List.of(dto));
    }

    public static String getNotValidChildDtoDuplicatedId() {
        ChildDto dto = new ChildDto();
        dto.setId(999L);
        dto.setSex("male");
        dto.setBirthDate(LocalDate.of(2019, 3, 3));
        return asJsonString(List.of(dto, dto));
    }

    public static String getValidChildDtoForUpdate() {
        ChildDto dto = new ChildDto();
        dto.setId(1L);
        dto.setSex("female");
        dto.setBirthDate(LocalDate.of(2018, 1, 1));
        ChildDto dto2 = new ChildDto();
        dto2.setId(2L);
        dto2.setSex("male");
        dto2.setBirthDate(LocalDate.of(2018, 2, 2));
        return asJsonString(List.of(dto, dto2));
    }

    public static String getNotValidChildDtoForUpdate() {
        ChildDto dto = new ChildDto();
        dto.setId(1L);
        dto.setSex("female");
        dto.setBirthDate(LocalDate.of(2019, 3, 3));
        ChildDto dto2 = new ChildDto();
        dto2.setId(999L);
        dto2.setSex("male");
        dto2.setBirthDate(LocalDate.of(2019, 3, 3));
        return asJsonString(List.of(dto, dto2));
    }
}