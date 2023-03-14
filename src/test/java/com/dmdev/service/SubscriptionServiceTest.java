package com.dmdev.service;

import com.dmdev.dao.SubscriptionDao;
import com.dmdev.dto.CreateSubscriptionDto;
import com.dmdev.entity.Provider;
import com.dmdev.entity.Status;
import com.dmdev.entity.Subscription;
import com.dmdev.exception.SubscriptionException;
import com.dmdev.exception.ValidationException;
import com.dmdev.mapper.CreateSubscriptionMapper;
import com.dmdev.validator.CreateSubscriptionValidator;
import com.dmdev.validator.Error;
import com.dmdev.validator.ValidationResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubscriptionServiceTest {
    @Mock
    private SubscriptionDao subscriptionDao;
    @Mock
    private CreateSubscriptionMapper createSubscriptionMapper;
    @Mock
    private CreateSubscriptionValidator createSubscriptionValidator;
    @Mock
    Clock clock;
    @InjectMocks
    SubscriptionService subscriptionService;

    @Test
    void testWhenCreateValidDto_ThenSubscriptionEqualToDto() {
        CreateSubscriptionDto dto = buildCreateSubscriptionDto();
        Subscription subscription = buildSubscription();

        doReturn(new ValidationResult()).when(createSubscriptionValidator).validate(dto);
        doReturn(List.of(subscription)).when(subscriptionDao).findByUserId(dto.getUserId());
        doReturn(subscription).when(subscriptionDao).upsert(subscription); //TODO clarify how to use real class

        Subscription actualResult = subscriptionService.upsert(dto);
        assertThat(actualResult).isEqualTo(subscription);
    }
    @Test
    void testWhenValidatorResultHasErrorAndCallingUpsert_ThenThrowValidationException(){
        CreateSubscriptionDto dto = buildCreateSubscriptionDto();

        ValidationResult validationResult = new ValidationResult();
        validationResult.add(Error.of(1, "message"));

        doReturn(validationResult).when(createSubscriptionValidator).validate(dto);

        assertThrows(ValidationException.class, () -> subscriptionService.upsert(dto));
        verifyNoInteractions(subscriptionDao, createSubscriptionMapper);
    }

    @Test
    void cancel() { //TODO correct name
        Subscription subscription = buildSubscription();

        doReturn(Optional.of(subscription)).when(subscriptionDao).findById(subscription.getId());
        subscriptionService.cancel(subscription.getId());
        Status actualResult = subscription.getStatus();

        assertThat(actualResult).isEqualTo(Status.CANCELED);
    }
    @Test
    void cancelFailed(){ //TODO correct name
        Subscription subscription = buildSubscription();
        subscription.setStatus(Status.CANCELED);

        doReturn(Optional.of(subscription)).when(subscriptionDao).findById(subscription.getId());

        assertThrows(SubscriptionException.class, () -> subscriptionService.cancel(subscription.getId()));
    }

    @Test
    void expire() { //TODO correct name
        Subscription subscription = buildSubscription();

        doReturn(Optional.of(subscription)).when(subscriptionDao).findById(subscription.getId());
        subscriptionService.expire(subscription.getId());

        assertThat(subscription.getStatus()).isEqualTo(Status.EXPIRED);
    }
    @Test
    void expireFailed(){ //TODO correct name
        Subscription subscription = buildSubscription();

        doReturn(Optional.of(subscription)).when(subscriptionDao).findById(subscription.getId());
        subscription.setStatus(Status.EXPIRED);

        assertThrows(SubscriptionException.class, () -> subscriptionService.expire(subscription.getId()));
    }
    @Test
    void testIfObjectIsNotFound_ThenThrowIllegalArgumentException(){
        Subscription subscription = buildSubscription();
        assertThrows(IllegalArgumentException.class, () -> subscriptionService.expire(subscription.getId()));
    }
    private static CreateSubscriptionDto buildCreateSubscriptionDto() {
        return CreateSubscriptionDto.builder()
                .userId(1)
                .name("Ivan")
                .provider(Provider.APPLE.name())
                .expirationDate(Instant.MAX)
                .build();
    }
    private static Subscription buildSubscription() {
        return Subscription.builder()
                .id(1)
                .userId(1)
                .name("Ivan")
                .provider(Provider.APPLE)
                .expirationDate(Instant.MAX)
                .status(Status.ACTIVE)
                .build();
    }
}