package space.obminyashka.items_exchange.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import space.obminyashka.items_exchange.api.ApiKey;
import space.obminyashka.items_exchange.exception.InvalidDtoException;
import space.obminyashka.items_exchange.service.SubcategoryService;
import space.obminyashka.items_exchange.util.ResponseMessagesHandler;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

import static space.obminyashka.items_exchange.config.SecurityConfig.HAS_ROLE_ADMIN;
import static space.obminyashka.items_exchange.util.MessageSourceUtil.getExceptionMessageSourceWithId;

@RestController
@RequestMapping(ApiKey.SUBCATEGORY)
@Tag(name = "Subcategory")
@RequiredArgsConstructor
@Validated
public class SubcategoryController {

    private final SubcategoryService subcategoryService;

    @GetMapping("/{category_id}/names")
    @Operation(summary = "Find all subcategories names by category ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND")})
    public ResponseEntity<List<String>> getSubcategoryNamesByCategoryId(@PathVariable("category_id")
                                                                        @Positive(message = ResponseMessagesHandler.ValidationMessage.INVALID_NOT_POSITIVE_ID) long id) {
        List<String> subcategoriesNames = subcategoryService.findSubcategoryNamesByCategoryId(id);
        return subcategoriesNames.isEmpty() ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(subcategoriesNames, HttpStatus.OK);
    }

    @PreAuthorize(HAS_ROLE_ADMIN)
    @DeleteMapping("/{subcategory_id}")
    @Operation(summary = "Delete an existed subcategory by its ID", description = "ADMIN ONLY")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN")})
    @ResponseStatus(HttpStatus.OK)
    public void deleteSubcategoryById(@PathVariable("subcategory_id") @PositiveOrZero(message = "{validation.null.id}") long id)
            throws InvalidDtoException {
        if (!subcategoryService.isSubcategoryDeletable(id)) {
            throw new InvalidDtoException(getExceptionMessageSourceWithId(id, ResponseMessagesHandler.ValidationMessage.SUBCATEGORY_NOT_DELETABLE));
        }
        subcategoryService.removeSubcategoryById(id);
    }
}
