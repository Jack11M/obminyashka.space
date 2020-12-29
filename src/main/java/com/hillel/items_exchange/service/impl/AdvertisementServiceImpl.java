package com.hillel.items_exchange.service.impl;

import com.hillel.items_exchange.dao.AdvertisementRepository;
import com.hillel.items_exchange.dto.AdvertisementDto;
import com.hillel.items_exchange.dto.AdvertisementFilterDto;
import com.hillel.items_exchange.model.Advertisement;
import com.hillel.items_exchange.model.Image;
import com.hillel.items_exchange.model.Location;
import com.hillel.items_exchange.model.User;
import com.hillel.items_exchange.model.enums.Status;
import com.hillel.items_exchange.service.AdvertisementService;
import com.hillel.items_exchange.service.ImageService;
import com.hillel.items_exchange.service.LocationService;
import com.hillel.items_exchange.service.SubcategoryService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdvertisementServiceImpl implements AdvertisementService {

    private final ModelMapper modelMapper;
    private final AdvertisementRepository advertisementRepository;
    private final SubcategoryService subcategoryService;
    private final LocationService locationService;
    private final ImageService imageService;

    @Override
    public List<AdvertisementDto> findAll(Pageable pageable) {
        List<Advertisement> content = advertisementRepository.findAll(pageable).getContent();
        return mapAdvertisementsToDto(content);
    }

    @Override
    public List<AdvertisementDto> findFirst10ByTopic(String topic) {
        return mapAdvertisementsToDto(advertisementRepository.findFirst10ByTopicIgnoreCaseContaining(topic));
    }

    @Override
    public Optional<Advertisement> findById(long advertisementId) {
        return advertisementRepository.findById(advertisementId);
    }

    @Override
    public Optional<AdvertisementDto> findDtoById(long id) {
        return findById(id).map(this::mapAdvertisementToDto);
    }

    @Override
    public List<AdvertisementDto> findFirst10AdvertisementsByMultipleParams(AdvertisementFilterDto dto) {
        return mapAdvertisementsToDto(
                advertisementRepository.findFirst10ByAgeOrGenderOrSizeOrSeasonOrSubcategoryId(
                        dto.getAge(), dto.getGender(), dto.getSize(), dto.getSeason(),
                        dto.getSubcategoryId()));
    }

    @Override
    public boolean isUserHasAdvertisementWithId(long id, User user) {
        return advertisementRepository.existsAdvertisementByIdAndUser(id, user);
    }

    @Override
    public AdvertisementDto createAdvertisement(AdvertisementDto advertisementDto, User user) {
        Advertisement adv = mapDtoToAdvertisement(advertisementDto);
        adv.setUser(user);
        adv.setStatus(Status.NEW);
        updateSubcategory(adv, advertisementDto.getSubcategoryId());

        return mapAdvertisementToDto(advertisementRepository.save(adv));
    }

    @Override
    public AdvertisementDto updateAdvertisement(AdvertisementDto dto) {
        Advertisement toUpdate = mapDtoToAdvertisement(dto);
        Advertisement fromDB = advertisementRepository.findById(dto.getId())
                .orElseThrow(EntityNotFoundException::new);

        updateAdvertisement(toUpdate, fromDB);
        updateSubcategory(fromDB, toUpdate.getSubcategory().getId());
        updateImages(toUpdate, fromDB);
        createOrUpdateLocation(toUpdate, fromDB);

        fromDB.setStatus(Status.UPDATED);
        Advertisement updatedAdvertisement = advertisementRepository.saveAndFlush(fromDB);
        return mapAdvertisementToDto(updatedAdvertisement);
    }

    private void updateAdvertisement(Advertisement toUpdate, Advertisement fromDB) {
        if (!fromDB.equals(toUpdate)) {
            BeanUtils.copyProperties(toUpdate, fromDB, "created", "updated", "status", "location", "user", "subcategory", "images", "chats");
        }
    }

    private void updateSubcategory(Advertisement fromDBAdvertisement, long id) {
        fromDBAdvertisement.setSubcategory(subcategoryService.findById(id)
                .orElseThrow(EntityNotFoundException::new));
    }

    private void updateImages(Advertisement toUpdateAdvertisement, Advertisement fromDBAdvertisement) {
        List<Image> existImages = fromDBAdvertisement.getImages();
        List<Image> newImages = toUpdateAdvertisement.getImages();
        if (existImages != null && !Objects.equals(newImages, existImages)) {
            existImages.retainAll(newImages);
            newImages.stream().filter(image -> !existImages.contains(image)).forEach(existImages::add);
        }
    }

    private void createOrUpdateLocation(Advertisement toUpdate, Advertisement fromDB) {
        Location toUpdateLocation = toUpdate.getLocation();
        if (!toUpdateLocation.equals(fromDB.getLocation())) {
            Location newLocation = locationService.findById(toUpdateLocation.getId())
                    .orElseGet(() -> locationService.save(toUpdateLocation));
            BeanUtils.copyProperties(toUpdateLocation, newLocation);
            fromDB.setLocation(newLocation);
        }
    }

    @Override
    public void remove(long id) {
        advertisementRepository.deleteById(id);
        advertisementRepository.flush();
    }

    @Override
    public void setDefaultImage(Advertisement advertisement, Long imageId, User owner) {
        advertisement.getImages().stream()
                .filter(img -> img.getId() == imageId)
                .findFirst()
                .map(Image::getResource)
                .map(imageService::scale)
                .ifPresent(advertisement::setDefaultPhoto);

        advertisementRepository.saveAndFlush(advertisement);
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
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        return modelMapper.map(advertisement, AdvertisementDto.class);
    }

    @Override
    public boolean isUserHasAdvertisementAndItHasImageWithId(Long advertisementId, Long imageId, User owner) {
        return owner.getAdvertisements().stream()
                .filter(adv -> adv.getId() == advertisementId)
                .map(Advertisement::getImages)
                .flatMap(Collection::stream)
                .anyMatch(image -> image.getId() == imageId);
    }
}
