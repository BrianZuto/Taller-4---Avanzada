package com.inventpro.repository;

import com.inventpro.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    
    List<Producto> findByActivoTrue();
    
    List<Producto> findByCategoriaIdAndActivoTrue(Long categoriaId);
    
    @Query("SELECT p FROM Producto p WHERE p.activo = true AND " +
           "(LOWER(p.nombre) LIKE LOWER(CONCAT('%', :busqueda, '%')) OR " +
           "LOWER(p.presentacion) LIKE LOWER(CONCAT('%', :busqueda, '%')))")
    List<Producto> buscarPorNombreOPresentacion(String busqueda);
    
    @Query("SELECT p FROM Producto p WHERE p.activo = true AND p.stock <= p.stockMinimo")
    List<Producto> encontrarStockBajo();
    
    @Query("SELECT COUNT(p) FROM Producto p WHERE p.categoria.id = :categoriaId AND p.activo = true")
    Long contarProductosPorCategoria(Long categoriaId);
    
    // Métodos para estadísticas
    long countByStockLessThanAndActivoTrue(int stock);
    long countByStockGreaterThanAndActivoTrue(int stock);
    long countByStockBetweenAndActivoTrue(int stockMin, int stockMax);
    
    @Query("SELECT c.nombre, COUNT(p) FROM Producto p JOIN p.categoria c WHERE p.activo = true GROUP BY c.nombre")
    List<Object[]> countProductosByCategoria();
    
    @Query("SELECT p.nombre, p.stock, p.stockMinimo FROM Producto p WHERE p.activo = true AND p.stock < p.stockMinimo")
    List<Object[]> findProductosStockBajo();
    
    @Query("SELECT SUM(p.precio * p.stock) FROM Producto p WHERE p.activo = true")
    BigDecimal sumValorTotalInventario();
}
