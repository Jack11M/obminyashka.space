package com.hillel.items_exchange.service;

import com.hillel.items_exchange.dao.AdvertisementRepository;
import com.hillel.items_exchange.dto.AdvertisementDto;
import com.hillel.items_exchange.model.Advertisement;
import com.hillel.items_exchange.model.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdvertisementService {
    private final ModelMapper modelMapper;
    private final AdvertisementRepository advertisementRepository;

    public List<AdvertisementDto> findAll() {
        return modelMapper.map(advertisementRepository.findAll(), new TypeToken<List<AdvertisementDto>>() {}.getType());
    }

    public Optional<AdvertisementDto> findById(Long id) {
        return advertisementRepository.findById(id).map(this::mapAdvertisementToDto);
    }

    public boolean isAdvertisementExists(Long id, User user) {
        return advertisementRepository.existsAdvertisementByIdAndUser(id, user);
    }

    public AdvertisementDto createAdvertisement(AdvertisementDto advertisementDto, User user) {
        Advertisement adv = mapDtoToAdvertisement(advertisementDto);
        adv.setUser(user);
        return mapAdvertisementToDto(advertisementRepository.save(adv));
    }

    public AdvertisementDto updateAdvertisement(AdvertisementDto advertisementDto) {
        Advertisement updatedAdvertisement = advertisementRepository.save(mapDtoToAdvertisement(advertisementDto));
        return mapAdvertisementToDto(updatedAdvertisement);
    }

    public void remove(AdvertisementDto advertisementDto) {
        advertisementRepository.delete(mapDtoToAdvertisement(advertisementDto));
    }

    private Advertisement mapDtoToAdvertisement(AdvertisementDto dto) {
        return modelMapper.map(dto, Advertisement.class);
    }

    private AdvertisementDto mapAdvertisementToDto(Advertisement advertisement) {
        return modelMapper.map(advertisement, AdvertisementDto.class);
    }
}