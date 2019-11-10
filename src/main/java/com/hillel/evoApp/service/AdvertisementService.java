package com.hillel.evoApp.service;

import com.hillel.evoApp.dao.AdvertisementRepository;
import com.hillel.evoApp.dto.AdvertisementDto;
import com.hillel.evoApp.model.Advertisement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdvertisementService {

    private final AdvertisementRepository advertisementRepository;

    public List<String> findAllCategoryNames() {
        List<String> categoryNames = advertisementRepository.findAllCategories();

        if (categoryNames != null) {
            log.info("IN AdvertisementServiceImpl (categoryNames): there are these categories: {}", categoryNames);
            return categoryNames;
        } else {
            log.warn("IN AdvertisementServiceImpl (categoryNames): there are no categories");
            return Collections.singletonList("No categories");
        }
    }

    public List<Advertisement> findAll() {
        return advertisementRepository.findAll();
    }

    public Optional<Advertisement> findById(Long id) {
        return advertisementRepository.findById(id);
    }

    public Advertisement createAdvertisement(AdvertisementDto dto) {
        return advertisementRepository.save(populateAdvertisementFromDto(dto));
    }

    public Advertisement updateAdvertisement(AdvertisementDto dto) {
        return advertisementRepository.save(populateAdvertisementFromDto(dto));
    }

    public void remove(AdvertisementDto dto) {
        advertisementRepository.delete(populateAdvertisementFromDto(dto));
    }

    private Advertisement populateAdvertisementFromDto(AdvertisementDto dto) {
        Advertisement adv = new Advertisement();
        adv.setTopic(dto.getTopic());
        adv.setDealType(dto.getDealType());
        adv.setIsFavourite(dto.getIsFavourite());
        adv.setDescription(dto.getDescription());
        adv.setLocations(Collections.singletonList(dto.getLocations()));
        adv.setAdvertisementImages(dto.getAdvertisementImages());
        adv.setExchangeProducts(dto.getExchangeProducts());
        adv.setProduct(dto.getProduct());
        adv.setUsers(Collections.singletonList(dto.getUser()));

        return adv;
    }
}
