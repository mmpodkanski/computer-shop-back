package io.github.mmpodkanski.computershop;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "app")
public class AppConfig {
    private String jwtSecret;
    private String jwtExpirationMs;

    public String getJwtSecret() {
        return jwtSecret;
    }

    public String getJwtExpirationMs() {
        return jwtExpirationMs;
    }

    void setJwtSecret(String jwtSecret) {
        this.jwtSecret = jwtSecret;
    }

    void setJwtExpirationMs(String jwtExpirationMs) {
        this.jwtExpirationMs = jwtExpirationMs;
    }
}
