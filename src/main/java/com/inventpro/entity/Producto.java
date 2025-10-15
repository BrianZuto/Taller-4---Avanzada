package com.inventpro.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "productos")
public class Producto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;
    
    @Column(name = "presentacion", length = 50)
    private String presentacion;
    
    @Column(name = "precio", precision = 10, scale = 2)
    private BigDecimal precio;
    
    @Column(name = "stock")
    private Integer stock = 0;
    
    @Column(name = "stock_minimo")
    private Integer stockMinimo = 0;
    
    @Column(name = "impuesto", precision = 5, scale = 2)
    private BigDecimal impuesto = BigDecimal.ZERO;
    
    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;
    
    @Column(name = "activo")
    private Boolean activo = true;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id")
    @JsonIgnoreProperties("productos")
    private Categoria categoria;
    
    // Constructores
    public Producto() {
        this.fechaCreacion = LocalDateTime.now();
        this.activo = true;
        this.stock = 0;
        this.stockMinimo = 0;
        this.impuesto = BigDecimal.ZERO;
    }
    
    public Producto(String nombre, String presentacion, BigDecimal precio, Integer stock, Categoria categoria) {
        this();
        this.nombre = nombre;
        this.presentacion = presentacion;
        this.precio = precio;
        this.stock = stock;
        this.categoria = categoria;
    }
    
    // Getters y Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getPresentacion() {
        return presentacion;
    }
    
    public void setPresentacion(String presentacion) {
        this.presentacion = presentacion;
    }
    
    public BigDecimal getPrecio() {
        return precio;
    }
    
    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }
    
    public Integer getStock() {
        return stock;
    }
    
    public void setStock(Integer stock) {
        this.stock = stock;
    }
    
    public Integer getStockMinimo() {
        return stockMinimo;
    }
    
    public void setStockMinimo(Integer stockMinimo) {
        this.stockMinimo = stockMinimo;
    }
    
    public BigDecimal getImpuesto() {
        return impuesto;
    }
    
    public void setImpuesto(BigDecimal impuesto) {
        this.impuesto = impuesto;
    }
    
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
    
    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    
    public Boolean getActivo() {
        return activo;
    }
    
    public boolean isActivo() {
        return activo != null ? activo : false;
    }
    
    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
    
    public Categoria getCategoria() {
        return categoria;
    }
    
    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
}
