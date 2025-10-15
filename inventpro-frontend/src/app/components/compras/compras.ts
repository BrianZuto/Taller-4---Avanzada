import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ProductoService } from '../../services/producto.service';
import { Producto } from '../../models/producto.models';
import { Layout } from '../layout/layout';
import Swal from 'sweetalert2';

interface CompraItem {
  producto: Producto;
  cantidad: number;
}

@Component({
  selector: 'app-compras',
  standalone: true,
  imports: [CommonModule, FormsModule, Layout],
  templateUrl: './compras.html',
  styleUrl: './compras.css'
})
export class Compras implements OnInit {

  productos: Producto[] = [];
  carritoCompras: CompraItem[] = [];
  busqueda: string = '';

  constructor(
    private productoService: ProductoService
  ) {}

  ngOnInit(): void {
    this.cargarProductos();
  }

  cargarProductos(): void {
    console.log('🔄 Cargando productos para compras...');
    this.productoService.getAllProductos().subscribe({
      next: (productos) => {
        console.log('✅ Productos cargados:', productos);
        this.productos = productos;
      },
      error: (error) => {
        console.error('❌ Error al cargar productos:', error);
        Swal.fire({
          title: 'Error',
          text: 'No se pudieron cargar los productos',
          icon: 'error',
          confirmButtonColor: '#dc3545',
          confirmButtonText: 'Aceptar'
        });
      }
    });
  }

  buscarProductos(): void {
    if (this.busqueda.trim()) {
      this.productoService.buscarProductos(this.busqueda).subscribe({
        next: (productos) => {
          this.productos = productos;
        },
        error: (error) => {
          console.error('Error al buscar productos:', error);
        }
      });
    } else {
      this.cargarProductos();
    }
  }

  abrirModalNuevaCompra(): void {
    // Este método se puede usar para futuras funcionalidades
    console.log('Nueva compra');
  }

  getCantidadCompra(producto: Producto): number {
    const item = this.carritoCompras.find(item => item.producto.id === producto.id);
    return item ? item.cantidad : 0;
  }

  incrementarCantidad(producto: Producto): void {
    const item = this.carritoCompras.find(item => item.producto.id === producto.id);
    if (item) {
      item.cantidad++;
    } else {
      this.carritoCompras.push({ producto, cantidad: 1 });
    }
  }

  decrementarCantidad(producto: Producto): void {
    const item = this.carritoCompras.find(item => item.producto.id === producto.id);
    if (item) {
      item.cantidad--;
      if (item.cantidad <= 0) {
        this.carritoCompras = this.carritoCompras.filter(i => i.producto.id !== producto.id);
      }
    }
  }

  actualizarCantidad(producto: Producto, event: any): void {
    const cantidad = parseInt(event.target.value) || 0;
    if (cantidad <= 0) {
      this.carritoCompras = this.carritoCompras.filter(item => item.producto.id !== producto.id);
    } else {
      const item = this.carritoCompras.find(item => item.producto.id === producto.id);
      if (item) {
        item.cantidad = cantidad;
      } else {
        this.carritoCompras.push({ producto, cantidad });
      }
    }
  }

  calcularTotal(producto: Producto): number {
    const cantidad = this.getCantidadCompra(producto);
    return cantidad * producto.precio;
  }

  tieneProductosEnCarrito(): boolean {
    return this.carritoCompras.length > 0;
  }

  getProductosEnCarrito(): CompraItem[] {
    return this.carritoCompras;
  }

  calcularTotalCompra(): number {
    return this.carritoCompras.reduce((total, item) => {
      return total + (item.cantidad * item.producto.precio);
    }, 0);
  }

  comprarProducto(producto: Producto): void {
    const cantidad = this.getCantidadCompra(producto);
    if (cantidad <= 0) {
      Swal.fire({
        title: 'Cantidad inválida',
        text: 'Debes seleccionar al menos 1 unidad',
        icon: 'warning',
        confirmButtonColor: '#3085d6',
        confirmButtonText: 'Aceptar'
      });
      return;
    }

    const total = cantidad * producto.precio;

    Swal.fire({
      title: 'Confirmar Compra',
      html: `
        <div style="text-align: left;">
          <p><strong>Producto:</strong> ${producto.nombre}</p>
          <p><strong>Cantidad:</strong> ${cantidad} unidades</p>
          <p><strong>Precio unitario:</strong> $${producto.precio.toFixed(2)}</p>
          <p><strong>Total:</strong> $${total.toFixed(2)}</p>
        </div>
      `,
      icon: 'question',
      showCancelButton: true,
      confirmButtonColor: '#28a745',
      cancelButtonColor: '#6c757d',
      confirmButtonText: 'Confirmar Compra',
      cancelButtonText: 'Cancelar'
    }).then((result) => {
      if (result.isConfirmed) {
        this.procesarCompraIndividual(producto, cantidad);
      }
    });
  }

