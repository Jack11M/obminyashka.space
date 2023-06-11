package space.obminyashka.items_exchange.mapper;

import org.mapstruct.Mapper;

import org.mapstruct.Mapping;
import space.obminyashka.items_exchange.dto.SubcategoryDto;
import space.obminyashka.items_exchange.model.Subcategory;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubcategoryMapper {
    SubcategoryDto toDto(Subcategory model);

    @Mapping(target = "category", ignore = true)
    @Mapping(target = "advertisements", ignore = true)
    Subcategory toModel(SubcategoryDto dto);

    List<SubcategoryDto> toDtoList(List<Subcategory> modelList);
    List<Subcategory> toModelList(List<SubcategoryDto> dtoList);
}
