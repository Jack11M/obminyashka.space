package com.hillel.items_exchange.mapper;

import com.hillel.items_exchange.dto.ChildDto;
import com.hillel.items_exchange.model.Child;
import com.hillel.items_exchange.model.ChildGender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ChildMapperTest {

    public static final long EXISTED_ID = 1L;
    public static final String MALE = "male";

    @Autowired
    private ChildMapper childMapper;

    private Child child;
    private ChildDto childDto;

    @BeforeEach
    void setUp() {
        child = createChild();
        childDto = createChildDto();
    }

    @Test
    @DisplayName("Expect childDto is mapped properly")
    void testConvertChildDto_ShouldMapCorrectChild() {
        //when
        ChildDto result = childMapper.convertChild(child);
        //then
        assertAll(
                () -> assertEquals(result.getId(), childDto.getId()),
                () -> assertEquals(result.getBirthDate(), childDto.getBirthDate()),
                () -> assertEquals(result.getSex(), childDto.getSex())
        );
    }

    @Test
    @DisplayName("Expect child is mapped properly")
    void testConvertChild_ShouldMapCorrectChildDto() {
        //when
        Child result = childMapper.convertChildDto(childDto);
        //then
        assertAll(
                () -> assertEquals(result.getId(), child.getId()),
                () -> assertEquals(result.getBirthDate(), child.getBirthDate()),
                () -> assertEquals(result.getSex(), child.getSex())
        );
    }

    private Child createChild() {
        final Child child = new Child();
        child.setId(EXISTED_ID);
        child.setBirthDate(LocalDate.of(1900, 1, 1));
        child.setSex(MALE);

        return child;
    }

    private ChildDto createChildDto() {
        final ChildDto childDto = new ChildDto();
        childDto.setId(child.getId());
        childDto.setBirthDate(child.getBirthDate());
        childDto.setSex(ChildGender.MALE);

        return childDto;
    }
}