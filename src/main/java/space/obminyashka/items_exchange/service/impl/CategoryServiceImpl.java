package space.obminyashka.items_exchange.service.impl;

import space.obminyashka.items_exchange.dao.CategoryRepository;
import space.obminyashka.items_exchange.dto.CategoryDto;
import space.obminyashka.items_exchange.dto.SubcategoryDto;
import space.obminyashka.items_exchange.mapper.CategoryMapper;
import space.obminyashka.items_exchange.model.Category;
import space.obminyashka.items_exchange.model.Subcategory;
import space.obminyashka.items_exchange.model.enums.Size;
import space.obminyashka.items_exchange.service.CategoryService;
import space.obminyashka.items_exchange.service.SubcategoryService;
import java.util.*;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final SubcategoryService subcategoryService;
    private final CategoryMapper categoryMapper;

    @Override
    public List<String> findAllCategoryNames() {
        return categoryRepository.findAllCategoriesNames();
    }

    @Override
    public List<CategoryDto> findAllCategoryDtos() {
        return categoryMapper.toDtoList(categoryRepository.findAll());
    }

    @Override
    public Optional<CategoryDto> findCategoryDtoById(long id) {
        return categoryRepository.findById(id)
                .map(categoryMapper::toDto);
    }

    @Override
    public CategoryDto saveCategoryWithSubcategories(CategoryDto categoryDto) {
        final Category category = saveCategory(categoryDto);
        return categoryMapper.toDto(category);
    }

    @Override
    public void removeById(long categoryId) {
        categoryRepository.deleteById(categoryId);
    }

    @Override
    public boolean isCategoryExistsById(long id) {
        return categoryRepository.existsById(id);
    }

    @Override
    public boolean isCategoryDtoDeletable(long categoryId) {
        return categoryRepository.findById(categoryId)
                .map(category -> category.getSubcategories().stream()
                        .map(Subcategory::getAdvertisements)
                        .allMatch(Collection::isEmpty))
                .orElse(false);
    }

    @Override
    public boolean isCategoryDtoUpdatable(CategoryDto categoryDto) {
        return isCategoryExistsByIdAndNameOrNotExistsByName(categoryDto.getId(),
                categoryDto.getName()) && isSubcategoriesExist(categoryDto);
    }

    @Override
    public boolean isCategoryDtoValidForCreating(CategoryDto categoryDto) {
        return isCategoryNameHasNotDuplicate(categoryDto.getName())
                && categoryDto.getSubcategories().stream().allMatch(this::isSubcategoryIdEqualsZero);
    }

    @Override
    public List<String> findSizesForCategory(int id) {
        return switch (id) {
            case 1 -> Arrays.stream(Size.Clothing.values())
                    .map(Size.Clothing::getRange)
                    .toList();
            case 2 -> Arrays.stream(Size.Shoes.values())
                    .map(Size.Shoes::getLength)
                    .map(String::valueOf)
                    .toList();
            default -> Collections.emptyList();
        };
    }

    private boolean isSubcategoryIdEqualsZero(SubcategoryDto subcategoryDto) {
        return subcategoryDto.getId() == 0L;
    }

    private boolean isCategoryNameHasNotDuplicate(String name) {
        return !categoryRepository.existsByNameIgnoreCase(name);
    }

    private boolean isSubcategoriesExist(CategoryDto categoryDto) {
        final List<Long> existingSubcategoriesIds = subcategoryService.findAllSubcategoryIds();

        return categoryDto.getSubcategories().stream()
                .filter(subcategoryDto -> !isSubcategoryIdEqualsZero(subcategoryDto))
                .allMatch(subcategoryDto -> existingSubcategoriesIds.contains(subcategoryDto.getId()));
    }

    private boolean isCategoryExistsByIdAndNameOrNotExistsByName(long categoryId, String categoryName) {
        return categoryRepository.existsByIdAndNameIgnoreCase(categoryId, categoryName)
                || (categoryRepository.existsById(categoryId) && isCategoryNameHasNotDuplicate(categoryName));
    }

    private Category saveCategory(CategoryDto categoryDto) {
        final Category category = categoryMapper.toModel(categoryDto);
        category.getSubcategories().forEach(subcategory -> subcategory.setCategory(category));
        return categoryRepository.saveAndFlush(category);
    }
}
