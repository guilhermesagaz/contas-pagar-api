package com.br.contasapagar.infrastructure.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Validated
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "authentication")
public class AuthenticationProperties {

    private String secretKey;
    private TokenDetails accessToken;
    private TokenDetails refreshToken;

    @Getter
    @Setter
    public static class TokenDetails {
        private Integer expirationInSeconds;
    }
}
