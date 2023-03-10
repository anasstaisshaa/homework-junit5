package com.dmdev.mapper;

import com.dmdev.dto.CreateSubscriptionDto;
import com.dmdev.entity.Provider;
import com.dmdev.entity.Status;
import com.dmdev.entity.Subscription;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class CreateSubscriptionMapperTest {
    private final CreateSubscriptionMapper createSubscriptionMapper = CreateSubscriptionMapper.getInstance();

    @Test
    void map() {

        CreateSubscriptionDto dto = getCreateSubscriptionDto();
        Subscription actualResult = createSubscriptionMapper.map(dto);

        Subscription expectedResult = getSubscription();

        assertThat(actualResult).isEqualTo(expectedResult);
    }

    private static Subscription getSubscription() {
        return Subscription.builder()
                .id(null)
                .userId(1)
                .name("Ivan")
                .provider(Provider.APPLE)
                .expirationDate(Instant.MAX)
                .status(Status.ACTIVE)
                .build();
    }

    private static CreateSubscriptionDto getCreateSubscriptionDto() {
        return CreateSubscriptionDto.builder()
                .userId(1)
                .name("Ivan")
                .provider(Provider.APPLE.name())
                .expirationDate(Instant.MAX)
                .build();
    }
}