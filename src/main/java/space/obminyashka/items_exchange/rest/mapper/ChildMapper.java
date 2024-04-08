package space.obminyashka.items_exchange.rest.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import space.obminyashka.items_exchange.repository.model.Child;
import space.obminyashka.items_exchange.rest.dto.ChildDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ChildMapper {
    ChildDto toDto(Child model);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    Child toModel(ChildDto dto);

    List<ChildDto> toDtoList(List<Child> modelList);
    List<Child> toModelList(List<ChildDto> dtoList);
}
