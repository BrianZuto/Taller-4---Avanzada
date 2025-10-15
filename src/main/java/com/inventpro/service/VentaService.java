package com.inventpro.service;

import com.inventpro.entity.Venta;
import com.inventpro.entity.VentaProducto;
import com.inventpro.entity.Producto;
import com.inventpro.repository.VentaRepository;
import com.inventpro.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class VentaService {

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    public List<Venta> findAll() {
        List<Venta> ventas = ventaRepository.findByActivoTrue();
        // Cargar los productos de cada venta
        for (Venta venta : ventas) {
            venta.getProductos().size(); // Esto fuerza la carga de los productos
        }
        return ventas;
    }

    public Optional<Venta> findById(Long id) {
        return ventaRepository.findById(id);
    }

    public Venta save(Venta venta) {
        // Calcular el total de la venta
        calcularTotalVenta(venta);
        
        // Actualizar el stock de los productos
        actualizarStockProductos(venta);
        
        return ventaRepository.save(venta);
    }

    public Venta update(Long id, Venta venta) {
        Optional<Venta> ventaExistente = ventaRepository.findById(id);
        if (ventaExistente.isPresent()) {
            Venta ventaActual = ventaExistente.get();
            
            // Restaurar el stock de los productos de la venta original
            restaurarStockProductos(ventaActual);
            
            // Actualizar los datos de la venta
            ventaActual.setCliente(venta.getCliente());
            ventaActual.setProductos(venta.getProductos());
            
            // Calcular el nuevo total
            calcularTotalVenta(ventaActual);
            
            // Actualizar el stock con los nuevos productos
            actualizarStockProductos(ventaActual);
            
            return ventaRepository.save(ventaActual);
        }
        throw new RuntimeException("Venta no encontrada con ID: " + id);
    }

    public void delete(Long id) {
        Optional<Venta> venta = ventaRepository.findById(id);
        if (venta.isPresent()) {
            // Restaurar el stock de los productos
            restaurarStockProductos(venta.get());
            
            // Marcar como inactiva (soft delete)
            Venta ventaActual = venta.get();
            ventaActual.setActivo(false);
            ventaRepository.save(ventaActual);
        } else {
            throw new RuntimeException("Venta no encontrada con ID: " + id);
        }
    }

    public List<Venta> buscar(String busqueda) {
        if (busqueda == null || busqueda.trim().isEmpty()) {
            return findAll();
        }
        return ventaRepository.buscarVentasPorTexto(busqueda.trim());
    }

    public List<Venta> findByCliente(String cliente) {
        return ventaRepository.findByClienteContainingIgnoreCaseAndActivoTrue(cliente);
    }

    public List<Venta> findByFechaRange(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return ventaRepository.findByFechaVentaBetweenAndActivoTrue(fechaInicio, fechaFin);
    }

    public long countVentas() {
        return ventaRepository.countByActivoTrue();
    }

    public Double getTotalVentas() {
        return ventaRepository.sumTotalVentasActivas();
    }

    private void calcularTotalVenta(Venta venta) {
        double total = 0.0;
        for (VentaProducto item : venta.getProductos()) {
            total += item.getSubtotal().doubleValue();
        }
        venta.setTotal(java.math.BigDecimal.valueOf(total));
    }

    private void actualizarStockProductos(Venta venta) {
        for (VentaProducto item : venta.getProductos()) {
            Producto producto = item.getProducto();
            int stockActual = producto.getStock();
            int cantidadVendida = item.getCantidad();
            
            if (stockActual < cantidadVendida) {
                throw new RuntimeException("Stock insuficiente para el producto: " + producto.getNombre() + 
                    ". Stock disponible: " + stockActual + ", Cantidad solicitada: " + cantidadVendida);
            }
            
            producto.setStock(stockActual - cantidadVendida);
            productoRepository.save(producto);
        }
    }

    private void restaurarStockProductos(Venta venta) {
        for (VentaProducto item : venta.getProductos()) {
            Producto producto = item.getProducto();
            int stockActual = producto.getStock();
            int cantidadRestaurar = item.getCantidad();
            
            producto.setStock(stockActual + cantidadRestaurar);
            productoRepository.save(producto);
        }
    }
}
