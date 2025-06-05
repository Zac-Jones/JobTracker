package dev.zac.jobTracker.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.ToString;

@ConfigurationProperties(prefix = "jwt")
@Validated
public record JwtProperties(
    @ToString.Exclude
    @NotBlank String secret,
    @Positive long expiration
) {}