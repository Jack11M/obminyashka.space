package space.obminyashka.items_exchange.mapper;

import org.mapstruct.Mapper;

import org.mapstruct.Mapping;
import space.obminyashka.items_exchange.dto.ImageDto;
import space.obminyashka.items_exchange.model.Image;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ImageMapper {
    ImageDto toDto(Image model);

    @Mapping(target = "advertisement", ignore = true)
    Image toModel(ImageDto dto);

    List<ImageDto> toDtoList(List<Image> modelList);
    List<Image> toModelList(List<ImageDto> dtoList);
}
