package space.obminyashka.items_exchange.mapper;

import org.mapstruct.Mapper;
import space.obminyashka.items_exchange.dto.ChildDto;
import space.obminyashka.items_exchange.model.Child;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ChildMapper {
    ChildDto toDto(Child model);

    //Unmapped target properties: "id, user".
    Child toModel(ChildDto dto);

    List<ChildDto> toDtoList(List<Child> modelList);
    List<Child> toModelList(List<ChildDto> dtoList);
}
