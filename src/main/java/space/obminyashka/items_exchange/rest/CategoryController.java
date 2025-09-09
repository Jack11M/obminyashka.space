package space.obminyashka.items_exchange.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import space.obminyashka.items_exchange.rest.api.ApiKey;
import space.obminyashka.items_exchange.rest.dto.CategoryDto;
import space.obminyashka.items_exchange.rest.exception.DataConflictException;
import space.obminyashka.items_exchange.rest.exception.bad_request.BadRequestException;
import space.obminyashka.items_exchange.rest.exception.bad_request.InvalidDtoException;
import space.obminyashka.items_exchange.rest.exception.not_found.CategorySizeNotFoundException;
import space.obminyashka.items_exchange.rest.exception.not_found.EntityIdNotFoundException;
import space.obminyashka.items_exchange.rest.response.message.ResponseMessagesHandler;
import space.obminyashka.items_exchange.service.CategoryService;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static space.obminyashka.items_exchange.config.SecurityConfig.HAS_ROLE_ADMIN;
import static space.obminyashka.items_exchange.rest.response.message.MessageSourceProxy.getMessageSource;
import static space.obminyashka.items_exchange.rest.response.message.MessageSourceProxy.getParametrizedMessageSource;
import static space.obminyashka.items_exchange.rest.response.message.ResponseMessagesHandler.ValidationMessage.*;

@RestController
@Tag(name = "Category")
@RequiredArgsConstructor
@Validated
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping(value = ApiKey.CATEGORY_NAMES, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get all names of existing categories.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND")})
    public ResponseEntity<List<String>> getAllCategoriesNames() {
        List<String> categoriesNames = categoryService.findAllCategoryNames();
        return categoriesNames.isEmpty() ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(categoriesNames, HttpStatus.OK);
    }

    @GetMapping(value = ApiKey.CATEGORY_ALL, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get all existing categories.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND")})
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        List<CategoryDto> categories = categoryService.findAllCategoryDtos();
        return categories.isEmpty() ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping(value = ApiKey.CATEGORY_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get a category by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND")})
    public ResponseEntity<CategoryDto> getCategoryById(@Positive(message = "{" + INVALID_NOT_POSITIVE_ID + "}")
                                                       @PathVariable("category_id") long id) {

        return ResponseEntity.of(categoryService.findCategoryDtoById(id));
    }

    @GetMapping(value = ApiKey.CATEGORY_SIZES, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get a category sizes by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND")})
    public List<String> getCategorySizesById(@Positive(message = "{" + INVALID_NOT_POSITIVE_ID + "}")
                                             @PathVariable("category_id") long id) throws CategorySizeNotFoundException {
        return Optional.of(categoryService.findSizesForCategory(id))
                .filter(Predicate.not(List::isEmpty))
                .orElseThrow(() -> new CategorySizeNotFoundException(
                        getMessageSource(ResponseMessagesHandler.ValidationMessage.INVALID_CATEGORY_SIZES_ID)));
    }

    @PreAuthorize(HAS_ROLE_ADMIN)
    @PostMapping(value = ApiKey.CATEGORY, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create a new category", description = "ADMIN ONLY")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "CREATED"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN")})
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
    @PutMapping(value = ApiKey.CATEGORY_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update an existed category", description = "ADMIN ONLY")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "ACCEPTED"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN")})
    @ResponseStatus(HttpStatus.ACCEPTED)
    public CategoryDto updateCategory(@Positive(message = "{" + INVALID_NOT_POSITIVE_ID + "}") @PathVariable("category_id") long id,
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
    @Operation(summary = "Delete an existed category", description = "ADMIN ONLY")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN")})
    @ResponseStatus(HttpStatus.OK)
    public void deleteCategoryById(@Positive(message = "{" + INVALID_NOT_POSITIVE_ID + "}")
                                   @PathVariable("category_id") long id) throws DataConflictException {
        if (!categoryService.isCategoryValid(id)) {
            throw new BadRequestException(getParametrizedMessageSource(INVALID_CATEGORY_ID, id));
        }
        if (!categoryService.isCategoryExistsById(id)) {
            throw new EntityIdNotFoundException(getParametrizedMessageSource(INVALID_CATEGORY_ID, id));
        }
        if (!categoryService.isCategoryDeletable(id)) {
            throw new DataConflictException(getParametrizedMessageSource(CATEGORY_NOT_DELETABLE, id));
        }
        categoryService.removeById(id);
    }
}
