package com.hillel.items_exchange.util;

import com.hillel.items_exchange.dto.ChildDto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static com.hillel.items_exchange.util.JsonConverter.asJsonString;

public class ChildDtoCreatingUtil {
    public static String getJsonOfChildrenDto(long maleId, long femaleId, int year) {
        return asJsonString(List.of(
                getChildDto(maleId, LocalDate.of(year, 3, 3), "male"),
                getChildDto(femaleId, LocalDate.of(year, 4, 4), "female")
        ));
    }

    public static String getJsonOfChildrenDto(int quantity) {
        return asJsonString(getChildrenDtoList(quantity));
    }

    public static List<ChildDto> getChildrenDtoList(int quantity) {
        List<ChildDto> childDtoList = new ArrayList<>();
        IntStream.range(0, quantity).forEach(i ->
                childDtoList.add(getChildDto(0L, LocalDate.of(2010, 1, 1), "male")));
        return childDtoList;
    }

    public static ChildDto getChildDto(long id, LocalDate birthDate, String sex) {
        return ChildDto.builder()
                .id(id)
                .birthDate(birthDate)
                .sex(sex)
                .build();
    }
}
