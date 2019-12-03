package com.hillel.items_exchange.controller;

import com.hillel.items_exchange.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @ResponseBody
    @GetMapping("/categories/names")
    public List<String> allCategoriesNames() {
        return categoryService.findAllCategoryNames();
    }
}
