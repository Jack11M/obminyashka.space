package space.obminyashka.items_exchange.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import space.obminyashka.items_exchange.repository.AreaRepository;
import space.obminyashka.items_exchange.repository.CityRepository;
import space.obminyashka.items_exchange.repository.LocationRepository;
import space.obminyashka.items_exchange.repository.model.Location;
import space.obminyashka.items_exchange.rest.dto.LocationDto;
import space.obminyashka.items_exchange.rest.mapper.LocationMapper;
import space.obminyashka.items_exchange.rest.response.LocationNameView;
import space.obminyashka.items_exchange.service.LocationService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {
    private final LocationRepository locationRepository;
    private final AreaRepository areaRepository;
    private final CityRepository cityRepository;
    private final LocationMapper locationMapper;

    @Override
    public List<LocationDto> findAll() {
        return locationMapper.toDtoList(locationRepository.findAll());
    }

    @Override
    public List<LocationNameView> findAllAreas() {
        return locationMapper.toNameViewList(areaRepository.findAll());
    }

    @Override
    public Optional<Location> findById(UUID id) {
        return locationRepository.findById(id);
    }

    @Override
    public List<LocationDto> findByIds(List<UUID> ids) {
        List<Location> locations = locationRepository.findByIdIn(ids);
        return locationMapper.toDtoList(locations);
    }

    @Override
    public Optional<LocationDto> getById(UUID id) {
        return findById(id)
                .map(locationMapper::toDto);
    }

    @Override
    public LocationDto save(LocationDto locationDto) {
        Location location = locationMapper.toModel(locationDto);
        Location savedLocation = locationRepository.saveAndFlush(location);
        return locationMapper.toDto(savedLocation);
    }

    @Override
    public void removeById(UUID id) {
        locationRepository.deleteById(id);
    }

    @Override
    public void removeById(List<UUID> ids) {
        ids.forEach(this::removeById);
    }

    @Override
    public boolean existsById(UUID id) {
        return locationRepository.existsById(id);
    }

    @Override
    public LocationDto update(LocationDto locationDto) {
        Location location = locationMapper.toModel(locationDto);
        Location updatedLocation = locationRepository.saveAndFlush(location);
        return locationMapper.toDto(updatedLocation);
    }

    @Override
    public List<LocationNameView> getAllCityByAreaId(UUID id) {
        return locationMapper.toCityNameViewList(cityRepository.findAllByAreaId(id));
    }

    @Override
    public boolean existAreaById(UUID id) {
        return areaRepository.existsById(id);
    }

}
