package com.inventpro.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class JwtConfigValidator {
    
    @Autowired
    private JwtProperties jwtProperties;
    
    @EventListener(ApplicationReadyEvent.class)
    public void validateJwtConfiguration() {
        try {
            String secret = jwtProperties.getSecret();
            
            if (secret == null || secret.trim().isEmpty()) {
                throw new IllegalStateException(
                    "❌ ERROR DE CONFIGURACIÓN JWT:\n" +
                    "La clave secreta JWT no está configurada.\n" +
                    "Por favor, configura 'jwt.secret' en application.properties"
                );
            }
            
            if (secret.length() < 32) {
                throw new IllegalStateException(
                    "❌ ERROR DE CONFIGURACIÓN JWT:\n" +
                    "La clave secreta JWT es demasiado corta.\n" +
                    "Longitud actual: " + secret.length() + " caracteres\n" +
                    "Longitud mínima requerida: 32 caracteres (256 bits)\n" +
                    "Por favor, cambia 'jwt.secret' en application.properties por una clave más larga y segura.\n" +
                    "Ejemplo: 'jwt.secret=inventproSecretKey2024VeryLongAndSecureKeyForJWTTokenGeneration'"
                );
            }
            
            System.out.println("✅ CONFIGURACIÓN JWT VÁLIDA:");
            System.out.println("   - Clave secreta: " + secret.length() + " caracteres (segura)");
            System.out.println("   - Expiración: " + (jwtProperties.getExpiration() / 1000 / 60 / 60) + " horas");
            
        } catch (Exception e) {
            System.err.println("\n" + e.getMessage());
            System.err.println("\n🔧 SOLUCIÓN:");
            System.err.println("1. Abre el archivo: src/main/resources/application.properties");
            System.err.println("2. Cambia la línea 'jwt.secret=' por una clave de al menos 32 caracteres");
            System.err.println("3. Reinicia la aplicación");
            System.err.println("\n💡 EJEMPLO DE CLAVE VÁLIDA:");
            System.err.println("jwt.secret=inventproSecretKey2024VeryLongAndSecureKeyForJWTTokenGeneration");
            
            // Detener la aplicación si la configuración es inválida
            System.exit(1);
        }
    }
}
