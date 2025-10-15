package com.inventpro.dto;

public class AuthResponse {
    
    private String token;
    private String tipo = "Bearer";
    private Long id;
    private String nombreCompleto;
    private String email;
    
    // Constructores
    public AuthResponse() {}
    
    public AuthResponse(String token, Long id, String nombreCompleto, String email) {
        this.token = token;
        this.id = id;
        this.nombreCompleto = nombreCompleto;
        this.email = email;
    }
    
    // Getters y Setters
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public String getTipo() {
        return tipo;
    }
    
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getNombreCompleto() {
        return nombreCompleto;
    }
    
    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
}
