package com.hillel.items_exchange.security.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
@RequiredArgsConstructor
@Slf4j
public class InvalidatedTokensHolder {

    private final ConcurrentMap<String, Date> data = new ConcurrentHashMap<>();

    public void invalidate(final String token, final Date expireDate) {
        data.computeIfAbsent(token, v -> expireDate);
    }

    public boolean isInvalidated(final String token) {
        return data.containsKey(token);
    }


    @Scheduled(fixedDelayString = "${app.jwt.expiration.time.ms}")
    private void removeExpiredTokens() {
        data.entrySet().removeIf(entry -> entry.getValue().before(new Date()));
    }
}
