package com.inventpro.repository;

import com.inventpro.entity.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {
    
    // Buscar ventas activas
    List<Venta> findByActivoTrue();
    
    // Buscar ventas por cliente
    List<Venta> findByClienteContainingIgnoreCaseAndActivoTrue(String cliente);
    
    // Buscar ventas por rango de fechas
    List<Venta> findByFechaVentaBetweenAndActivoTrue(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    
    // Buscar ventas por cliente y rango de fechas
    List<Venta> findByClienteContainingIgnoreCaseAndFechaVentaBetweenAndActivoTrue(
        String cliente, LocalDateTime fechaInicio, LocalDateTime fechaFin);
    
    // Contar ventas activas
    long countByActivoTrue();
    
    // Sumar total de ventas activas
    @Query("SELECT COALESCE(SUM(v.total), 0) FROM Venta v WHERE v.activo = true")
    Double sumTotalVentasActivas();
    
    // Buscar ventas por texto (cliente)
    @Query("SELECT v FROM Venta v WHERE v.activo = true AND " +
           "(LOWER(v.cliente) LIKE LOWER(CONCAT('%', :busqueda, '%')))")
    List<Venta> buscarVentasPorTexto(@Param("busqueda") String busqueda);
    
    // Métodos para estadísticas
    @Query("SELECT COALESCE(SUM(v.total), 0) FROM Venta v WHERE v.activo = true AND v.fechaVenta >= :startDate AND v.fechaVenta <= :endDate")
    BigDecimal sumTotalVentasByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT new map(p.nombre as producto, SUM(vp.cantidad) as cantidadVendida) FROM Venta v JOIN v.productos vp JOIN vp.producto p WHERE v.activo = true GROUP BY p.nombre ORDER BY SUM(vp.cantidad) DESC")
    List<Map<String, Object>> findTopSellingProducts();
}
