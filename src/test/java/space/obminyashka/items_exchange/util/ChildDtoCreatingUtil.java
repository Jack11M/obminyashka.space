package space.obminyashka.items_exchange.util;

import space.obminyashka.items_exchange.dto.ChildDto;
import space.obminyashka.items_exchange.model.enums.Gender;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class ChildDtoCreatingUtil {

    public static List<ChildDto> getTestChildren(long maleId, long femaleId, int year) {
        return List.of(
                getChildDto(maleId, LocalDate.of(year, 3, 3), Gender.MALE),
                getChildDto(femaleId, LocalDate.of(year, 4, 4), Gender.FEMALE)
        );
    }

    public static List<ChildDto> generateTestChildren(int quantity) {
        List<ChildDto> childDtoList = new ArrayList<>();
        IntStream.range(0, quantity).forEach(i ->
                childDtoList.add(getChildDto(0L, LocalDate.of(2010, 1, 1), Gender.MALE)));
        return childDtoList;
    }

    public static ChildDto getChildDto(long id, LocalDate birthDate, Gender sex) {
        return ChildDto.builder()
                .id(id)
                .birthDate(birthDate)
                .sex(sex)
                .build();
    }
}
