package space.obminyashka.items_exchange.mapper;

import org.mapstruct.Mapper;

import space.obminyashka.items_exchange.dto.SubcategoryDto;
import space.obminyashka.items_exchange.model.Subcategory;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubcategoryMapper {
    SubcategoryDto toDto(Subcategory model);

    //Unmapped target properties: "category, advertisements".
    Subcategory toModel(SubcategoryDto dto);

    List<SubcategoryDto> toDTOList(List<Subcategory> modelList);
    List<Subcategory> toModelList(List<SubcategoryDto> dtoList);
}
