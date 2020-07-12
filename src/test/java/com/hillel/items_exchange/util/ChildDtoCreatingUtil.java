package com.hillel.items_exchange.util;

import com.hillel.items_exchange.dto.ChildDto;

import java.time.LocalDate;
import java.util.List;

import static com.hillel.items_exchange.util.JsonConverter.asJsonString;

public class ChildDtoCreatingUtil {
    public static String getValidChildDtoForCreate() {
        ChildDto dto = new ChildDto(0, "male", LocalDate.of(2019, 3, 3));
        ChildDto dto2 = new ChildDto(0, "female", LocalDate.of(2019, 4, 4));
        return asJsonString(List.of(dto, dto2));
    }

    public static String getNotValidChildDtoForCreate() {
        ChildDto dto = new ChildDto(111L, "male", LocalDate.of(2019, 3, 3));
        ChildDto dto2 = new ChildDto(222L, "female", LocalDate.of(2019, 3, 3));
        return asJsonString(List.of(dto, dto2));
    }

    public static String getValidChildDtoForDelete() {
        return asJsonString(List.of(1L));
    }

    public static String getNotValidChildDtoForDelete() {
        ChildDto dto = new ChildDto(999L, "male", LocalDate.of(2019, 3, 3));
        return asJsonString(List.of(dto));
    }

    public static String getNotValidChildDtoDuplicatedId() {
        ChildDto dto = new ChildDto(999L, "male", LocalDate.of(2019, 3, 3));
        return asJsonString(List.of(dto, dto));
    }

    public static String getValidChildDtoForUpdate() {
        ChildDto dto = new ChildDto(1L, "female", LocalDate.of(2018, 1, 1));
        ChildDto dto2 = new ChildDto(2L, "male", LocalDate.of(2018, 2, 2));
        return asJsonString(List.of(dto, dto2));
    }

    public static String getNotValidChildDtoForUpdate() {
        ChildDto dto = new ChildDto(1L, "female", LocalDate.of(2019, 3, 3));
        ChildDto dto2 = new ChildDto(999L, "male", LocalDate.of(2019, 3, 3));
        return asJsonString(List.of(dto, dto2));
    }
}