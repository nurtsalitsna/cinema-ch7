package com.spring.binar.challenge_5.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "binar.spring")
public class ApplicationProperties {
    private String jwtSecret;
    private int jwtExpirationMs;
    private int jwtRefreshExpirationMs;
    private String cloudinaryCloudName;
    private String cloudinaryApiKey;
    private String cloudinaryApiSecret;
}
