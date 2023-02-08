package space.obminyashka.items_exchange.mapper;

import org.mapstruct.Mapper;
import space.obminyashka.items_exchange.dto.CategoryDto;
import space.obminyashka.items_exchange.dto.CategoryNameDto;
import space.obminyashka.items_exchange.model.Category;

import java.util.List;

@Mapper(componentModel = "spring", uses = SubcategoryMapper.class)
public interface CategoryMapper {
    Category toModel(CategoryDto dto);
    CategoryDto toDto(Category model);
    CategoryNameDto toNameDto(Category model);

    List<CategoryDto> toDtoList(List<Category> modelList);
    List<Category> toModelList(List<CategoryDto> dtoList);
}
