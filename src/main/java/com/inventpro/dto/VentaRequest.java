package com.inventpro.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.List;

public class VentaRequest {
    
    @NotBlank(message = "El nombre del cliente es obligatorio")
    @Size(max = 255, message = "El nombre del cliente no puede exceder los 255 caracteres")
    private String cliente;
    
    @NotEmpty(message = "La venta debe tener al menos un producto")
    private List<VentaProductoRequest> productos;

    public VentaRequest() {}

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public List<VentaProductoRequest> getProductos() {
        return productos;
    }

    public void setProductos(List<VentaProductoRequest> productos) {
        this.productos = productos;
    }
}
