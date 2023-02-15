package space.obminyashka.items_exchange.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import space.obminyashka.items_exchange.api.ApiKey;
import space.obminyashka.items_exchange.dto.CategoryDto;
import space.obminyashka.items_exchange.exception.BadRequestException;
import space.obminyashka.items_exchange.exception.InvalidDtoException;
import space.obminyashka.items_exchange.service.CategoryService;
import space.obminyashka.items_exchange.util.ResponseMessagesHandler;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

import static space.obminyashka.items_exchange.config.SecurityConfig.HAS_ROLE_ADMIN;
import static space.obminyashka.items_exchange.util.MessageSourceUtil.getExceptionMessageSourceWithId;
import static space.obminyashka.items_exchange.util.MessageSourceUtil.getMessageSource;

@RestController
@Api(tags = "Category")
@RequiredArgsConstructor
@Validated
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping(ApiKey.CATEGORY_NAMES)
    @ApiOperation(value = "Get all names of existing categories.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "NOT FOUND")})
    public ResponseEntity<List<String>> getAllCategoriesNames() {
        List<String> categoriesNames = categoryService.findAllCategoryNames();
        return categoriesNames.isEmpty() ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(categoriesNames, HttpStatus.OK);
    }

    @GetMapping(ApiKey.CATEGORY_ALL)
    @ApiOperation(value = "Get all existing categories.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "NOT FOUND")})
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        List<CategoryDto> categories = categoryService.findAllCategoryDtos();
        return categories.isEmpty() ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping(ApiKey.CATEGORY_ID)
    @ApiOperation(value = "Get a category by its ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "BAD REQUEST"),
            @ApiResponse(code = 404, message = "NOT FOUND")})
    public ResponseEntity<CategoryDto> getCategoryById(@Positive(message = ResponseMessagesHandler.ValidationMessage.INVALID_NOT_POSITIVE_ID)
                                                       @PathVariable("category_id") long id) {

        return ResponseEntity.of(categoryService.findCategoryDtoById(id));
    }

    @GetMapping(ApiKey.CATEGORY_SIZES)
    @ApiOperation(value = "Get a category sizes by its ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "BAD REQUEST"),
            @ApiResponse(code = 404, message = "NOT FOUND")})
    public ResponseEntity<List<String>> getCategorySizesById(@Positive(message = ResponseMessagesHandler.ValidationMessage.INVALID_NOT_POSITIVE_ID)
                                                             @PathVariable("category_id") int id) throws BadRequestException {
        var sizes = categoryService.findSizesForCategory(id);

        if (sizes.isEmpty()) {
            throw new BadRequestException(getMessageSource(ResponseMessagesHandler.ValidationMessage.INVALID_CATEGORY_SIZES_ID));
        }

        return ResponseEntity.ok(sizes);
    }

    @PreAuthorize(HAS_ROLE_ADMIN)
    @PostMapping(ApiKey.CATEGORY)
    @ApiOperation(value = "Create a new category", notes = "ADMIN ONLY")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "CREATED"),
            @ApiResponse(code = 400, message = "BAD REQUEST"),
            @ApiResponse(code = 403, message = "FORBIDDEN")})
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto createCategory(@Valid @RequestBody CategoryDto categoryDto)
            throws InvalidDtoException {
        if (categoryService.isCategoryDtoValidForCreating(categoryDto)) {
            return categoryService.saveCategoryWithSubcategories(categoryDto);
        }

        throw new InvalidDtoException(getMessageSource(
                ResponseMessagesHandler.ValidationMessage.INVALID_NEW_CATEGORY_DTO));
    }

    @PreAuthorize(HAS_ROLE_ADMIN)
    @PutMapping(ApiKey.CATEGORY_ID)
    @ApiOperation(value = "Update an existed category", notes = "ADMIN ONLY")
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "ACCEPTED"),
            @ApiResponse(code = 400, message = "BAD REQUEST"),
            @ApiResponse(code = 403, message = "FORBIDDEN")})
    @ResponseStatus(HttpStatus.ACCEPTED)
    public CategoryDto updateCategory(@Positive(message = ResponseMessagesHandler.ValidationMessage.INVALID_NOT_POSITIVE_ID) @PathVariable("category_id") long id,
                                      @Valid @RequestBody CategoryDto categoryDto) {
        categoryDto.setId(id);
        if (categoryService.isCategoryDtoUpdatable(categoryDto)) {
            return categoryService.saveCategoryWithSubcategories(categoryDto);
        }

        throw new IllegalIdentifierException(getMessageSource(
                ResponseMessagesHandler.ValidationMessage.INVALID_UPDATED_CATEGORY_DTO));
    }

    @PreAuthorize(HAS_ROLE_ADMIN)
    @DeleteMapping(ApiKey.CATEGORY_ID)
    @ApiOperation(value = "Delete an existed category", notes = "ADMIN ONLY")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "BAD REQUEST"),
            @ApiResponse(code = 403, message = "FORBIDDEN")})
    @ResponseStatus(HttpStatus.OK)
    public void deleteCategoryById(@PathVariable("category_id") @Positive(message = ResponseMessagesHandler.ValidationMessage.INVALID_NOT_POSITIVE_ID) long id)
            throws InvalidDtoException {

        if (!categoryService.isCategoryDtoDeletable(id)) {
            throw new InvalidDtoException(getExceptionMessageSourceWithId(
                    id, ResponseMessagesHandler.ValidationMessage.CATEGORY_NOT_DELETABLE));
        }
        categoryService.removeById(id);
    }
}
