package space.obminyashka.items_exchange.util;

import space.obminyashka.items_exchange.dto.ChildDto;
import space.obminyashka.items_exchange.model.enums.Gender;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class ChildDtoCreatingUtil {

    public static List<ChildDto> getTestChildren(int year) {
        return List.of(
                getChildDto(LocalDate.of(year, 3, 3), Gender.MALE),
                getChildDto(LocalDate.of(year, 4, 4), Gender.FEMALE)
        );
    }

    public static List<ChildDto> generateTestChildren(int quantity) {
        List<ChildDto> childDtoList = new ArrayList<>();
        IntStream.range(0, quantity).forEach(i ->
                childDtoList.add(getChildDto(LocalDate.of(2010, 1, 1), Gender.MALE)));
        return childDtoList;
    }

    public static ChildDto getChildDto(LocalDate birthDate, Gender sex) {
        return ChildDto.builder()
                .birthDate(birthDate)
                .sex(sex)
                .build();
    }
}
