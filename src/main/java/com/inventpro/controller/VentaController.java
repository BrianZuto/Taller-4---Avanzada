package com.inventpro.controller;

import com.inventpro.entity.Venta;
import com.inventpro.entity.VentaProducto;
import com.inventpro.entity.Producto;
import com.inventpro.dto.VentaRequest;
import com.inventpro.dto.VentaProductoRequest;
import com.inventpro.service.VentaService;
import com.inventpro.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/ventas")
@CrossOrigin(origins = {"http://localhost:4200", "http://13.61.142.123", "http://13.50.197.126", "http://localhost"})
public class VentaController {

    @Autowired
    private VentaService ventaService;

    @Autowired
    private ProductoService productoService;

    @GetMapping
    public ResponseEntity<?> getAllVentas() {
        try {
            List<Venta> ventas = ventaService.findAll();
            System.out.println("Ventas activas encontradas: " + ventas.size());
            
            // Crear una respuesta simplificada con los productos incluidos
            List<Map<String, Object>> ventasConProductos = new ArrayList<>();
            for (Venta venta : ventas) {
                Map<String, Object> ventaMap = new HashMap<>();
                ventaMap.put("id", venta.getId());
                ventaMap.put("cliente", venta.getCliente());
                ventaMap.put("total", venta.getTotal());
                ventaMap.put("fechaVenta", venta.getFechaVenta());
                ventaMap.put("activo", venta.isActivo());
                
                // Incluir los productos de la venta
                List<Map<String, Object>> productosList = new ArrayList<>();
                for (VentaProducto ventaProducto : venta.getProductos()) {
                    Map<String, Object> productoMap = new HashMap<>();
                    productoMap.put("productoNombre", ventaProducto.getProducto().getNombre());
                    productoMap.put("cantidad", ventaProducto.getCantidad());
                    productoMap.put("precioUnitario", ventaProducto.getPrecioUnitario());
                    productoMap.put("subtotal", ventaProducto.getSubtotal());
                    productosList.add(productoMap);
                }
                ventaMap.put("productos", productosList);
                ventasConProductos.add(ventaMap);
            }
            
            return ResponseEntity.ok(ventasConProductos);
        } catch (Exception e) {
            System.err.println("Error al obtener ventas: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/test")
    public ResponseEntity<?> test() {
        Map<String, String> response = new HashMap<>();
        response.put("mensaje", "Endpoint de ventas funcionando correctamente");
        response.put("timestamp", java.time.LocalDateTime.now().toString());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Venta> getVentaById(@PathVariable Long id) {
        Optional<Venta> venta = ventaService.findById(id);
        return venta.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createVenta(@Valid @RequestBody VentaRequest ventaRequest) {
        try {
            System.out.println("=== CREANDO VENTA ===");
            System.out.println("Cliente: " + ventaRequest.getCliente());
            System.out.println("Productos: " + ventaRequest.getProductos().size());

            Venta venta = new Venta();
            venta.setCliente(ventaRequest.getCliente());

            // Crear los productos de la venta
            for (VentaProductoRequest productoRequest : ventaRequest.getProductos()) {
                System.out.println("Procesando producto ID: " + productoRequest.getProductoId());
                
                Optional<Producto> productoOpt = productoService.findById(productoRequest.getProductoId());
                if (productoOpt.isEmpty()) {
                    System.out.println("Producto no encontrado: " + productoRequest.getProductoId());
                    Map<String, String> error = new HashMap<>();
                    error.put("mensaje", "Producto no encontrado con ID: " + productoRequest.getProductoId());
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
                }

                Producto producto = productoOpt.get();
                System.out.println("Producto encontrado: " + producto.getNombre() + ", Stock: " + producto.getStock());
                
                if (!producto.isActivo()) {
                    System.out.println("Producto inactivo: " + producto.getNombre());
                    Map<String, String> error = new HashMap<>();
                    error.put("mensaje", "El producto " + producto.getNombre() + " no está disponible");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
                }

                VentaProducto ventaProducto = new VentaProducto();
                ventaProducto.setVenta(venta);
                ventaProducto.setProducto(producto);
                ventaProducto.setCantidad(productoRequest.getCantidad());
                ventaProducto.setPrecioUnitario(producto.getPrecio());
                ventaProducto.setSubtotal(producto.getPrecio().multiply(java.math.BigDecimal.valueOf(productoRequest.getCantidad())));

                venta.getProductos().add(ventaProducto);
                System.out.println("Producto agregado a la venta: " + producto.getNombre());
            }

            System.out.println("Guardando venta...");
            Venta ventaGuardada = ventaService.save(venta);
            System.out.println("=== VENTA CREADA EXITOSAMENTE ===");
            return ResponseEntity.status(HttpStatus.CREATED).body(ventaGuardada);

        } catch (Exception e) {
            System.err.println("=== ERROR AL CREAR VENTA ===");
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Error al crear la venta: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateVenta(@PathVariable Long id, @Valid @RequestBody VentaRequest ventaRequest) {
        try {
            Venta venta = new Venta();
            venta.setCliente(ventaRequest.getCliente());

            // Crear los productos de la venta
            for (VentaProductoRequest productoRequest : ventaRequest.getProductos()) {
                Optional<Producto> productoOpt = productoService.findById(productoRequest.getProductoId());
                if (productoOpt.isEmpty()) {
                    Map<String, String> error = new HashMap<>();
                    error.put("mensaje", "Producto no encontrado con ID: " + productoRequest.getProductoId());
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
                }

                Producto producto = productoOpt.get();
                VentaProducto ventaProducto = new VentaProducto();
                ventaProducto.setVenta(venta);
                ventaProducto.setProducto(producto);
                ventaProducto.setCantidad(productoRequest.getCantidad());
                ventaProducto.setPrecioUnitario(producto.getPrecio());
                ventaProducto.setSubtotal(producto.getPrecio().multiply(java.math.BigDecimal.valueOf(productoRequest.getCantidad())));

                venta.getProductos().add(ventaProducto);
            }

            Venta ventaActualizada = ventaService.update(id, venta);
            return ResponseEntity.ok(ventaActualizada);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Error al actualizar la venta: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteVenta(@PathVariable Long id) {
        try {
            System.out.println("🗑️ Eliminando venta con ID: " + id);
            ventaService.delete(id);
            System.out.println("✅ Venta eliminada exitosamente");

            Map<String, String> response = new HashMap<>();
            response.put("mensaje", "Venta eliminada exitosamente");
            response.put("id", id.toString());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.err.println("❌ Error al eliminar venta: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Error al eliminar la venta: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Venta>> buscarVentas(@RequestParam(required = false) String busqueda) {
        List<Venta> ventas = ventaService.buscar(busqueda);
        return ResponseEntity.ok(ventas);
    }

    @GetMapping("/estadisticas")
    public ResponseEntity<?> getEstadisticas() {
        try {
            Map<String, Object> estadisticas = new HashMap<>();
            estadisticas.put("totalVentas", ventaService.countVentas());
            estadisticas.put("totalIngresos", ventaService.getTotalVentas());
            return ResponseEntity.ok(estadisticas);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Error al obtener estadísticas: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}
