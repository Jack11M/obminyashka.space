package space.obminyashka.items_exchange.service.impl;

import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import space.obminyashka.items_exchange.controller.request.AdvertisementFindRequest;
import space.obminyashka.items_exchange.dao.AdvertisementRepository;
import space.obminyashka.items_exchange.dto.*;
import space.obminyashka.items_exchange.mapper.AdvertisementMapper;
import space.obminyashka.items_exchange.mapper.CategoryMapper;
import space.obminyashka.items_exchange.mapper.LocationMapper;
import space.obminyashka.items_exchange.model.Advertisement;
import space.obminyashka.items_exchange.model.Image;
import space.obminyashka.items_exchange.model.QAdvertisement;
import space.obminyashka.items_exchange.model.User;
import space.obminyashka.items_exchange.model.enums.AgeRange;
import space.obminyashka.items_exchange.model.enums.Status;
import space.obminyashka.items_exchange.service.AdvertisementService;
import space.obminyashka.items_exchange.service.ImageService;
import space.obminyashka.items_exchange.service.LocationService;
import space.obminyashka.items_exchange.service.SubcategoryService;

import jakarta.persistence.EntityNotFoundException;

import java.time.format.DateTimeFormatter;
import java.util.*;


@CacheConfig(cacheNames = "titles")
@Service
@RequiredArgsConstructor
public class AdvertisementServiceImpl implements AdvertisementService {

    private final AdvertisementMapper advertisementMapper;
    private final AdvertisementRepository advertisementRepository;
    private final SubcategoryService subcategoryService;
    private final CategoryMapper categoryMapper;
    private final LocationService locationService;
    private final LocationMapper locationMapper;
    private final ImageService imageService;
    private final Random random = new Random();

    @Value("${display.adv.date.format}")
    private String dateFormat;

    @Override
    public List<AdvertisementTitleDto> findRandomNThumbnails(AdvertisementFindRequest findAdvsRequest) {
        final var totalRecordsSize = advertisementRepository.countByIdNotAndSubcategoryId(
                findAdvsRequest.getExcludeAdvertisementId(), findAdvsRequest.getSubcategoryId());
        final var bound = (int) (totalRecordsSize / findAdvsRequest.getSize());
        findAdvsRequest.setPage(bound > 0 ? random.nextInt(bound) : 0);
        return findAllThumbnails(findAdvsRequest).getContent();
    }

    @Override
    public Page<AdvertisementTitleDto> findAllThumbnails(AdvertisementFindRequest findAdvsRequest) {
        return advertisementRepository.findAllByIdNotAndSubcategoryId(findAdvsRequest.getExcludeAdvertisementId(),
                        findAdvsRequest.getSubcategoryId(),
                        PageRequest.of(findAdvsRequest.getPage(), findAdvsRequest.getSize()))
                .map(this::buildAdvertisementTitle);
    }

    @Cacheable
    @Override
    public List<AdvertisementTitleDto> findAllByUsername(String username) {
        return advertisementRepository.findAllByUserUsername(username).stream()
                .map(this::buildAdvertisementTitle)
                .toList();
    }

    // @Cacheable(key = "#keyword")
    @Override
    public Page<AdvertisementTitleDto> findByKeyword(String keyword, Pageable pageable) {
        final var wholeStringSearchResult = advertisementRepository.search(keyword, pageable);
        if (!wholeStringSearchResult.isEmpty()) {
            return wholeStringSearchResult.map(this::buildAdvertisementTitle);
        }
        final var keywords = Set.of(keyword.split(" "));
        if (!keywords.isEmpty()) {
            return advertisementRepository.search(keywords, pageable).map(this::buildAdvertisementTitle);
        }
        return Page.empty();
    }

    @Override
    public Page<AdvertisementTitleDto> findByCategoryId(Long categoryId, Pageable pageable) {
        return advertisementRepository.findAdvertisementByCategoryId(categoryId, pageable)
                .map(this::buildAdvertisementTitle);
    }

    @Override
    public Optional<Advertisement> findByIdAndOwnerUsername(UUID advertisementId, String ownerName) {
        return advertisementRepository.findAdvertisementByIdAndUserUsername(advertisementId, ownerName);
    }

    @Override
    public Optional<AdvertisementDisplayDto> findDtoById(UUID id) {
        return advertisementRepository.findById(id).map(this::buildAdvertisementDisplayDto);
    }

    @Override
    public List<AdvertisementTitleDto> findFirst10ByFilter(AdvertisementFilterDto dto) {
        QAdvertisement qAdvertisement = QAdvertisement.advertisement;
        BooleanBuilder predicate = new BooleanBuilder()
                .and(!dto.getSubcategoryId().isEmpty() ? qAdvertisement.subcategory.id.in(dto.getSubcategoryId()) : null)
                .and(dto.getLocationId() != null ? qAdvertisement.location.id.eq(dto.getLocationId()) : null)
                .and(!dto.getSize().isEmpty() ? qAdvertisement.size.in(dto.getSize()) : null)
                .and(!dto.getSeason().isEmpty() ? qAdvertisement.season.in(dto.getSeason()) : null)
                .and(!dto.getGender().isEmpty() ? qAdvertisement.gender.in(dto.getGender()) : null)
                .and(!dto.getAge().isEmpty() ? qAdvertisement.age.in(dto.getAge()) : null);

        return advertisementMapper.toTitleDtoList(advertisementRepository.findAll(predicate));
    }

