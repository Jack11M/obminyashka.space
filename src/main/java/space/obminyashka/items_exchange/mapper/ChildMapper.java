package space.obminyashka.items_exchange.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import space.obminyashka.items_exchange.dto.ChildDto;
import space.obminyashka.items_exchange.model.Child;

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
