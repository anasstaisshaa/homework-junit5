package com.dmdev.validator;

import com.dmdev.dto.CreateSubscriptionDto;
import com.dmdev.entity.Provider;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CreateSubscriptionValidatorTest {
    private final CreateSubscriptionValidator validator = CreateSubscriptionValidator.getInstance();

    @Test
    void validateSuccess() {
        CreateSubscriptionDto dto = CreateSubscriptionDto.builder()
                .userId(1)
                .name("Ivan")
                .provider("Apple")
                .expirationDate(Instant.MAX)
                .build();
        ValidationResult actualResult = validator.validate(dto);
        assertFalse(actualResult.hasErrors());
    }
    @Test
    void invalidUserId(){
        CreateSubscriptionDto dto = CreateSubscriptionDto.builder()
                .userId(null)
                .name("Ivan")
                .provider("Apple")
                .expirationDate(Instant.MAX)
                .build();
        ValidationResult actualResult = validator.validate(dto);
        assertThat(actualResult.getErrors()).hasSize(1);
        assertThat(actualResult.getErrors().get(0).getCode()).isEqualTo(100);
        assertThat(actualResult.getErrors().get(0).getMessage()).isEqualTo("userId is invalid");
    }
    @Test
    void invalidName(){
        CreateSubscriptionDto dto = CreateSubscriptionDto.builder()
                .userId(1)
                .name(null)
                .provider("Apple")
                .expirationDate(Instant.MAX)
                .build();
        ValidationResult actualResult = validator.validate(dto);
        assertThat(actualResult.getErrors()).hasSize(1);
        assertThat(actualResult.getErrors().get(0).getCode()).isEqualTo(101);
        assertThat(actualResult.getErrors().get(0).getMessage()).isEqualTo("name is invalid");
    }
    @Test
    void invalidProvider(){
        CreateSubscriptionDto dto = CreateSubscriptionDto.builder()
                .userId(1)
                .name("Ivan")
                .provider("")
                .expirationDate(Instant.MAX)
                .build();
        ValidationResult actualResult = validator.validate(dto);
        assertThat(actualResult.getErrors()).hasSize(1);
        assertThat(actualResult.getErrors().get(0).getCode()).isEqualTo(102);
        assertThat(actualResult.getErrors().get(0).getMessage()).isEqualTo("provider is invalid");
    }
    @Test
    void invalidDate(){
        CreateSubscriptionDto dto = CreateSubscriptionDto.builder()
                .userId(1)
                .name("Ivan")
                .provider("Apple")
                .expirationDate(Instant.MIN)
                .build();
        ValidationResult actualResult = validator.validate(dto);
        assertThat(actualResult.getErrors()).hasSize(1);
        assertThat(actualResult.getErrors().get(0).getCode()).isEqualTo(103);
        assertThat(actualResult.getErrors().get(0).getMessage()).isEqualTo("expirationDate is invalid");
    }
}