    @Override
    public boolean isUserHasAdvertisementWithId(UUID id, User user) {
        return advertisementRepository.existsAdvertisementByIdAndUser(id, user);
    }

    @Override
    public AdvertisementModificationDto createAdvertisement(AdvertisementModificationDto dto, User owner, List<byte[]> compressedImages, byte[] titleImage) {
        Advertisement adv = advertisementMapper.toModel(dto);
        adv.setUser(owner);
        adv.setStatus(Status.NEW);
        adv.setImages(compressedImages.stream().map(image -> new Image(image, adv)).toList());
        adv.setDefaultPhoto(titleImage);
        updateSubcategory(adv, dto.getSubcategoryId());
        updateLocation(adv, dto.getLocationId());
        return advertisementMapper.toModificationDto(advertisementRepository.save(adv));
    }

    @Override
    public AdvertisementModificationDto updateAdvertisement(AdvertisementModificationDto dto) {
        Advertisement toUpdate = advertisementMapper.toModel(dto);
        Advertisement fromDB = advertisementRepository.findById(dto.getId())
                .orElseThrow(EntityNotFoundException::new);

        updateAdvertisement(toUpdate, fromDB);
        updateSubcategory(fromDB, toUpdate.getSubcategory().getId());
        updateLocation(fromDB, toUpdate.getLocation().getId());
        fromDB.setStatus(Status.UPDATED);
        Advertisement updatedAdvertisement = advertisementRepository.saveAndFlush(fromDB);
        return advertisementMapper.toModificationDto(updatedAdvertisement);
    }

    @Override
    public boolean existById(UUID id) {
        return advertisementRepository.existsById(id);
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

    private void updateLocation(Advertisement fromDBAdvertisement, UUID id) {
        fromDBAdvertisement.setLocation(locationService.findById(id)
                .orElseThrow(EntityNotFoundException::new));
    }

    @Override
    @CacheEvict(key = "#id")
    public void remove(UUID id) {
        advertisementRepository.deleteById(id);
        advertisementRepository.flush();
    }

    @Override
    public void setDefaultImage(Advertisement advertisement, UUID imageId) {
        advertisement.getImages().stream()
                .filter(img -> img.getId().equals(imageId))
                .findFirst()
                .map(Image::getResource)
                .map(imageService::scale)
                .ifPresent(advertisement::setDefaultPhoto);

        advertisementRepository.saveAndFlush(advertisement);
    }

    @Override
    public boolean isUserHasAdvertisementAndItHasImageWithId(UUID advertisementId, UUID imageId, User owner) {
        return owner.getAdvertisements().stream()
                .filter(adv -> adv.getId().equals(advertisementId))
                .map(Advertisement::getImages)
                .flatMap(Collection::stream)
                .anyMatch(image -> image.getId().equals(imageId));
    }

    @Override
    public long count() {
        return advertisementRepository.count();
    }

    @Override
    public boolean areAdvertisementsExistWithSubcategory(long id) {
        return advertisementRepository.existsBySubcategoryId(id);
    }

    private AdvertisementTitleDto buildAdvertisementTitle(Advertisement advertisement) {
        return AdvertisementTitleDto.builder()
                .advertisementId(advertisement.getId())
                .image(getImage(advertisement))
                .title(advertisement.getTopic())
                .location(locationMapper.toDto(advertisement.getLocation()))
                .ownerName(advertisement.getUser().getUsername())
                .ownerAvatar(advertisement.getUser().getAvatarImage())
                .build();
    }

    private AdvertisementDisplayDto buildAdvertisementDisplayDto(Advertisement advertisement) {
        String createdDate = advertisement.getCreated().format(DateTimeFormatter.ofPattern(dateFormat));
        String age = Optional.ofNullable(advertisement.getAge()).map(AgeRange::getValue).orElse("");
        AdvertisementDisplayDto displayDto = AdvertisementDisplayDto.builder()
                .advertisementId(advertisement.getId())
                .ownerName(getOwnerFullName(advertisement.getUser()))
                .ownerAvatar(advertisement.getUser().getAvatarImage())
                .age(age)
                .phone(getOwnerPhone(advertisement.getUser()))
                .category(categoryMapper.toNameDto(advertisement.getSubcategory().getCategory()))
                .createdDate(createdDate)
                .build();

        AdvertisementDisplayDto mappedDto = advertisementMapper.toDto(advertisement);
        BeanUtils.copyProperties(mappedDto, displayDto, "createdDate", "phone", "age", "ownerName",
                "category");
        return displayDto;
    }

    private String getOwnerPhone(User user) {
        var phones = user.getPhones();
        return phones.stream()
                .findFirst()
                .map(phone -> String.valueOf(phone.getPhoneNumber()))
                .orElse("");
    }

    private byte[] getImage(Advertisement advertisement) {
        return Optional.ofNullable(advertisement.getDefaultPhoto())
                .orElseGet(() -> advertisement.getImages().stream()
                        .findFirst()
                        .map(Image::getResource)
                        .orElse(new byte[0]));
    }

    private String getOwnerFullName(User user) {
        String formatted = String.format("%s %s", user.getFirstName(), user.getLastName());
        return formatted.isBlank() ? user.getUsername() : formatted.trim();
    }
}
