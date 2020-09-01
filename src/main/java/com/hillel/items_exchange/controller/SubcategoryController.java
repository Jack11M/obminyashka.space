package com.hillel.items_exchange.controller;

import com.hillel.items_exchange.exception.InvalidDtoException;
import com.hillel.items_exchange.mapper.tranfer.Exist;
import com.hillel.items_exchange.service.SubcategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.hillel.items_exchange.config.SecurityConfig.HAS_ROLE_ADMIN;
import static com.hillel.items_exchange.util.MessageSourceUtil.getExceptionMessageSourceWithId;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/subcategory")
@RequiredArgsConstructor
public class SubcategoryController {

    private final SubcategoryService subcategoryService;

    @GetMapping("/{category_id}/names")
    @ApiOperation(value = "Get subcategory names of an existing category.")
    public ResponseEntity<List<String>> getSubcategoryNamesByCategoryId(@Validated(Exist.class)
                                                                            @PathVariable("category_id") long id) {
        List<String> subcategoriesNames = subcategoryService.findSubcategoryNamesByCategoryId(id);
        if (subcategoriesNames.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(subcategoryService.findSubcategoryNamesByCategoryId(id), HttpStatus.OK);
    }

    @PreAuthorize(HAS_ROLE_ADMIN)
    @DeleteMapping("/{subcategory_id}")
    @ApiOperation(value = "Delete subcategory by ID if it exists. Only for users authorized as admin.")
    public ResponseEntity<HttpStatus> deleteSubcategoryById(@PathVariable("subcategory_id") long id) {
        if (!subcategoryService.isSubcategoryDeletable(id)) {
            throw new InvalidDtoException(getExceptionMessageSourceWithId(id, "subcategory.not-deletable"));
        }

        subcategoryService.removeSubcategoryById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
