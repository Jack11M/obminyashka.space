package space.obminyashka.items_exchange.rest.mapper;

import org.mapstruct.Mapper;

import org.mapstruct.Mapping;
import space.obminyashka.items_exchange.rest.response.ImageView;
import space.obminyashka.items_exchange.repository.model.Image;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ImageMapper {
    ImageView toDto(Image model);

    @Mapping(target = "advertisement", ignore = true)
    Image toModel(ImageView dto);

    List<ImageView> toDtoList(List<Image> modelList);
    List<Image> toModelList(List<ImageView> dtoList);
}
