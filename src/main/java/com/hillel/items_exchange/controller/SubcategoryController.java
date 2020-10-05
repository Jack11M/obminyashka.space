package com.hillel.items_exchange.controller;

import com.hillel.items_exchange.exception.InvalidDtoException;
import com.hillel.items_exchange.service.SubcategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.PositiveOrZero;
import java.util.List;

import static com.hillel.items_exchange.config.SecurityConfig.HAS_ROLE_ADMIN;
import static com.hillel.items_exchange.util.MessageSourceUtil.getExceptionMessageSourceWithId;

@RestController
@RequestMapping("/subcategory")
@Api(tags = "Subcategory")
@RequiredArgsConstructor
@Validated
@Slf4j
public class SubcategoryController {

    private final SubcategoryService subcategoryService;

    @GetMapping("/{category_id}/names")
    @ApiOperation(value = "Find all subcategories name by category ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "BAD REQUEST"),
            @ApiResponse(code = 404, message = "NOT FOUND")})
    public ResponseEntity<List<String>> getSubcategoryNamesByCategoryId(@PathVariable("category_id")
                                                                            @PositiveOrZero(message = "{invalid.id}") long id) {
        List<String> subcategoriesNames = subcategoryService.findSubcategoryNamesByCategoryId(id);
        if (subcategoriesNames.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(subcategoriesNames, HttpStatus.OK);
    }

    @PreAuthorize(HAS_ROLE_ADMIN)
    @DeleteMapping("/{subcategory_id}")
    @ApiOperation(value = "Delete an existed subcategory by its ID. (ADMIN ONLY)")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "BAD REQUEST"),
            @ApiResponse(code = 403, message = "FORBIDDEN")})
    public ResponseEntity<HttpStatus> deleteSubcategoryById(@PathVariable("subcategory_id")
                                                                @PositiveOrZero(message = "{invalid.id}") long id) {
        if (!subcategoryService.isSubcategoryDeletable(id)) {
            throw new InvalidDtoException(getExceptionMessageSourceWithId(id,
                    "subcategory.not-deletable"));
        }

        subcategoryService.removeSubcategoryById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
