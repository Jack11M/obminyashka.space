package space.obminyashka.items_exchange.mapper;

import org.mapstruct.Mapper;

import space.obminyashka.items_exchange.dto.ImageDto;
import space.obminyashka.items_exchange.model.Image;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ImageMapper {
    ImageDto toDto(Image model);

    //java: Unmapped target property: "advertisement".
    Image toModel(ImageDto dto);

    List<ImageDto> toDTOList(List<Image> modelList);
    List<Image> toModelList(List<ImageDto> dtoList);
}
