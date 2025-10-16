package com.inventpro.controller;

import com.inventpro.service.EstadisticasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/estadisticas")
@CrossOrigin(origins = {"http://localhost:4200", "http://13.61.142.123", "http://localhost"})
public class EstadisticasController {

    @Autowired
    private EstadisticasService estadisticasService;

    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboardData() {
        try {
            Map<String, Object> dashboardData = estadisticasService.getDashboardData();
            return ResponseEntity.ok(dashboardData);
        } catch (Exception e) {
            System.err.println("Error al obtener datos del dashboard: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/ventas")
    public ResponseEntity<Map<String, Object>> getVentasEstadisticas() {
        try {
            Map<String, Object> ventasData = estadisticasService.getVentasEstadisticas();
            return ResponseEntity.ok(ventasData);
        } catch (Exception e) {
            System.err.println("Error al obtener estadísticas de ventas: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/productos")
    public ResponseEntity<Map<String, Object>> getProductosEstadisticas() {
        try {
            Map<String, Object> productosData = estadisticasService.getProductosEstadisticas();
            return ResponseEntity.ok(productosData);
        } catch (Exception e) {
            System.err.println("Error al obtener estadísticas de productos: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/inventario")
    public ResponseEntity<Map<String, Object>> getInventarioEstadisticas() {
        try {
            Map<String, Object> inventarioData = estadisticasService.getInventarioEstadisticas();
            return ResponseEntity.ok(inventarioData);
        } catch (Exception e) {
            System.err.println("Error al obtener estadísticas de inventario: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
}
