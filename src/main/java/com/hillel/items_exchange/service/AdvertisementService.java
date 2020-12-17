package com.hillel.items_exchange.service;

import com.hillel.items_exchange.dao.AdvertisementRepository;
import com.hillel.items_exchange.dto.AdvertisementDto;
import com.hillel.items_exchange.dto.AdvertisementFilterDto;
import com.hillel.items_exchange.model.*;
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
import java.util.Optional;

import static com.hillel.items_exchange.util.MessageSourceUtil.getExceptionMessageSource;

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
        updateSubcategory(adv.getProduct(), advertisementDto.getProduct().getSubcategoryId());

        return mapAdvertisementToDto(advertisementRepository.save(adv));
    }

    public AdvertisementDto updateAdvertisement(AdvertisementDto dto) {
        Advertisement toUpdate = mapDtoToAdvertisement(dto);
        Advertisement fromDB = advertisementRepository.findById(dto.getId())
                .orElseThrow(EntityNotFoundException::new);

        updateAdvertisement(toUpdate, fromDB);

        Product toUpdateProduct = toUpdate.getProduct();
        Product fromDBProduct = fromDB.getProduct();
        updateProduct(toUpdateProduct, fromDBProduct);
        updateSubcategory(fromDBProduct, toUpdateProduct.getSubcategory().getId());
        updateImages(toUpdateProduct, fromDBProduct);
        createOrUpdateLocation(toUpdate, fromDB);

        fromDB.setStatus(Status.UPDATED);
        Advertisement updatedAdvertisement = advertisementRepository.saveAndFlush(fromDB);
        return mapAdvertisementToDto(updatedAdvertisement);
    }

    private void updateAdvertisement(Advertisement toUpdate, Advertisement fromDB) {
        if (!fromDB.equals(toUpdate)) {
            BeanUtils.copyProperties(toUpdate, fromDB, "created", "updated", "status", "location", "user", "product");
        }
    }

    private void updateProduct(Product toUpdateProduct, Product fromDBProduct) {
        if (!fromDBProduct.equals(toUpdateProduct)) {
            BeanUtils.copyProperties(toUpdateProduct, fromDBProduct, "advertisement", "subcategory", "images");
        }
    }

    private void updateSubcategory(Product fromDBProduct, long id) {
        fromDBProduct.setSubcategory(subcategoryService.findById(id)
                .orElseThrow(EntityNotFoundException::new));
    }

    private void updateImages(Product toUpdateProduct, Product fromDBProduct) {
        List<Image> existImages = fromDBProduct.getImages();
        List<Image> newImages = toUpdateProduct.getImages();
        if (existImages.size() != newImages.size() || !existImages.containsAll(newImages)) {
            existImages.retainAll(newImages);
            newImages.stream().filter(image -> !existImages.contains(image))
                    .forEach(image -> {
                        image.getProduct().setId(fromDBProduct.getId());
                        existImages.add(image);
                    });
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

    public void remove(long id) {
        advertisementRepository.deleteById(id);
        advertisementRepository.flush();
    }

    public void setDefaultImage(Long advertisementId, Long imageId, User owner) throws ClassNotFoundException {
        final Advertisement advertisement = owner.getAdvertisements().parallelStream()
                .filter(adv -> adv.getId() == advertisementId)
                .findFirst()
                .orElseThrow(() -> new ClassNotFoundException(getExceptionMessageSource("exception.illegal.id")));
        final Image image = advertisement.getProduct().getImages().parallelStream()
                .filter(img -> img.getId() == imageId)
                .findFirst()
                .orElseThrow(() -> new ClassNotFoundException(getExceptionMessageSource("exception.illegal.id")));
        advertisement.getProduct().getImages().forEach(img -> img.setDefaultPhoto(false));
        image.setDefaultPhoto(true);
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
        return modelMapper.map(advertisement, AdvertisementDto.class);
    }

    public boolean isAdvertisementAndImageExists(Long advertisementId, Long imageId, User owner) {
        return owner.getAdvertisements().stream()
                .filter(adv -> adv.getId() == advertisementId)
                .map(Advertisement::getProduct)
                .map(Product::getImages)
                .flatMap(Collection::stream)
                .anyMatch(image -> image.getId() == imageId);
    }
}
