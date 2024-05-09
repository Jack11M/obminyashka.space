package space.obminyashka.items_exchange.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import space.obminyashka.items_exchange.repository.AdvertisementRepository;
import space.obminyashka.items_exchange.repository.enums.AgeRange;
import space.obminyashka.items_exchange.repository.enums.Status;
import space.obminyashka.items_exchange.repository.model.Advertisement;
import space.obminyashka.items_exchange.repository.model.Image;
import space.obminyashka.items_exchange.repository.model.User;
import space.obminyashka.items_exchange.rest.dto.AdvertisementModificationDto;
import space.obminyashka.items_exchange.rest.exception.IllegalOperationException;
import space.obminyashka.items_exchange.rest.exception.not_found.EntityIdNotFoundException;
import space.obminyashka.items_exchange.rest.mapper.AdvertisementMapper;
import space.obminyashka.items_exchange.rest.mapper.CategoryMapper;
import space.obminyashka.items_exchange.rest.mapper.LocationMapper;
import space.obminyashka.items_exchange.rest.request.AdvertisementFilterRequest;
import space.obminyashka.items_exchange.rest.response.AdvertisementDisplayView;
import space.obminyashka.items_exchange.rest.response.AdvertisementTitleView;
import space.obminyashka.items_exchange.service.AdvertisementService;
import space.obminyashka.items_exchange.service.ImageService;
import space.obminyashka.items_exchange.service.LocationService;
import space.obminyashka.items_exchange.service.SubcategoryService;

import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import static java.util.function.Predicate.not;
import static space.obminyashka.items_exchange.rest.response.message.MessageSourceProxy.getParametrizedMessageSource;
import static space.obminyashka.items_exchange.rest.response.message.ResponseMessagesHandler.ExceptionMessage.ADVERTISEMENT_NOT_EXISTED_ID;
import static space.obminyashka.items_exchange.rest.response.message.ResponseMessagesHandler.ExceptionMessage.FAVORITE_ADVERTISEMENT_NOT_FOUND;
import static space.obminyashka.items_exchange.rest.response.message.ResponseMessagesHandler.ValidationMessage.USER_NOT_OWNER;


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
    public Page<AdvertisementTitleView> findAllFavorite(String username, Pageable pageable) {
        return advertisementRepository.findFavoriteAdvertisementsByUsername(username, pageable)
                .map(advertisementMapper::toAdvertisementTitleDto);
    }

    @Override
    public void addFavorite(UUID advertisementId, String username) {
        advertisementRepository.addFavoriteAdvertisementsByUsername(username, advertisementId);
    }

    @Override
    public void deleteFavorite(UUID advertisementId, String username) {
        final var numberOfDeletedAdv =
                advertisementRepository.removeFavoriteAdvertisementsByIdAndUserUsername(advertisementId, username);

        if (numberOfDeletedAdv == 0) {
            final var message = getParametrizedMessageSource(FAVORITE_ADVERTISEMENT_NOT_FOUND, advertisementId);
            throw new EntityIdNotFoundException(message);
        }
    }

    @Override
    public List<AdvertisementTitleView> findAllByUsername(String username) {
        return advertisementRepository.findAllByUserUsername(username).stream()
                .map(advertisementMapper::toAdvertisementTitleDto)
                .toList();
    }

    @Override
    public Optional<AdvertisementDisplayView> findDtoById(UUID id) {
        return advertisementRepository.findById(id).map(this::buildAdvertisementDisplayDto);
    }

    @Override
    public Page<AdvertisementTitleView> filterAdvertisementBySearchParameters(AdvertisementFilterRequest request) {
        PageRequest pageRequest = PageRequest.of(preparePage(request), request.getSize());
        return advertisementRepository.findAll(request.toPredicate(), pageRequest)
                .map(this::buildAdvertisementTitle);
    }

    private int preparePage(AdvertisementFilterRequest request) {
        if (!request.isEnableRandom()) {
            return request.getPage();
        }
        List<Long> subcategoriesIdValues = Optional.ofNullable(request.getSubcategoriesIdValues())
                .filter(not(List::isEmpty))
                .orElse(null);

        long totalRecords = advertisementRepository.countByIdNotAndSubcategoryId(
                request.getExcludeAdvertisementId(), subcategoriesIdValues);

        int bound = (int) (totalRecords / request.getSize());
        return bound > 0 ? random.nextInt(bound) : 0;
    }

    @Override
    public void validateUserAsAdvertisementOwner(UUID id, String username) throws IllegalOperationException, EntityNotFoundException {
        if (!advertisementRepository.existsAdvertisementById(id)) {
            throw new EntityNotFoundException(getParametrizedMessageSource(ADVERTISEMENT_NOT_EXISTED_ID, id));
        }
        if (!advertisementRepository.existsAdvertisementByIdAndUserUsername(id, username)) {
            throw new IllegalOperationException(getParametrizedMessageSource(USER_NOT_OWNER, id));
        }
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
            BeanUtils.copyProperties(toUpdate, fromDB, "created", "updated", "status", "location", "user", "subcategory", "images");
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
        advertisementRepository.deleteAdvertisementById(id);
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

    private AdvertisementTitleView buildAdvertisementTitle(Advertisement advertisement) {
        return AdvertisementTitleView.builder()
                .advertisementId(advertisement.getId())
                .image(getImage(advertisement))
                .title(advertisement.getTopic())
                .location(locationMapper.toDto(advertisement.getLocation()))
                .build();
    }

    private AdvertisementDisplayView buildAdvertisementDisplayDto(Advertisement advertisement) {
        String createdDate = advertisement.getCreated().format(DateTimeFormatter.ofPattern(dateFormat));
        String age = Optional.ofNullable(advertisement.getAge()).map(AgeRange::getValue).orElse("");
        AdvertisementDisplayView displayDto = AdvertisementDisplayView.builder()
                .advertisementId(advertisement.getId())
                .ownerName(getOwnerFullName(advertisement.getUser()))
                .ownerAvatar(advertisement.getUser().getAvatarImage())
                .age(age)
                .phone(getOwnerPhone(advertisement.getUser()))
                .category(categoryMapper.toNameDto(advertisement.getSubcategory().getCategory()))
                .createdDate(createdDate)
                .build();

        AdvertisementDisplayView mappedDto = advertisementMapper.toDto(advertisement);
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
