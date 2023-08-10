package space.obminyashka.items_exchange.rest.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import space.obminyashka.items_exchange.repository.model.Area;
import space.obminyashka.items_exchange.repository.model.District;
import space.obminyashka.items_exchange.rest.dto.LocationDto;
import space.obminyashka.items_exchange.repository.model.Location;
import space.obminyashka.items_exchange.rest.response.LocationNameView;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LocationMapper {
    LocationDto toDto(Location model);

    LocationNameView toNameView(Area area);

    @Mapping(target = "advertisements", ignore = true)
    Location toModel(LocationDto dto);

    List<LocationNameView> toNameViewList(List<Area> areas);

    List<LocationNameView> toDistrictNameViewList(List<District> list);

    List<LocationDto> toDtoList(List<Location> modelList);

    List<Location> toModelList(List<LocationDto> dtoList);
}
