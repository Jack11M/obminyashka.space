package com.hillel.items_exchange.service;

import com.hillel.items_exchange.dao.AdvertisementRepository;
import com.hillel.items_exchange.dto.AdvertisementDto;
import com.hillel.items_exchange.dto.AdvertisementFilterDto;
import com.hillel.items_exchange.dto.ImageDto;
import com.hillel.items_exchange.model.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdvertisementService {
    private final ModelMapper modelMapper;
    private final AdvertisementRepository advertisementRepository;
    private final SubcategoryService subcategoryService;
    private final LocationService locationService;

    public List<AdvertisementDto> findAll(Pageable pageable) {
        List<Advertisement> content = advertisementRepository.findAll(pageable).getContent();
        return mapAdvertisementsToDto(content);
    }

    public List<AdvertisementDto> findAllByTopic(String topic) {
        return mapAdvertisementsToDto(advertisementRepository.findFirst10ByTopicIgnoreCaseContaining(topic));
    }

    public Optional<AdvertisementDto> findById(long id) {
        return advertisementRepository.findById(id).map(this::mapAdvertisementToDto);
    }

    public List<AdvertisementDto> findAdvertisementsByMultipleParams(AdvertisementFilterDto dto) {
        return mapAdvertisementsToDto(
                advertisementRepository.findFirst10ByProductAgeOrProductGenderOrProductSizeOrProductSeasonOrProductSubcategoryId(
                        dto.getAge(), dto.getGender(), dto.getSize(), dto.getSeason(), dto.getSubcategoryId()));
    }

    public boolean isAdvertisementExists(long id, User user) {
        return advertisementRepository.existsAdvertisementByIdAndUser(id, user);
    }

    public AdvertisementDto createAdvertisement(AdvertisementDto advertisementDto, User user) {
        Advertisement adv = mapDtoToAdvertisement(advertisementDto);
        adv.setUser(user);
        adv.setStatus(Status.NEW);
        adv.getProduct().setSubcategory(subcategoryService.findById(advertisementDto.getProduct().getSubcategoryId())
                .orElseThrow(EntityNotFoundException::new));

        Optional<Location> location = locationService.findByCityAndDistrict(
                adv.getLocation().getCity(), adv.getLocation().getDistrict());
        adv.setLocation(location.orElseGet(() -> locationService.createLocation(adv.getLocation())));

        return mapAdvertisementToDto(advertisementRepository.save(adv));
    }

    public AdvertisementDto updateAdvertisement(AdvertisementDto dto) {
        Advertisement toUpdate = mapDtoToAdvertisement(dto);
        Advertisement fromDB = advertisementRepository.findById(dto.getId())
                .orElseThrow(EntityNotFoundException::new);
        BeanUtils.copyProperties(toUpdate, fromDB, "user", "created",
                "updated", "status");
        fromDB.setStatus(Status.UPDATED);
        fromDB.getProduct().setSubcategory(subcategoryService.findById(dto.getProduct().getSubcategoryId())
                .orElseThrow(EntityNotFoundException::new));
        fromDB.getProduct().getImages()
                .forEach(image -> image.setProduct(toUpdate.getProduct()));
        Optional<Location> location = locationService.findByCityAndDistrict(
                toUpdate.getLocation().getCity(), toUpdate.getLocation().getDistrict());
        fromDB.setLocation(location.orElseGet(() -> locationService.createLocation(toUpdate.getLocation())));
        fromDB.setDescription(toUpdate.getDescription());

        Advertisement updatedAdvertisement = advertisementRepository.saveAndFlush(fromDB);
        return mapAdvertisementToDto(updatedAdvertisement);
    }


    public void remove(long id) {
        advertisementRepository.deleteById(id);
        advertisementRepository.flush();
    }

    private List<AdvertisementDto> mapAdvertisementsToDto(Iterable<Advertisement> advertisements) {
        return modelMapper.map(advertisements, new TypeToken<List<AdvertisementDto>>() {
        }.getType());
    }

    private Advertisement mapDtoToAdvertisement(AdvertisementDto dto) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        return modelMapper.map(dto, Advertisement.class);
    }

    private AdvertisementDto mapAdvertisementToDto(Advertisement advertisement) {
        return modelMapper.map(advertisement, AdvertisementDto.class);
    }
}