package com.hillel.items_exchange.service.impl;

import com.hillel.items_exchange.dao.CategoryRepository;
import com.hillel.items_exchange.dto.CategoryDto;
import com.hillel.items_exchange.dto.SubcategoryDto;
import com.hillel.items_exchange.model.Category;
import com.hillel.items_exchange.model.Subcategory;
import com.hillel.items_exchange.service.CategoryService;
import com.hillel.items_exchange.service.SubcategoryService;
import java.util.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import static com.hillel.items_exchange.mapper.UtilMapper.*;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final SubcategoryService subcategoryService;
    private final ModelMapper modelMapper;

    @Override
    public List<String> findAllCategoryNames() {
        return categoryRepository.findAllCategoriesNames();
    }

    @Override
    public List<CategoryDto> findAllCategoryDto() {
        return new ArrayList<>(convertAllTo(categoryRepository.findAll(),
                CategoryDto.class, ArrayList::new));
    }

    @Override
    public Optional<CategoryDto> findCategoryDtoById(long id) {
        return categoryRepository.findById(id)
                .map(category -> convertTo(category, CategoryDto.class));
    }

    @Override
    public CategoryDto create(CategoryDto categoryDto) {
        final Category savedCategory = saveCategory(categoryDto);
        return convertTo(savedCategory, CategoryDto.class);
    }

    @Override
    public CategoryDto update(CategoryDto categoryDto) {
        final Category updatedCategory = saveCategory(categoryDto);
        return convertTo(updatedCategory, CategoryDto.class);
    }

    @Override
    public void removeById(long categoryId) {
        categoryRepository.deleteById(categoryId);
    }

    @Override
    public boolean isCategoryDtoDeletable(long categoryId) {
        return isCategoryExistsById(categoryId) && isInternalSubcategoriesHaveNotProducts(categoryId);
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

    private boolean isSubcategoryIdEqualsZero(SubcategoryDto subcategoryDto) {
        return subcategoryDto.getId() == 0L;
    }

    private boolean isCategoryExistsById(long categoryId) {
        return categoryRepository.existsById(categoryId);
    }

    private boolean isCategoryNameHasNotDuplicate(String name) {
        return !categoryRepository.existsByNameIgnoreCase(name);
    }

    private boolean isSubcategoriesExist(CategoryDto categoryDto) {
        return categoryDto.getSubcategories().stream()
                .filter(subcategoryDto -> !isSubcategoryIdEqualsZero(subcategoryDto))
                .allMatch(subcategoryDto -> subcategoryService.isSubcategoryExistsById(subcategoryDto.getId()));
    }

    private boolean isInternalSubcategoriesHaveNotProducts(long categoryId) {
        return categoryRepository.findById(categoryId)
                .map(category -> category.getSubcategories().stream()
                        .map(Subcategory::getProducts)
                        .allMatch(Collection::isEmpty))
                .orElse(false);
    }

    private boolean isCategoryExistsByIdAndNameOrNotExistsByName(long categoryId, String categoryName) {
        return categoryRepository.existsByIdAndNameIgnoreCase(categoryId, categoryName)
                || (categoryRepository.existsById(categoryId) && isCategoryNameHasNotDuplicate(categoryName));
    }

    private Category saveCategory(CategoryDto categoryDto) {
        final Category category = convertTo(categoryDto, Category.class);
        List<Subcategory> subcategories = mapBy(categoryDto.getSubcategories(),
                subcategoryDto -> setCategoryToSubcategory(category, subcategoryDto));
        category.setSubcategories(subcategories);
        return categoryRepository.saveAndFlush(category);
    }

    private Subcategory setCategoryToSubcategory(Category category, SubcategoryDto subcategoryDto) {
        final Subcategory subcategory = modelMapper.map(subcategoryDto, Subcategory.class);
        subcategory.setCategory(category);
        return subcategory;
    }
}
