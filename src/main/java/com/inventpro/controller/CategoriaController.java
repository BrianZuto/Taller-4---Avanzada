package com.inventpro.controller;

import com.inventpro.entity.Categoria;
import com.inventpro.dto.CategoriaRequest;
import com.inventpro.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/categorias")
@CrossOrigin(origins = {"http://localhost:4200", "http://13.61.142.123", "http://13.50.197.126", "http://localhost"})
public class CategoriaController {
    
    @Autowired
    private CategoriaService categoriaService;
    
    @GetMapping
    public ResponseEntity<List<Categoria>> getAllCategorias() {
        try {
            List<Categoria> categorias = categoriaService.findAll();
            System.out.println("Categorías activas encontradas: " + categorias.size());
            return ResponseEntity.ok(categorias);
        } catch (Exception e) {
            System.err.println("Error al obtener categorías: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Categoria> getCategoriaById(@PathVariable Long id) {
        Optional<Categoria> categoria = categoriaService.findById(id);
        return categoria.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<?> createCategoria(@Valid @RequestBody CategoriaRequest categoriaRequest) {
        try {
            if (categoriaService.existsByNombre(categoriaRequest.getNombre())) {
                Map<String, String> error = new HashMap<>();
                error.put("mensaje", "Ya existe una categoría con ese nombre");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }
            
            Categoria categoria = new Categoria();
            categoria.setNombre(categoriaRequest.getNombre());
            categoria.setDescripcion(categoriaRequest.getDescripcion());
            
            Categoria categoriaGuardada = categoriaService.save(categoria);
            return ResponseEntity.status(HttpStatus.CREATED).body(categoriaGuardada);
            
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Error al crear la categoría: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategoria(@PathVariable Long id, @Valid @RequestBody CategoriaRequest categoriaRequest) {
        try {
            Categoria categoria = new Categoria();
            categoria.setNombre(categoriaRequest.getNombre());
            categoria.setDescripcion(categoriaRequest.getDescripcion());
            
            Categoria categoriaActualizada = categoriaService.update(id, categoria);
            return ResponseEntity.ok(categoriaActualizada);
            
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Error al actualizar la categoría: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategoria(@PathVariable Long id) {
        try {
            categoriaService.delete(id);
            Map<String, String> response = new HashMap<>();
            response.put("mensaje", "Categoría eliminada exitosamente");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Error al eliminar la categoría: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    @GetMapping("/buscar")
    public ResponseEntity<List<Categoria>> buscarCategorias(@RequestParam(required = false) String busqueda) {
        List<Categoria> categorias = categoriaService.buscar(busqueda);
        return ResponseEntity.ok(categorias);
    }
    
    @GetMapping("/test")
    public ResponseEntity<?> test() {
        Map<String, String> response = new HashMap<>();
        response.put("mensaje", "Backend funcionando correctamente");
        response.put("timestamp", java.time.LocalDateTime.now().toString());
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/datos-prueba")
    public ResponseEntity<?> crearDatosPrueba() {
        try {
            // Crear categorías de prueba
            Categoria categoria1 = new Categoria();
            categoria1.setNombre("Electrónicos");
            categoria1.setDescripcion("Dispositivos electrónicos y tecnología");
            categoriaService.save(categoria1);
            
            Categoria categoria2 = new Categoria();
            categoria2.setNombre("Ropa");
            categoria2.setDescripcion("Vestimenta y accesorios");
            categoriaService.save(categoria2);
            
            Categoria categoria3 = new Categoria();
            categoria3.setNombre("Hogar");
            categoria3.setDescripcion("Artículos para el hogar");
            categoriaService.save(categoria3);
            
            Map<String, String> response = new HashMap<>();
            response.put("mensaje", "Datos de prueba creados exitosamente");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Error al crear datos de prueba: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}
