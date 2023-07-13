package space.obminyashka.items_exchange.rest.mapper;

import org.mapstruct.Mapper;
import space.obminyashka.items_exchange.rest.dto.CategoryDto;
import space.obminyashka.items_exchange.rest.response.CategoryNameView;
import space.obminyashka.items_exchange.repository.model.Category;

import java.util.List;

@Mapper(componentModel = "spring", uses = SubcategoryMapper.class)
public interface CategoryMapper {
    Category toModel(CategoryDto dto);
    CategoryDto toDto(Category model);
    CategoryNameView toNameDto(Category model);

    List<CategoryDto> toDtoList(List<Category> modelList);
    List<Category> toModelList(List<CategoryDto> dtoList);
}
