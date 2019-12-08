package com.hillel.items_exchange.service.impl;

import com.hillel.items_exchange.dao.CategoryRepository;
import com.hillel.items_exchange.dao.SubcategoryRepository;
import com.hillel.items_exchange.dto.CategoryControllerDto;
import com.hillel.items_exchange.dto.SubcategoryControllerDto;
import com.hillel.items_exchange.model.Category;
import com.hillel.items_exchange.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final SubcategoryRepository subcategoryRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<String> findAllCategoryNames() {
        List<String> categoryNames = categoryRepository.findAllCategoriesNames();

        if (categoryNames.isEmpty()) {
            log.warn("IN CategoryServiceImpl (findAllCategoryNames): there are not categories names");
            return Collections.emptyList();
        } else {
            log.info("IN CategoryServiceImpl (findAllCategoryNames): there are these categories: {}", categoryNames);
            return categoryNames;
        }
    }

    @Override
    public List<CategoryControllerDto> findAllCategories() {
        List<CategoryControllerDto> allCategories = modelMapper.map(categoryRepository.findAll(),
                new TypeToken<List<CategoryControllerDto>>() {
                }.getType());

        if (allCategories.isEmpty()) {
            log.warn("IN CategoryServiceImpl (findAllCategories): there are not categories");
            return Collections.emptyList();
        } else {
            log.info("IN CategoryServiceImpl (findAllCategories): there are these categories have been found: {}",
                    allCategories);
            return modelMapper.map(categoryRepository.findAll(), new TypeToken<List<CategoryControllerDto>>() {
            }.getType());
        }
    }

    @Override
    public Optional<CategoryControllerDto> findCategoryById(Long id) {
        if (categoryRepository.findById(id).isPresent()) {
            log.info("IN CategoryServiceImpl (findCategoryById): the category has been found by id {}", id);
        } else {
            log.warn("IN CategoryServiceImpl (CategoryById): has not been found by id {}", id);
        }
        return categoryRepository.findById(id).map(this::mapCategoryToDto);
    }

    @Override
    public List<String> findSubcategoryNamesByCategoryId(Long categoryId) {
        List<String> subcategoryNames = subcategoryRepository.findSubcategoriesNamesByCategory(categoryId);

        if (subcategoryNames.isEmpty()) {
            log.warn("IN CategoryServiceImpl (findSubcategoryNamesByCategoryId): there are not subcategories " +
                    "in the category with id: {}", categoryId);
            return Collections.emptyList();
        } else {
            log.info("IN CategoryServiceImpl (findSubcategoryNamesByCategoryId): there are these subcategories: {} " +
                    "in the category with id: {}", subcategoryNames, categoryId);
            return subcategoryNames;
        }
    }

    @Override
    public Optional<CategoryControllerDto> addNewCategory(CategoryControllerDto categoryControllerDto) {
        if (categoryControllerDto != null && isCategoryDtoCreatable(categoryControllerDto)) {
            Category newCategory = categoryRepository.save(mapDtoToCategory(categoryControllerDto));
            log.info("IN CategoryServiceImpl (addNewCategory): the category has been created: {}", newCategory);
            return Optional.of(mapCategoryToDto(newCategory));
        } else {
            log.warn("IN CategoryServiceImpl (addNewCategory): the category can not be created");
            return Optional.empty();
        }
    }

    @Override
    public Optional<CategoryControllerDto> updateCategory(CategoryControllerDto categoryControllerDto) {
        if (categoryControllerDto != null && isCategoryIdInvalid(categoryControllerDto.getId())) {
            Category updatedCategory = categoryRepository.save(mapDtoToCategory(categoryControllerDto));
            log.info("IN CategoryServiceImpl (updateCategory): the category has been updated: {}", updatedCategory);
            return Optional.of(mapCategoryToDto(updatedCategory));
        } else {
            log.warn("IN CategoryServiceImpl (updateCategory): the category can not be updated");
            return Optional.empty();
        }
    }

    private Boolean isCategoryIdInvalid(Long id) {
        if (id == null || id <= 0L) {
            log.warn("IN CategoryServiceImpl (isCategoryIdInvalid): this id is invalid {}", id);
            return true;
        }
        if (!categoryRepository.existsById(id)) {
            log.info("IN CategoryServiceImpl (isCategoryIdInvalid): the category does not exist by this id: {}", id);
            return true;
        }
        return false;
    }

    private Boolean isCategoryDtoCreatable(CategoryControllerDto categoryDto) {
        if (categoryDto.getId() != null) {
            return false;
        }
        if (findSubcategoryNamesByCategoryId(categoryDto.getId()).stream()
                .anyMatch(subcategoryDto -> categoryDto.getSubcategories().stream()
                        .map(SubcategoryControllerDto::getName)
                        .collect(Collectors.toList())
                        .contains(subcategoryDto))) {
            return false;
        }
        if (categoryDto.getSubcategories().stream()
                .anyMatch(subcategoryDto -> subcategoryDto.getId() != null)) {
            return false;
        }
        return !findAllCategoryNames().contains(categoryDto.getName());
    }

    private Category mapDtoToCategory(CategoryControllerDto categoryControllerDto) {
        return modelMapper.map(categoryControllerDto, Category.class);
    }

    private CategoryControllerDto mapCategoryToDto(Category category) {
        return modelMapper.map(category, CategoryControllerDto.class);
    }
}
