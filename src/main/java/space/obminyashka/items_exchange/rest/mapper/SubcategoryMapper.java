package space.obminyashka.items_exchange.rest.mapper;

import org.mapstruct.Mapper;

import org.mapstruct.Mapping;
import space.obminyashka.items_exchange.rest.response.SubcategoryView;
import space.obminyashka.items_exchange.repository.model.Subcategory;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubcategoryMapper {
    SubcategoryView toDto(Subcategory model);

    @Mapping(target = "category", ignore = true)
    @Mapping(target = "advertisements", ignore = true)
    Subcategory toModel(SubcategoryView dto);

    List<SubcategoryView> toDtoList(List<Subcategory> modelList);
    List<Subcategory> toModelList(List<SubcategoryView> dtoList);
}
