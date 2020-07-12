package com.hillel.items_exchange.util;

import com.hillel.items_exchange.dto.ChildDto;

import java.time.LocalDate;
import java.util.List;

import static com.hillel.items_exchange.util.JsonConverter.asJsonString;

public class ChildDtoCreatingUtil {

    private static final ChildDto CHILD_DTO_1 = new ChildDto(0, "male", LocalDate.of(2019, 3, 3));
    private static final ChildDto CHILD_DTO_2 = new ChildDto(0, "female", LocalDate.of(2019, 4, 4));
    private static final ChildDto CHILD_DTO_3 = new ChildDto(111L, "male", LocalDate.of(2019, 3, 3));
    private static final ChildDto CHILD_DTO_4 = new ChildDto(222L, "female", LocalDate.of(2019, 3, 3));
    private static final ChildDto CHILD_DTO_5 = new ChildDto(999L, "male", LocalDate.of(2019, 3, 3));
    private static final ChildDto CHILD_DTO_6 = new ChildDto(1L, "female", LocalDate.of(2018, 1, 1));
    private static final ChildDto CHILD_DTO_7 = new ChildDto(2L, "male", LocalDate.of(2018, 2, 2));
    private static final ChildDto CHILD_DTO_8 = new ChildDto(1L, "female", LocalDate.of(2019, 3, 3));

    public static String getValidChildDtoForCreate() {
        return asJsonString(List.of(CHILD_DTO_1, CHILD_DTO_2));
    }

    public static String getNotValidChildDtoForCreate() {
        return asJsonString(List.of(CHILD_DTO_3, CHILD_DTO_4));
    }

    public static String getNotValidChildDtoForDelete() {
        return asJsonString(List.of(CHILD_DTO_5));
    }

    public static String getNotValidChildDtoDuplicatedId() {
        return asJsonString(List.of(CHILD_DTO_5, CHILD_DTO_5));
    }

    public static String getValidChildDtoForUpdate() {
        return asJsonString(List.of(CHILD_DTO_6, CHILD_DTO_7));
    }

    public static String getNotValidChildDtoForUpdate() {
        return asJsonString(List.of(CHILD_DTO_8, CHILD_DTO_5));
    }
}