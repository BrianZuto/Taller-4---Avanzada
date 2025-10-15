package com.inventpro.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class VentaProductoRequest {
    
    @NotNull(message = "El ID del producto es obligatorio")
    private Long productoId;
    
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    private Integer cantidad;

    public VentaProductoRequest() {}

    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
}
