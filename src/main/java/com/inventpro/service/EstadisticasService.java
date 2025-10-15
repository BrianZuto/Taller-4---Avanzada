package com.inventpro.service;

import com.inventpro.repository.VentaRepository;
import com.inventpro.repository.ProductoRepository;
import com.inventpro.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class EstadisticasService {

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    public Map<String, Object> getDashboardData() {
        Map<String, Object> dashboard = new HashMap<>();
        
        // Estadísticas generales
        long totalVentas = ventaRepository.count();
        long totalProductos = productoRepository.count();
        long totalCategorias = categoriaRepository.count();
        
        // Ventas del día
        LocalDateTime inicioDia = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime finDia = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);
        BigDecimal ventasHoy = ventaRepository.sumTotalVentasByDateRange(inicioDia, finDia);
        if (ventasHoy == null) ventasHoy = BigDecimal.ZERO;
        
        // Productos con stock bajo
        long productosStockBajo = productoRepository.countByStockLessThanAndActivoTrue(10);
        
        // Top productos vendidos
        List<Map<String, Object>> topProductos = ventaRepository.findTopSellingProducts();
        
        dashboard.put("totalVentas", totalVentas);
        dashboard.put("totalProductos", totalProductos);
        dashboard.put("totalCategorias", totalCategorias);
        dashboard.put("ventasHoy", ventasHoy);
        dashboard.put("productosStockBajo", productosStockBajo);
        dashboard.put("topProductos", topProductos);
        
        return dashboard;
    }

    public Map<String, Object> getVentasEstadisticas() {
        Map<String, Object> ventasData = new HashMap<>();
        
        // Ventas por día de la última semana
        List<Map<String, Object>> ventasPorDia = new ArrayList<>();
        for (int i = 6; i >= 0; i--) {
            LocalDateTime fecha = LocalDateTime.now().minusDays(i);
            LocalDateTime inicioDia = fecha.withHour(0).withMinute(0).withSecond(0);
            LocalDateTime finDia = fecha.withHour(23).withMinute(59).withSecond(59);
            
            BigDecimal totalDia = ventaRepository.sumTotalVentasByDateRange(inicioDia, finDia);
            if (totalDia == null) totalDia = BigDecimal.ZERO;
            
            Map<String, Object> diaData = new HashMap<>();
            diaData.put("fecha", fecha.format(DateTimeFormatter.ofPattern("dd/MM")));
            diaData.put("total", totalDia);
            ventasPorDia.add(diaData);
        }
        
        // Ventas por mes del último año
        List<Map<String, Object>> ventasPorMes = new ArrayList<>();
        for (int i = 11; i >= 0; i--) {
            LocalDateTime fecha = LocalDateTime.now().minusMonths(i);
            LocalDateTime inicioMes = fecha.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
            LocalDateTime finMes = fecha.withDayOfMonth(fecha.toLocalDate().lengthOfMonth()).withHour(23).withMinute(59).withSecond(59);
            
            BigDecimal totalMes = ventaRepository.sumTotalVentasByDateRange(inicioMes, finMes);
            if (totalMes == null) totalMes = BigDecimal.ZERO;
            
            Map<String, Object> mesData = new HashMap<>();
            mesData.put("mes", fecha.format(DateTimeFormatter.ofPattern("MM/yyyy")));
            mesData.put("total", totalMes);
            ventasPorMes.add(mesData);
        }
        
        ventasData.put("ventasPorDia", ventasPorDia);
        ventasData.put("ventasPorMes", ventasPorMes);
        ventasData.put("topProductos", ventaRepository.findTopSellingProducts());
        
        return ventasData;
    }

    public Map<String, Object> getProductosEstadisticas() {
        Map<String, Object> productosData = new HashMap<>();
        
        // Productos por categoría
        List<Map<String, Object>> productosPorCategoria = new ArrayList<>();
        List<Object[]> categoriasConProductos = productoRepository.countProductosByCategoria();
        
        for (Object[] row : categoriasConProductos) {
            Map<String, Object> categoriaData = new HashMap<>();
            categoriaData.put("categoria", row[0]);
            categoriaData.put("cantidad", row[1]);
            productosPorCategoria.add(categoriaData);
        }
        
        // Productos con stock bajo
        List<Map<String, Object>> productosStockBajo = new ArrayList<>();
        List<Object[]> stockBajo = productoRepository.findProductosStockBajo();
        
        for (Object[] row : stockBajo) {
            Map<String, Object> productoData = new HashMap<>();
            productoData.put("nombre", row[0]);
            productoData.put("stock", row[1]);
            productoData.put("stockMinimo", row[2]);
            productosStockBajo.add(productoData);
        }
        
        productosData.put("productosPorCategoria", productosPorCategoria);
        productosData.put("productosStockBajo", productosStockBajo);
        
        return productosData;
    }

    public Map<String, Object> getInventarioEstadisticas() {
        Map<String, Object> inventarioData = new HashMap<>();
        
        // Valor total del inventario
        BigDecimal valorTotalInventario = productoRepository.sumValorTotalInventario();
        if (valorTotalInventario == null) valorTotalInventario = BigDecimal.ZERO;
        
        // Productos por estado de stock
        long productosStockAlto = productoRepository.countByStockGreaterThanAndActivoTrue(50);
        long productosStockMedio = productoRepository.countByStockBetweenAndActivoTrue(10, 50);
        long productosStockBajo = productoRepository.countByStockLessThanAndActivoTrue(10);
        
        inventarioData.put("valorTotalInventario", valorTotalInventario);
        inventarioData.put("productosStockAlto", productosStockAlto);
        inventarioData.put("productosStockMedio", productosStockMedio);
        inventarioData.put("productosStockBajo", productosStockBajo);
        
        return inventarioData;
    }
}
