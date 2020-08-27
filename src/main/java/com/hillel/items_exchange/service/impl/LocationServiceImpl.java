package com.hillel.items_exchange.service.impl;

import com.hillel.items_exchange.dao.LocationRepository;
import com.hillel.items_exchange.dto.LocationDto;
import com.hillel.items_exchange.model.Location;
import com.hillel.items_exchange.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.hillel.items_exchange.mapper.UtilMapper.convertAllTo;
import static com.hillel.items_exchange.mapper.UtilMapper.convertTo;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {
    private final LocationRepository locationRepository;

    @Override
    public List<LocationDto> findAll() {
        return new ArrayList<>(convertAllTo(locationRepository.findAll(), LocationDto.class, ArrayList::new));
    }

    @Override
    public Optional<Location> findById(long id) {
        return locationRepository.findById(id);
    }

    @Override
    public List<LocationDto> findByIds(List<Long> ids) {
        List<Location> locations = locationRepository.findByIdIn(ids);
        return new ArrayList<>(convertAllTo(locations, LocationDto.class, ArrayList::new));
    }

    @Override
    public Optional<LocationDto> getById(long id) {
        return findById(id)
                .map(location -> convertTo(location, LocationDto.class));
    }

    @Override
    public Location save(Location location) {
        return locationRepository.save(location);
    }

    @Override
    public LocationDto save(LocationDto locationDto) {
        Location location = convertTo(locationDto, Location.class);
        Location savedLocation = locationRepository.saveAndFlush(location);
        return convertTo(savedLocation, LocationDto.class);
    }

    @Override
    public void removeById(long id) {
        locationRepository.deleteById(id);
    }

    @Override
    public void removeById(List<Long> ids) {
        ids.forEach(this::removeById);
    }

    @Override
    public boolean existsById(long id) {
        return locationRepository.existsById(id);
    }

    @Override
    public LocationDto update(LocationDto locationDto) {
        Location location = convertTo(locationDto, Location.class);
        Location updatedLocation = locationRepository.saveAndFlush(location);
        return convertTo(updatedLocation, LocationDto.class);
    }
}