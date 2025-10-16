package com.inventpro.controller;

import com.inventpro.entity.Producto;
import com.inventpro.dto.ProductoRequest;
import com.inventpro.service.ProductoService;
import com.inventpro.service.CategoriaService;
import com.inventpro.entity.Categoria;
import com.inventpro.repository.ProductoRepository;
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
@RequestMapping("/api/productos")
@CrossOrigin(origins = {"http://localhost:4200", "http://13.61.142.123", "http://13.50.197.126", "http://localhost"})
public class ProductoController {
    
    @Autowired
    private ProductoService productoService;
    
    @Autowired
    private CategoriaService categoriaService;
    
    @Autowired
    private ProductoRepository productoRepository;
    
    @GetMapping
    public ResponseEntity<?> getAllProductos() {
        try {
            List<Producto> productos = productoRepository.findByActivoTrue();
            System.out.println("Productos activos encontrados: " + productos.size());
            
            // Crear una respuesta simple sin la relación con categoría
            List<Map<String, Object>> productosSimples = new java.util.ArrayList<>();
            for (Producto producto : productos) {
                Map<String, Object> productoSimple = new HashMap<>();
                productoSimple.put("id", producto.getId());
                productoSimple.put("nombre", producto.getNombre());
                productoSimple.put("presentacion", producto.getPresentacion());
                productoSimple.put("precio", producto.getPrecio());
                productoSimple.put("stock", producto.getStock());
                productoSimple.put("stockMinimo", producto.getStockMinimo());
                productoSimple.put("impuesto", producto.getImpuesto());
                productoSimple.put("fechaCreacion", producto.getFechaCreacion());
                productoSimple.put("activo", producto.getActivo());
                // Solo incluir el ID de la categoría, no el objeto completo
                if (producto.getCategoria() != null) {
                    productoSimple.put("categoriaId", producto.getCategoria().getId());
                    productoSimple.put("categoriaNombre", producto.getCategoria().getNombre());
                }
                productosSimples.add(productoSimple);
            }
            
            return ResponseEntity.ok(productosSimples);
        } catch (Exception e) {
            System.err.println("Error al obtener productos: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/test")
    public ResponseEntity<?> test() {
        Map<String, String> response = new HashMap<>();
        response.put("mensaje", "Endpoint de productos funcionando correctamente");
        response.put("timestamp", java.time.LocalDateTime.now().toString());
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/datos-prueba")
    public ResponseEntity<?> crearDatosPrueba() {
        try {
            // Obtener la primera categoría disponible
            List<Categoria> categorias = categoriaService.findAll();
            if (categorias.isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("mensaje", "No hay categorías disponibles. Crea categorías primero.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }

            Categoria categoria = categorias.get(0);

            // Crear productos de prueba
            Producto producto1 = new Producto();
            producto1.setNombre("Detodito Azul");
            producto1.setPresentacion("Paquete");
            producto1.setPrecio(new java.math.BigDecimal("7000.00"));
            producto1.setStock(30);
            producto1.setStockMinimo(5);
            producto1.setImpuesto(new java.math.BigDecimal("9.00"));
            producto1.setCategoria(categoria);
            productoService.save(producto1);

            Producto producto2 = new Producto();
            producto2.setNombre("Coca Cola");
            producto2.setPresentacion("Botella 500ml");
            producto2.setPrecio(new java.math.BigDecimal("3500.00"));
            producto2.setStock(50);
            producto2.setStockMinimo(10);
            producto2.setImpuesto(new java.math.BigDecimal("19.00"));
            producto2.setCategoria(categoria);
            productoService.save(producto2);

            Map<String, String> response = new HashMap<>();
            response.put("mensaje", "Productos de prueba creados exitosamente");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Error al crear productos de prueba: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @PostMapping("/comprar")
    public ResponseEntity<?> comprarProducto(@Valid @RequestBody com.inventpro.dto.CompraRequest compraRequest) {
        try {
            System.out.println("🛒 Procesando compra para producto ID: " + compraRequest.getProductoId() + 
                             ", cantidad: " + compraRequest.getCantidad());
            
            // Buscar el producto
            Optional<Producto> productoOpt = productoService.findById(compraRequest.getProductoId());
            if (productoOpt.isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("mensaje", "Producto no encontrado con ID: " + compraRequest.getProductoId());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }
            
            Producto producto = productoOpt.get();
            
            // Verificar que el producto esté activo
            if (!producto.isActivo()) {
                Map<String, String> error = new HashMap<>();
                error.put("mensaje", "El producto no está disponible para compra");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }
            
            // Actualizar el stock
            int nuevoStock = producto.getStock() + compraRequest.getCantidad();
            producto.setStock(nuevoStock);
            
            // Guardar el producto actualizado
            Producto productoActualizado = productoService.save(producto);
            
            System.out.println("✅ Compra procesada exitosamente. Nuevo stock: " + nuevoStock);
            
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Compra procesada exitosamente");
            response.put("producto", productoActualizado.getNombre());
            response.put("cantidadComprada", compraRequest.getCantidad());
            response.put("stockAnterior", producto.getStock() - compraRequest.getCantidad());
            response.put("stockActual", nuevoStock);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("❌ Error al procesar compra: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Error al procesar la compra: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Producto> getProductoById(@PathVariable Long id) {
        Optional<Producto> producto = productoService.findById(id);
        return producto.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<?> createProducto(@Valid @RequestBody ProductoRequest productoRequest) {
        try {
            Optional<Categoria> categoria = categoriaService.findById(productoRequest.getCategoriaId());
            if (categoria.isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("mensaje", "La categoría especificada no existe");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }
            
            Producto producto = new Producto();
            producto.setNombre(productoRequest.getNombre());
            producto.setPresentacion(productoRequest.getPresentacion());
            producto.setPrecio(productoRequest.getPrecio());
            producto.setStock(productoRequest.getStock());
            producto.setStockMinimo(productoRequest.getStockMinimo());
            producto.setImpuesto(productoRequest.getImpuesto());
            producto.setCategoria(categoria.get());
            
            Producto productoGuardado = productoService.save(producto);
            return ResponseEntity.status(HttpStatus.CREATED).body(productoGuardado);
            
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Error al crear el producto: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProducto(@PathVariable Long id, @Valid @RequestBody ProductoRequest productoRequest) {
        try {
            Optional<Categoria> categoria = categoriaService.findById(productoRequest.getCategoriaId());
            if (categoria.isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("mensaje", "La categoría especificada no existe");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }
            
            Producto producto = new Producto();
            producto.setNombre(productoRequest.getNombre());
            producto.setPresentacion(productoRequest.getPresentacion());
            producto.setPrecio(productoRequest.getPrecio());
            producto.setStock(productoRequest.getStock());
            producto.setStockMinimo(productoRequest.getStockMinimo());
            producto.setImpuesto(productoRequest.getImpuesto());
            producto.setCategoria(categoria.get());
            
            Producto productoActualizado = productoService.update(id, producto);
            return ResponseEntity.ok(productoActualizado);
            
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Error al actualizar el producto: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProducto(@PathVariable Long id) {
        try {
            System.out.println("🗑️ Eliminando producto con ID: " + id);
            
            // Verificar si el producto existe
            Optional<Producto> productoExistente = productoService.findById(id);
            if (productoExistente.isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("mensaje", "Producto no encontrado con ID: " + id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }
            
            // Eliminar el producto
            productoService.delete(id);
            System.out.println("✅ Producto eliminado exitosamente");
            
            Map<String, String> response = new HashMap<>();
            response.put("mensaje", "Producto eliminado exitosamente");
            response.put("id", id.toString());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("❌ Error al eliminar producto: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Error al eliminar el producto: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    @GetMapping("/buscar")
    public ResponseEntity<List<Producto>> buscarProductos(@RequestParam(required = false) String busqueda) {
        List<Producto> productos = productoService.buscar(busqueda);
        return ResponseEntity.ok(productos);
    }
    
    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<Producto>> getProductosByCategoria(@PathVariable Long categoriaId) {
        List<Producto> productos = productoService.findByCategoria(categoriaId);
        return ResponseEntity.ok(productos);
    }
    
    @GetMapping("/stock-bajo")
    public ResponseEntity<List<Producto>> getProductosStockBajo() {
        List<Producto> productos = productoService.encontrarStockBajo();
        return ResponseEntity.ok(productos);
    }
    
}
