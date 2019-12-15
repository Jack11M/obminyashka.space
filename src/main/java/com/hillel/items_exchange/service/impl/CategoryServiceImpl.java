package com.hillel.items_exchange.service.impl;

import com.hillel.items_exchange.dao.CategoryRepository;
import com.hillel.items_exchange.dao.SubcategoryRepository;
import com.hillel.items_exchange.dto.CategoryControllerDto;
import com.hillel.items_exchange.dto.SubcategoryControllerDto;
import com.hillel.items_exchange.mapper.CategoryMapper;
import com.hillel.items_exchange.model.Category;
import com.hillel.items_exchange.model.Subcategory;
import com.hillel.items_exchange.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final SubcategoryRepository subcategoryRepository;
    private final ModelMapper modelMapper;
    private final CategoryMapper categoryMapper;

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
        if (isCategoryDtoCreatable(categoryControllerDto)) {
            Category newCategory = categoryMapper.categoryControllerDtoToNewCategory(categoryControllerDto);
            categoryRepository.save(newCategory);
            log.info("IN CategoryServiceImpl (addNewCategory): the category has been created: {}", newCategory);
            return Optional.of(categoryMapper.categoryToCategoryDto(newCategory));
        } else {
            log.warn("IN CategoryServiceImpl (addNewCategory): the category can not be created");
            return Optional.empty();
        }
    }

    @Override
    public Optional<CategoryControllerDto> updateCategory(CategoryControllerDto categoryControllerDto) {
        if (isCategoryDtoIdValid(categoryControllerDto.getId())
                && !isSubcategoryDtoIdInvalid(categoryControllerDto.getSubcategories())) {
            Optional<Category> updatedCategory = categoryRepository.findById(categoryControllerDto.getId());
            if (updatedCategory.isPresent()) {
                updatedCategory = Optional.of(categoryMapper.categoryControllerDtoToUpdatedCategory(categoryControllerDto,
                        updatedCategory.get()));
                categoryRepository.save(updatedCategory.get());
                log.info("IN CategoryServiceImpl (updateCategory): the category has been updated: {}", updatedCategory.get());
                return Optional.of(mapCategoryToDto(updatedCategory.get()));
            } else {
                log.warn("IN CategoryServiceImpl (updateCategory): the category can not be updated");
                return Optional.empty();
            }
        } else {
            log.warn("IN CategoryServiceImpl (updateCategory): the category can not be updated");
            return Optional.empty();
        }
    }

    @Override
    public boolean removeCategoryById(Long categoryId) {
        if (isCategoryDtoIdValid(categoryId)) {
            Optional<Category> category = categoryRepository.findById(categoryId);
            if (category.isPresent() && category.get().getSubcategories().stream()
                    .anyMatch(subcategory -> isSubcategoryDeletable(subcategory.getId()))) {
                categoryRepository.deleteById(categoryId);
                log.info("IN CategoryServiceImpl (removeCategoryById): the category with id {} has been deleted successfully",
                        categoryId);
                return true;
            } else {
                log.warn("IN CategoryServiceImpl (removeCategoryById): the category with id {} has not been deleted",
                        categoryId);
                return false;
            }
        } else {
            log.warn("IN CategoryServiceImpl (removeCategoryById): the category with id {} has not been deleted",
                    categoryId);
            return false;
        }
    }

    @Override
    public boolean removeSubcategoryById(Long subcategoryId) {
        if (isSubcategoryDeletable(subcategoryId)) {
            subcategoryRepository.deleteById(subcategoryId);
            log.info("IN CategoryServiceImpl (removeSubcategoryById): the subcategory with id {} has been deleted successfully",
                    subcategoryId);
            return true;
        } else {
            log.warn("IN CategoryServiceImpl (removeSubcategoryById): the subcategory with id {} has not been deleted",
                    subcategoryId);
            return false;
        }
    }

    private boolean isCategoryDtoIdValid(Long id) {
        if (!categoryRepository.existsById(id)) {
            log.warn("IN CategoryServiceImpl (isCategoryDtoIdInvalid): the category does not exist by this id: {}", id);
            return false;
        }
        return !isIdInvalid(id);
    }

    private boolean isIdInvalid(Long id) {
        if (id == null || id <= 0L) {
            log.warn("IN CategoryServiceImpl (isIdInvalid): this id is invalid {}", id);
            return true;
        }
        return false;
    }

    private boolean isSubcategoryDtoIdInvalid(List<SubcategoryControllerDto> subcategoriesDtos) {
        return subcategoriesDtos.stream()
                .filter(subcategoryDto -> subcategoryDto.getId() != null)
                .map(SubcategoryControllerDto::getId)
                .anyMatch(this::isSubcategoryDtoIdInvalid);
    }

    private boolean isSubcategoryDtoIdInvalid(Long subcategoryId) {
        if (isIdInvalid(subcategoryId) || !subcategoryRepository.existsById(subcategoryId)) {
            log.warn("IN CategoryServiceImpl (isSubcategoryDtoIdInvalid): the subcategory does not exist by given id {}",
                    subcategoryId);
            return true;
        }
        return false;
    }

    private boolean isSubcategoryDeletable(Long subcategoryDtoId) {
        if (!isSubcategoryDtoIdInvalid(subcategoryDtoId)) {
            Optional<Subcategory> subcategory = subcategoryRepository.findById(subcategoryDtoId);
            if (subcategory.isPresent()) {
                if (subcategory.get().getProducts().isEmpty()) {
                    return true;
                } else {
                    log.warn("IN CategoryServiceImpl (isSubcategoryDeletable): the subcategory with id {} can not be deleted" +
                                    " because it has products",
                            subcategoryDtoId);
                    return false;
                }
            } else {
                log.warn("IN CategoryServiceImpl (isSubcategoryDeletable): the subcategory does not exist by given id {}",
                        subcategoryDtoId);
                return false;
            }
        } else {
            log.warn("IN CategoryServiceImpl (isSubcategoryDeletable): the subcategory does not exist by given id {}",
                    subcategoryDtoId);
            return false;
        }
    }

    private boolean isCategoryDtoCreatable(CategoryControllerDto categoryDto) {
        if (categoryDto.getId() != null) {
            log.warn("IN CategoryServiceImpl (isCategoryCreatable): the new category must not have id");
            return false;
        }
        if (categoryDto.getSubcategories().stream()
                .anyMatch(subcategoryDto -> subcategoryDto.getId() != null)) {
            log.warn("IN CategoryServiceImpl (isCategoryCreatable): the new category must not have id of the subcategories");
            return false;
        }
        if (findAllCategoryNames().contains(categoryDto.getName())) {
            log.warn("IN CategoryServiceImpl (isCategoryCreatable): the new category must not have a name like the existing category: {}",
                    categoryDto.getName());
            return false;
        }
        return true;
    }

    private CategoryControllerDto mapCategoryToDto(Category category) {
        return modelMapper.map(category, CategoryControllerDto.class);
    }
}
