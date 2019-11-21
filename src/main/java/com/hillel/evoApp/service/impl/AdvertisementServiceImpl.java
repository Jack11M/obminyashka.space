package com.hillel.evoApp.service.impl;

import com.hillel.evoApp.dao.AdvertisementRepository;
import com.hillel.evoApp.service.AdvertisementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdvertisementServiceImpl implements AdvertisementService {

    private final AdvertisementRepository advertisementRepository;

    @Override
    public List<String> findAllCategoryNames() {
        List<String> categoryNames = advertisementRepository.findAllCategories();

        if (categoryNames.isEmpty()) {
            log.info("IN AdvertisementServiceImpl (categoryNames): there are these categories: {}", categoryNames);
            return categoryNames;
        } else {
            log.warn("IN AdvertisementServiceImpl (categoryNames): there are no categories");
            return Collections.singletonList("No categories");
        }
    }
}
