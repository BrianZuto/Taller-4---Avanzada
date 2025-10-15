package com.inventpro.service;

import com.inventpro.entity.Categoria;
import com.inventpro.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {
    
    @Autowired
    private CategoriaRepository categoriaRepository;
    
    public List<Categoria> findAll() {
        return categoriaRepository.findByActivoTrue();
    }
    
    public Optional<Categoria> findById(Long id) {
        return categoriaRepository.findById(id);
    }
    
    public Categoria save(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }
    
    public Categoria update(Long id, Categoria categoriaActualizada) {
        return categoriaRepository.findById(id)
                .map(categoria -> {
                    categoria.setNombre(categoriaActualizada.getNombre());
                    categoria.setDescripcion(categoriaActualizada.getDescripcion());
                    return categoriaRepository.save(categoria);
                })
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + id));
    }
    
    public void delete(Long id) {
        categoriaRepository.findById(id)
                .ifPresent(categoria -> {
                    categoria.setActivo(false);
                    categoriaRepository.save(categoria);
                });
    }
    
    public List<Categoria> buscar(String busqueda) {
        if (busqueda == null || busqueda.trim().isEmpty()) {
            return findAll();
        }
        return categoriaRepository.buscarPorNombreODescripcion(busqueda.trim());
    }
    
    public boolean existsByNombre(String nombre) {
        return categoriaRepository.existsByNombreAndActivoTrue(nombre);
    }
}
