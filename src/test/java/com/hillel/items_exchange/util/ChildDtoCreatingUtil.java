package com.hillel.items_exchange.util;

import com.hillel.items_exchange.dto.ChildDto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import static com.hillel.items_exchange.util.JsonConverter.asJsonString;

public class ChildDtoCreatingUtil {
    public static String getValidChildDtoForCreate() throws ParseException {
        ChildDto dto = new ChildDto();
        dto.setSex("male");
        dto.setBirthDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-03-03"));
        ChildDto dto2 = new ChildDto();
        dto2.setSex("female");
        dto2.setBirthDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-04-04"));
        return asJsonString(List.of(dto, dto2));
    }

    public static String getNotValidChildDtoForCreate() throws ParseException {
        ChildDto dto = new ChildDto();
        dto.setId(111L);
        dto.setSex("male");
        dto.setBirthDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-03-03"));
        ChildDto dto2 = new ChildDto();
        dto.setId(222L);
        dto2.setSex("female");
        dto2.setBirthDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-04-04"));
        return asJsonString(List.of(dto, dto2));
    }

    public static String getValidChildDtoForDelete() throws ParseException {
        ChildDto dto = new ChildDto();
        dto.setId(1L);
        dto.setSex("male");
        dto.setBirthDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-01-01"));
        return asJsonString(List.of(dto));
    }

    public static String getNotValidChildDtoForDelete() throws ParseException {
        ChildDto dto = new ChildDto();
        dto.setId(999L);
        dto.setSex("male");
        dto.setBirthDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-01-01"));
        return asJsonString(List.of(dto));
    }

    public static String getNotValidChildDtoDuplicatedId() throws ParseException {
        ChildDto dto = new ChildDto();
        dto.setId(999L);
        dto.setSex("male");
        dto.setBirthDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-01-01"));
        return asJsonString(List.of(dto, dto));
    }

    public static String getValidChildDtoForUpdate() throws ParseException {
        ChildDto dto = new ChildDto();
        dto.setId(1L);
        dto.setSex("female");
        dto.setBirthDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-11"));
        ChildDto dto2 = new ChildDto();
        dto2.setId(2L);
        dto2.setSex("male");
        dto2.setBirthDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-12-12"));
        return asJsonString(List.of(dto, dto2));
    }

    public static String getNotValidChildDtoForUpdate() throws ParseException {
        ChildDto dto = new ChildDto();
        dto.setId(1L);
        dto.setSex("female");
        dto.setBirthDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-11"));
        ChildDto dto2 = new ChildDto();
        dto2.setId(999L);
        dto2.setSex("male");
        dto2.setBirthDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-12-12"));
        return asJsonString(List.of(dto, dto2));
    }
}
