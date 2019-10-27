package com.hillel.evoApp.service.impl;

import com.hillel.evoApp.dao.AdvertisementRepository;
import com.hillel.evoApp.model.City;
import com.hillel.evoApp.service.AdvertisementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdvertisementServiceImpl implements AdvertisementService {

    private final AdvertisementRepository advertisementRepository;

    @Override
    public List<String> findAllCategoryNames() {
        List<String> categoryNames = advertisementRepository.findAllCategories();

        if (categoryNames != null) {
            log.info("IN AdvertisementServiceImpl (findAllCategoryNames): there are these categories: {}", categoryNames);
            return categoryNames;
        } else {
            log.warn("IN AdvertisementServiceImpl (findAllCategoryNames): there are no categories");
            return Collections.singletonList("No categories");
        }
    }

    @Override
    public List<String> getAllCitiesNamesInRussian() {
        List<City> cities = Arrays.asList(City.values());

        List<String> citiesInString = cities.stream()
                .map(City::getRussianValue)
                .collect(Collectors.toList());

        log.info("IN AdvertisementServiceImpl (getAllCitiesNames): there are these cities: {}", cities);
        return citiesInString;
    }
}
