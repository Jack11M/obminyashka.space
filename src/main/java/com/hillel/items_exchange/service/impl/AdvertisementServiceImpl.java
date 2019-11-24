package com.hillel.items_exchange.service.impl;

import com.hillel.items_exchange.dao.AdvertisementRepository;
import com.hillel.items_exchange.service.AdvertisementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdvertisementServiceImpl implements AdvertisementService {

    private final AdvertisementRepository advertisementRepository;
}
