package space.obminyashka.items_exchange.rest.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import space.obminyashka.items_exchange.repository.model.Advertisement;
import space.obminyashka.items_exchange.repository.projection.AdvertisementTitleProjection;
import space.obminyashka.items_exchange.rest.dto.AdvertisementModificationDto;
import space.obminyashka.items_exchange.rest.response.AdvertisementDisplayView;
import space.obminyashka.items_exchange.rest.response.AdvertisementTitleView;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {ImageMapper.class, LocationMapper.class, SubcategoryMapper.class})
public interface AdvertisementMapper {

    @Mapping(target = "phone", ignore = true)
    @Mapping(source = "user.avatarImage", target = "ownerAvatar")
    @Mapping(source = "user.username", target = "ownerName")
    @Mapping(source = "subcategory.category", target = "category")
    @Mapping(source = "id", target = "advertisementId")
    @Mapping(source = "created", target = "createdDate")
    AdvertisementDisplayView toDto(Advertisement model);

    @Mapping(source = "location.id", target = "locationId")
    @Mapping(source = "subcategory.id", target = "subcategoryId")
    AdvertisementModificationDto toModificationDto(Advertisement model);

    //Unmapped target properties: "updated, status, defaultPhoto, user".
    @Mapping(source = "advertisementId", target = "id")
    @Mapping(source = "createdDate", target = "created")
    Advertisement toModel(AdvertisementDisplayView dto);

    //Unmapped target properties:updated,status, defaultPhoto, user, images
    @Mapping(source = "subcategoryId", target = "subcategory.id")
    @Mapping(source = "locationId", target = "location.id")
    Advertisement toModel(AdvertisementModificationDto dto);

    @Mapping(source = "id", target = "advertisementId")
    @Mapping(source = "defaultPhoto", target = "image")
    @Mapping(source = "topic", target = "title")
    @Mapping(source = ".", target = "isFavorite", conditionExpression = "java(isFavoriteByUserId(projection))")
    AdvertisementTitleView toAdvertisementTitleDto(AdvertisementTitleProjection projection);

    default boolean isFavoriteByUserId(AdvertisementTitleProjection projection) {
        return projection.getUserId() != null;
    }

    List<AdvertisementDisplayView> toDtoList(List<Advertisement> modelList);

    List<Advertisement> toModelList(List<AdvertisementDisplayView> dtoList);
}
