package com.hillel.evoApp.service.impl;

import com.hillel.evoApp.model.Category;
import com.hillel.evoApp.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    @Override
    public List<Category> findAllCategories() {
        return null;
    }
}