  procesarCompraIndividual(producto: Producto, cantidad: number): void {
    // Mostrar loading
    Swal.fire({
      title: 'Procesando compra...',
      text: 'Actualizando stock del producto',
      allowOutsideClick: false,
      didOpen: () => {
        Swal.showLoading();
      }
    });

    // Llamar al endpoint del backend
    this.productoService.comprarProducto(producto.id, cantidad).subscribe({
      next: (response) => {
        console.log('✅ Compra procesada:', response);

        // Actualizar el stock localmente
        producto.stock = response.stockActual;

        // Remover del carrito
        this.carritoCompras = this.carritoCompras.filter(item => item.producto.id !== producto.id);

        Swal.fire({
          title: '¡Compra exitosa!',
          html: `
            <div style="text-align: left;">
              <p><strong>Producto:</strong> ${response.producto}</p>
              <p><strong>Cantidad comprada:</strong> ${response.cantidadComprada} unidades</p>
              <p><strong>Stock anterior:</strong> ${response.stockAnterior}</p>
              <p><strong>Stock actual:</strong> ${response.stockActual}</p>
            </div>
          `,
          icon: 'success',
          confirmButtonColor: '#28a745',
          confirmButtonText: 'Aceptar'
        });
      },
      error: (error) => {
        console.error('❌ Error al procesar compra:', error);

        Swal.fire({
          title: 'Error en la compra',
          text: error.error?.mensaje || 'No se pudo procesar la compra',
          icon: 'error',
          confirmButtonColor: '#dc3545',
          confirmButtonText: 'Aceptar'
        });
      }
    });
  }

  procesarCompra(): void {
    if (this.carritoCompras.length === 0) {
      Swal.fire({
        title: 'Carrito vacío',
        text: 'No hay productos en el carrito',
        icon: 'warning',
        confirmButtonColor: '#3085d6',
        confirmButtonText: 'Aceptar'
      });
      return;
    }

    const total = this.calcularTotalCompra();
    const productosCount = this.carritoCompras.length;

    Swal.fire({
      title: 'Confirmar Compra Completa',
      html: `
        <div style="text-align: left;">
          <p><strong>Productos:</strong> ${productosCount}</p>
          <p><strong>Total a pagar:</strong> $${total.toFixed(2)}</p>
          <hr>
          <div style="max-height: 200px; overflow-y: auto;">
            ${this.carritoCompras.map(item =>
              `<p>• ${item.producto.nombre} (${item.cantidad} unidades) - $${(item.cantidad * item.producto.precio).toFixed(2)}</p>`
            ).join('')}
          </div>
        </div>
      `,
      icon: 'question',
      showCancelButton: true,
      confirmButtonColor: '#28a745',
      cancelButtonColor: '#6c757d',
      confirmButtonText: 'Procesar Compra',
      cancelButtonText: 'Cancelar'
    }).then((result) => {
      if (result.isConfirmed) {
        this.procesarCompraCompleta();
      }
    });
  }

  procesarCompraCompleta(): void {
    // Mostrar loading
    Swal.fire({
      title: 'Procesando compra...',
      text: 'Actualizando stock de todos los productos',
      allowOutsideClick: false,
      didOpen: () => {
        Swal.showLoading();
      }
    });

    // Simular procesamiento de compra completa
    setTimeout(() => {
      // Actualizar stock de todos los productos
      this.carritoCompras.forEach(item => {
        item.producto.stock += item.cantidad;
      });

      const totalProductos = this.carritoCompras.reduce((sum, item) => sum + item.cantidad, 0);
      const totalPrecio = this.calcularTotalCompra();

      // Limpiar carrito
      this.carritoCompras = [];

      Swal.fire({
        title: '¡Compra completada!',
        html: `
          <div style="text-align: left;">
            <p><strong>Productos actualizados:</strong> ${totalProductos} unidades</p>
            <p><strong>Total pagado:</strong> $${totalPrecio.toFixed(2)}</p>
            <p>El stock ha sido actualizado exitosamente.</p>
          </div>
        `,
        icon: 'success',
        confirmButtonColor: '#28a745',
        confirmButtonText: 'Aceptar'
      });
    }, 2000);
  }

  limpiarCarrito(): void {
    Swal.fire({
      title: '¿Limpiar carrito?',
      text: 'Se eliminarán todos los productos del carrito',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#d33',
      cancelButtonColor: '#6c757d',
      confirmButtonText: 'Sí, limpiar',
      cancelButtonText: 'Cancelar'
    }).then((result) => {
      if (result.isConfirmed) {
        this.carritoCompras = [];
        Swal.fire({
          title: 'Carrito limpiado',
          text: 'Se han eliminado todos los productos del carrito',
          icon: 'success',
          confirmButtonColor: '#28a745',
          confirmButtonText: 'Aceptar'
        });
      }
    });
  }
}
