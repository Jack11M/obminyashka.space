package com.hillel.items_exchange.service;

import com.hillel.items_exchange.dao.AdvertisementRepository;
import com.hillel.items_exchange.dto.AdvertisementDto;
import com.hillel.items_exchange.model.Advertisement;
import com.hillel.items_exchange.model.Status;
import com.hillel.items_exchange.model.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdvertisementService {
    private final ModelMapper modelMapper;
    private final AdvertisementRepository advertisementRepository;

    public List<AdvertisementDto> findAll() {
        return mapAdvertisementsToDto(advertisementRepository.findAll());
    }

    public List<AdvertisementDto> findAllByTopic(String topic) {
        return mapAdvertisementsToDto(advertisementRepository.findFirst10ByTopicIgnoreCaseContaining(topic));
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
        adv.setStatus(Status.NEW);
        return mapAdvertisementToDto(advertisementRepository.save(adv));
    }

    public AdvertisementDto updateAdvertisement(AdvertisementDto dto) {
        Advertisement toUpdate = mapDtoToAdvertisement(dto);
        Advertisement fromDB = advertisementRepository.findById(dto.getId())
                .orElseThrow(EntityNotFoundException::new);
        BeanUtils.copyProperties(toUpdate, fromDB, "location", "user", "product", "created", "updated", "status");
        fromDB.setStatus(Status.UPDATED);
        Advertisement updatedAdvertisement = advertisementRepository.saveAndFlush(fromDB);
        return mapAdvertisementToDto(updatedAdvertisement);
    }

    public void remove(Long id) {
        advertisementRepository.deleteById(id);
        advertisementRepository.flush();
    }

    private List<AdvertisementDto> mapAdvertisementsToDto(Iterable<Advertisement> advertisements) {
        return modelMapper.map(advertisements, new TypeToken<List<AdvertisementDto>>() {}.getType());
    }

    private Advertisement mapDtoToAdvertisement(AdvertisementDto dto) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        return modelMapper.map(dto, Advertisement.class);
    }

    private AdvertisementDto mapAdvertisementToDto(Advertisement advertisement) {
        return modelMapper.map(advertisement, AdvertisementDto.class);
    }
}