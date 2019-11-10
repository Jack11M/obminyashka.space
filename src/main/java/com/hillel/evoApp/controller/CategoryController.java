package com.hillel.evoApp.controller;

import com.hillel.evoApp.model.Category;
import com.hillel.evoApp.service.CategoryService;
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
    @GetMapping("/categories")
    public List<Category> allCategories() {
        return categoryService.findAllCategories();
    }
}
