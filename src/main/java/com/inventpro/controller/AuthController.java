package com.inventpro.controller;

import com.inventpro.dto.AuthResponse;
import com.inventpro.dto.LoginRequest;
import com.inventpro.dto.RegisterRequest;
import com.inventpro.entity.Usuario;
import com.inventpro.security.JwtUtil;
import com.inventpro.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {"http://localhost:4200", "http://13.61.142.123", "http://localhost"})
public class AuthController {
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(),
                    loginRequest.getPassword()
                )
            );
            
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtil.generateToken((Usuario) authentication.getPrincipal());
            
            Usuario usuario = (Usuario) authentication.getPrincipal();
            AuthResponse authResponse = new AuthResponse(
                jwt,
                usuario.getId(),
                usuario.getNombreCompleto(),
                usuario.getEmail()
            );
            
            return ResponseEntity.ok(authResponse);
            
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Credenciales inválidas");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }
    
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            System.out.println("=== INICIO REGISTRO ===");
            System.out.println("Datos recibidos:");
            System.out.println("Nombre: " + registerRequest.getNombreCompleto());
            System.out.println("Email: " + registerRequest.getEmail());
            System.out.println("Password: " + (registerRequest.getPassword() != null ? "***" : "null"));
            
            if (usuarioService.existsByEmail(registerRequest.getEmail())) {
                System.out.println("Email ya existe: " + registerRequest.getEmail());
                Map<String, String> error = new HashMap<>();
                error.put("mensaje", "El email ya está registrado");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }
            
            Usuario usuario = new Usuario();
            usuario.setNombreCompleto(registerRequest.getNombreCompleto());
            usuario.setEmail(registerRequest.getEmail());
            usuario.setPassword(registerRequest.getPassword());
            
            System.out.println("Usuario creado, guardando...");
            Usuario usuarioGuardado = usuarioService.save(usuario);
            System.out.println("Usuario guardado con ID: " + usuarioGuardado.getId());
            
            String jwt = jwtUtil.generateToken(usuarioGuardado);
            System.out.println("JWT generado exitosamente");
            
            AuthResponse authResponse = new AuthResponse(
                jwt,
                usuarioGuardado.getId(),
                usuarioGuardado.getNombreCompleto(),
                usuarioGuardado.getEmail()
            );
            
            System.out.println("=== REGISTRO EXITOSO ===");
            return ResponseEntity.status(HttpStatus.CREATED).body(authResponse);
            
        } catch (Exception e) {
            System.err.println("=== ERROR EN REGISTRO ===");
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Error al crear el usuario: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(Authentication authentication) {
        try {
            Usuario usuario = (Usuario) authentication.getPrincipal();
            Map<String, Object> profile = new HashMap<>();
            profile.put("id", usuario.getId());
            profile.put("nombreCompleto", usuario.getNombreCompleto());
            profile.put("email", usuario.getEmail());
            profile.put("fechaCreacion", usuario.getFechaCreacion());
            
            return ResponseEntity.ok(profile);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Error al obtener el perfil");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}
