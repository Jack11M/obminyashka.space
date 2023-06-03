package space.obminyashka.items_exchange.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import space.obminyashka.items_exchange.dto.PhoneDto;
import space.obminyashka.items_exchange.model.Phone;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface PhoneMapper {
    PhoneDto toDto(Phone model);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    Phone toModel(PhoneDto dto);

    List<PhoneDto> toDtoList(List<Phone> modelList);
    List<Phone> toModelList(List<PhoneDto> dtoList);

    Set<PhoneDto> toDtoSet(Set<Phone> modelSet);
    Set<Phone> toModelSet(Set<PhoneDto> dtoSet);
}
