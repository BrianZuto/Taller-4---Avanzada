package com.inventpro.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Component
@ConfigurationProperties(prefix = "jwt")
@Validated
public class JwtProperties {
    
    @NotBlank(message = "La clave secreta JWT no puede estar vacía")
    @Size(min = 32, message = "La clave secreta JWT debe tener al menos 32 caracteres (256 bits) para ser segura")
    private String secret;
    
    private Long expiration = 86400000L; // 24 horas por defecto
    
    // Constructores
    public JwtProperties() {}
    
    // Getters y Setters
    public String getSecret() {
        return secret;
    }
    
    public void setSecret(String secret) {
        this.secret = secret;
    }
    
    public Long getExpiration() {
        return expiration;
    }
    
    public void setExpiration(Long expiration) {
        this.expiration = expiration;
    }
}
