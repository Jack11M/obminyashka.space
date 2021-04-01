package space.obminyashka.items_exchange.controller;

import space.obminyashka.items_exchange.exception.InvalidDtoException;
import space.obminyashka.items_exchange.service.SubcategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.PositiveOrZero;
import java.util.List;

import static space.obminyashka.items_exchange.config.SecurityConfig.HAS_ROLE_ADMIN;
import static space.obminyashka.items_exchange.util.MessageSourceUtil.getExceptionMessageSourceWithId;

@RestController
@RequestMapping("/subcategory")
@Api(tags = "Subcategory")
@RequiredArgsConstructor
@Validated
public class SubcategoryController {

    private final SubcategoryService subcategoryService;

    @GetMapping("/{category_id}/names")
    @ApiOperation(value = "Find all subcategories names by category ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "BAD REQUEST"),
            @ApiResponse(code = 404, message = "NOT FOUND")})
    public ResponseEntity<List<String>> getSubcategoryNamesByCategoryId(@PathVariable("category_id")
                                                                            @PositiveOrZero(message = "{invalid.id}") long id) {
        List<String> subcategoriesNames = subcategoryService.findSubcategoryNamesByCategoryId(id);
        return subcategoriesNames.isEmpty() ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(subcategoriesNames, HttpStatus.OK);
    }

    @PreAuthorize(HAS_ROLE_ADMIN)
    @DeleteMapping("/{subcategory_id}")
    @ApiOperation(value = "Delete an existed subcategory by its ID", notes = "ADMIN ONLY")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "BAD REQUEST"),
            @ApiResponse(code = 403, message = "FORBIDDEN")})
    @ResponseStatus(HttpStatus.OK)
    public void deleteSubcategoryById(@PathVariable("subcategory_id") @PositiveOrZero(message = "{invalid.id}") long id)
            throws InvalidDtoException {
        if (!subcategoryService.isSubcategoryDeletable(id)) {
            throw new InvalidDtoException(getExceptionMessageSourceWithId(id, "subcategory.not-deletable"));
        }
        subcategoryService.removeSubcategoryById(id);
    }
}
