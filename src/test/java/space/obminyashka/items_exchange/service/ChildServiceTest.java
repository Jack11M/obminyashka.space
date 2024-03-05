package space.obminyashka.items_exchange.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import space.obminyashka.items_exchange.repository.ChildRepository;
import space.obminyashka.items_exchange.rest.dto.ChildDto;
import space.obminyashka.items_exchange.rest.mapper.ChildMapper;
import space.obminyashka.items_exchange.repository.model.Child;
import space.obminyashka.items_exchange.repository.enums.Gender;
import space.obminyashka.items_exchange.service.impl.ChildServiceImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChildServiceTest {
    @Mock
    private ChildMapper childMapper;
    @Mock
    private ChildRepository childRepository;
    @InjectMocks
    private ChildServiceImpl childService;

    @Test
    void updateChildren_whenGetNewChildrenForUser_shouldWorkCorrectly() {
        // Arrange
        String username = "testuser";
        List<ChildDto> childrenDtoToUpdate = List.of(new ChildDto(Gender.MALE, LocalDate.now()));

        List<Child> childrenToSave = List.of(new Child(UUID.randomUUID(), Gender.MALE, LocalDate.now(), null));
        when(childMapper.toModelList(childrenDtoToUpdate)).thenReturn(childrenToSave);

        // Act
        List<ChildDto> result = childService.updateChildren(username, childrenDtoToUpdate);

        // Assert
        assertAll(
                () -> verify(childRepository).deleteByUser_Username(username),
                () -> verify(childRepository, times(childrenDtoToUpdate.size())).createChildrenByUsername(any(), eq(username)),
                () -> assertEquals(childrenDtoToUpdate, result));
    }
}
