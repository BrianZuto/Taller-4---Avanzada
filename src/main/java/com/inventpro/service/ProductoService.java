package com.inventpro.service;

import com.inventpro.entity.Producto;
import com.inventpro.entity.Categoria;
import com.inventpro.repository.ProductoRepository;
import com.inventpro.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {
    
    @Autowired
    private ProductoRepository productoRepository;
    
    @Autowired
    private CategoriaRepository categoriaRepository;
    
    public List<Producto> findAll() {
        return productoRepository.findByActivoTrue();
    }
    
    public Optional<Producto> findById(Long id) {
        return productoRepository.findById(id);
    }
    
    public Producto save(Producto producto) {
        return productoRepository.save(producto);
    }
    
    public Producto update(Long id, Producto productoActualizado) {
        return productoRepository.findById(id)
                .map(producto -> {
                    producto.setNombre(productoActualizado.getNombre());
                    producto.setPresentacion(productoActualizado.getPresentacion());
                    producto.setPrecio(productoActualizado.getPrecio());
                    producto.setStock(productoActualizado.getStock());
                    producto.setStockMinimo(productoActualizado.getStockMinimo());
                    producto.setImpuesto(productoActualizado.getImpuesto());
                    producto.setCategoria(productoActualizado.getCategoria());
                    return productoRepository.save(producto);
                })
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));
    }
    
    public void delete(Long id) {
        productoRepository.findById(id)
                .ifPresent(producto -> {
                    producto.setActivo(false);
                    productoRepository.save(producto);
                });
    }
    
    public List<Producto> buscar(String busqueda) {
        if (busqueda == null || busqueda.trim().isEmpty()) {
            return findAll();
        }
        return productoRepository.buscarPorNombreOPresentacion(busqueda.trim());
    }
    
    public List<Producto> findByCategoria(Long categoriaId) {
        return productoRepository.findByCategoriaIdAndActivoTrue(categoriaId);
    }
    
    public List<Producto> encontrarStockBajo() {
        return productoRepository.encontrarStockBajo();
    }
    
    public Long contarProductosPorCategoria(Long categoriaId) {
        return productoRepository.contarProductosPorCategoria(categoriaId);
    }
}
