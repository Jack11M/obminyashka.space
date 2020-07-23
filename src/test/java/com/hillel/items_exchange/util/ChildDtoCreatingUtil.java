package com.hillel.items_exchange.util;

import com.hillel.items_exchange.dto.ChildDto;

import java.time.LocalDate;
import java.util.List;

import static com.hillel.items_exchange.util.JsonConverter.asJsonString;

public class ChildDtoCreatingUtil {

    public static ChildDto getMaleChild(long id, LocalDate date) {
        return new ChildDto(id, "male", date);
    }

    public static ChildDto getFemaleChild(long id, LocalDate date) {
        return new ChildDto(id, "female", date);
    }

    public static String getValidChildDtoForCreate() {
        return asJsonString(List.of(
                getMaleChild(0L, LocalDate.of(2019, 3, 3)),
                getFemaleChild(0L, LocalDate.of(2019, 4, 4))
        ));
    }

    public static String getNotValidChildDtoForCreate() {
        return asJsonString(List.of(
                getMaleChild(111L, LocalDate.of(2019, 3, 3)),
                getFemaleChild(222L, LocalDate.of(2019, 4, 4))
        ));
    }

    public static String getValidChildDtoForUpdate() {
        return asJsonString(List.of(
                getMaleChild(1L, LocalDate.of(2018, 1, 1)),
                getFemaleChild(2L, LocalDate.of(2018, 2, 2))
        ));
    }

    public static String getNotValidChildDtoForUpdate() {
        return asJsonString(List.of(
                getMaleChild(1L, LocalDate.of(2018, 3, 3)),
                getFemaleChild(99999L, LocalDate.of(2018, 4, 4))
        ));
    }
}