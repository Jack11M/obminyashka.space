package com.hillel.items_exchange.service.impl;

import com.hillel.items_exchange.dao.CategoryRepository;
import com.hillel.items_exchange.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<String> findAllCategoryNames() {
        List<String> categoryNames = categoryRepository.findAllCategories();

        if (categoryNames.isEmpty()) {
            log.warn("IN CategoryServiceImpl (findAllCategoryNames): there are no categories");
            return Collections.singletonList("No categories");
        } else {
            log.info("IN CategoryServiceImpl (findAllCategoryNames): there are these categories: {}", categoryNames);
            return categoryNames;
        }
    }
}
