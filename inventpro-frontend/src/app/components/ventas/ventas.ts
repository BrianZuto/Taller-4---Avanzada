import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { VentaService } from '../../services/venta.service';
import { ProductoService } from '../../services/producto.service';
import { Venta, VentaRequest, VentaProducto } from '../../models/venta.models';
import { Producto } from '../../models/producto.models';
import { Layout } from '../layout/layout';
import Swal from 'sweetalert2';

interface ProductoSeleccionado {
  producto: Producto;
  cantidad: number;
}

@Component({
  selector: 'app-ventas',
  standalone: true,
  imports: [CommonModule, FormsModule, Layout],
  templateUrl: './ventas.html',
  styleUrl: './ventas.css'
})
export class Ventas implements OnInit {

  ventas: Venta[] = [];
  productos: Producto[] = [];
  busqueda: string = '';
  mostrarModal: boolean = false;
  mostrarSelectorProductos: boolean = false;
  ventaEditando: Venta | null = null;
  productosSeleccionados: ProductoSeleccionado[] = [];
  productosDisponibles: Producto[] = [];
  productosFiltrados: Producto[] = [];
  busquedaProductos: string = '';

  ventaFormData: VentaRequest = {
    cliente: '',
    productos: []
  };

  constructor(
    private ventaService: VentaService,
    private productoService: ProductoService
  ) {}

  ngOnInit(): void {
    this.cargarVentas();
    this.cargarProductos();
  }

  cargarVentas(): void {
    console.log('🔄 Cargando ventas...');
    this.ventaService.getAllVentas().subscribe({
      next: (ventas) => {
        console.log('✅ Ventas cargadas:', ventas);
        this.ventas = ventas;
      },
      error: (error) => {
        console.error('❌ Error al cargar ventas:', error);
        Swal.fire({
          title: 'Error',
          text: 'No se pudieron cargar las ventas',
          icon: 'error',
          confirmButtonColor: '#dc3545',
          confirmButtonText: 'Aceptar'
        });
      }
    });
  }

  cargarProductos(): void {
    this.productoService.getAllProductos().subscribe({
      next: (productos) => {
        this.productos = productos;
        this.productosDisponibles = productos.filter(p => p.stock > 0);
        this.productosFiltrados = this.productosDisponibles;
      },
      error: (error) => {
        console.error('Error al cargar productos:', error);
      }
    });
  }

  buscarVentas(): void {
    if (this.busqueda.trim()) {
      this.ventaService.buscarVentas(this.busqueda).subscribe({
        next: (ventas) => {
          this.ventas = ventas;
        },
        error: (error) => {
          console.error('Error al buscar ventas:', error);
        }
      });
    } else {
      this.cargarVentas();
    }
  }

  abrirModalNuevaVenta(): void {
    this.ventaEditando = null;
    this.ventaFormData = {
      cliente: '',
      productos: []
    };
    this.productosSeleccionados = [];
    this.mostrarModal = true;
  }


  agregarProducto(producto: Producto): void {
    // Verificar si el producto ya está seleccionado
    const yaSeleccionado = this.productosSeleccionados.find(p => p.producto.id === producto.id);
    if (yaSeleccionado) {
      Swal.fire({
        title: 'Producto ya agregado',
        text: 'Este producto ya está en la venta',
        icon: 'warning',
        confirmButtonColor: '#3085d6',
        confirmButtonText: 'Aceptar'
      });
      return;
    }

    // Verificar stock disponible
    if (producto.stock <= 0) {
      Swal.fire({
        title: 'Sin stock',
        text: 'Este producto no tiene stock disponible',
        icon: 'warning',
        confirmButtonColor: '#3085d6',
        confirmButtonText: 'Aceptar'
      });
      return;
    }

    this.productosSeleccionados.push({
      producto: producto,
      cantidad: 1
    });

    this.cerrarSelectorProductos();
  }


  guardarVenta(): void {
    if (this.productosSeleccionados.length === 0) {
      Swal.fire({
        title: 'Sin productos',
        text: 'Debes agregar al menos un producto a la venta',
        icon: 'warning',
        confirmButtonColor: '#3085d6',
        confirmButtonText: 'Aceptar'
      });
      return;
    }

    // Verificar que todas las cantidades sean válidas
    const productosSinCantidad = this.productosSeleccionados.filter(p => p.cantidad <= 0);
    if (productosSinCantidad.length > 0) {
      Swal.fire({
        title: 'Cantidades inválidas',
        text: 'Todos los productos deben tener una cantidad mayor a 0',
        icon: 'warning',
        confirmButtonColor: '#3085d6',
        confirmButtonText: 'Aceptar'
      });
      return;
    }

    // Preparar los datos de la venta
    this.ventaFormData.productos = this.productosSeleccionados.map(item => ({
      productoId: item.producto.id,
      cantidad: item.cantidad
    }));

    const esEdicion = this.ventaEditando;
    const titulo = esEdicion ? 'Actualizando venta...' : 'Creando venta...';
    const mensajeExito = esEdicion ? 'Venta actualizada exitosamente' : 'Venta creada exitosamente';

    // Mostrar loading
    Swal.fire({
      title: titulo,
      text: 'Procesando la venta',
      allowOutsideClick: false,
      didOpen: () => {
        Swal.showLoading();
      }
    });

    if (esEdicion) {
      this.ventaService.updateVenta(this.ventaEditando!.id, this.ventaFormData).subscribe({
        next: () => {
          this.cerrarModal();
          this.cargarVentas();
          this.cargarProductos(); // Recargar productos para actualizar stock

          Swal.fire({
            title: '¡Actualizada!',
            text: mensajeExito,
            icon: 'success',
            confirmButtonColor: '#28a745',
            confirmButtonText: 'Aceptar'
          });
        },
        error: (error) => {
          console.error('Error al actualizar venta:', error);

          Swal.fire({
            title: 'Error',
            text: 'No se pudo actualizar la venta. Por favor, inténtalo de nuevo.',
            icon: 'error',
            confirmButtonColor: '#dc3545',
            confirmButtonText: 'Aceptar'
          });
        }
      });
    } else {
      this.ventaService.createVenta(this.ventaFormData).subscribe({
        next: () => {
          this.cerrarModal();
          this.cargarVentas();
          this.cargarProductos(); // Recargar productos para actualizar stock

          Swal.fire({
            title: '¡Venta creada!',
            text: mensajeExito,
            icon: 'success',
            confirmButtonColor: '#28a745',
            confirmButtonText: 'Aceptar'
          });
        },
        error: (error) => {
          console.error('Error al crear venta:', error);

          Swal.fire({
            title: 'Error',
            text: 'No se pudo crear la venta. Por favor, inténtalo de nuevo.',
            icon: 'error',
            confirmButtonColor: '#dc3545',
            confirmButtonText: 'Aceptar'
          });
        }
      });
    }
  }

  verVenta(venta: Venta): void {
    const productosHtml = venta.productos.map(p =>
      `<p>• ${p.productoNombre} (${p.cantidad} unidades) - $${p.subtotal.toFixed(2)}</p>`
    ).join('');

    Swal.fire({
      title: `Venta #${venta.id}`,
      html: `
        <div style="text-align: left;">
          <p><strong>Cliente:</strong> ${venta.cliente}</p>
          <p><strong>Fecha:</strong> ${new Date(venta.fechaVenta).toLocaleString()}</p>
          <p><strong>Productos:</strong></p>
          ${productosHtml}
          <hr>
          <p><strong>Total:</strong> $${venta.total.toFixed(2)}</p>
        </div>
      `,
      icon: 'info',
      confirmButtonColor: '#007bff',
      confirmButtonText: 'Cerrar'
    });
  }

  eliminarVenta(id: number): void {
    Swal.fire({
      title: '¿Estás seguro?',
      text: '¿Quieres eliminar esta venta?',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#d33',
      cancelButtonColor: '#6c757d',
      confirmButtonText: 'Sí, eliminar',
      cancelButtonText: 'Cancelar',
      reverseButtons: true
    }).then((result) => {
      if (result.isConfirmed) {
        console.log('🗑️ Eliminando venta con ID:', id);

        // Mostrar loading
        Swal.fire({
          title: 'Eliminando...',
          text: 'Por favor espera',
          allowOutsideClick: false,
          didOpen: () => {
            Swal.showLoading();
          }
        });

        this.ventaService.deleteVenta(id).subscribe({
          next: () => {
            console.log('✅ Venta eliminada exitosamente');
            this.cargarVentas();
            this.cargarProductos(); // Recargar productos para actualizar stock

            Swal.fire({
              title: '¡Eliminada!',
              text: 'La venta ha sido eliminada exitosamente',
              icon: 'success',
              confirmButtonColor: '#28a745',
              confirmButtonText: 'Aceptar'
            });
          },
          error: (error) => {
            console.error('❌ Error al eliminar venta:', error);

            Swal.fire({
              title: 'Error',
              text: 'No se pudo eliminar la venta. Por favor, inténtalo de nuevo.',
              icon: 'error',
              confirmButtonColor: '#dc3545',
              confirmButtonText: 'Aceptar'
            });
          }
        });
      }
    });
  }

  cerrarModal(event?: Event): void {
    if (event) {
      event.stopPropagation();
    }
    this.mostrarModal = false;
    this.ventaEditando = null;
    this.productosSeleccionados = [];
    this.ventaFormData = {
      cliente: '',
      productos: []
    };
  }

  // Nuevos métodos para manejo de productos
  abrirSelectorProductos(): void {
    this.mostrarSelectorProductos = true;
    this.busquedaProductos = '';
    this.filtrarProductos();
  }

  cerrarSelectorProductos(event?: Event): void {
    if (event) {
      event.stopPropagation();
    }
    this.mostrarSelectorProductos = false;
    this.busquedaProductos = '';
  }

  filtrarProductos(): void {
    if (this.busquedaProductos.trim()) {
      this.productosFiltrados = this.productosDisponibles.filter(p =>
        p.nombre.toLowerCase().includes(this.busquedaProductos.toLowerCase()) ||
        p.categoriaNombre.toLowerCase().includes(this.busquedaProductos.toLowerCase())
      );
    } else {
      this.productosFiltrados = this.productosDisponibles;
    }
  }

  seleccionarProducto(producto: Producto): void {
    // Verificar si el producto ya está seleccionado
    const yaSeleccionado = this.productosSeleccionados.find(p => p.producto.id === producto.id);
    if (yaSeleccionado) {
      Swal.fire('Producto ya seleccionado', 'Este producto ya está en la lista de productos de la venta.', 'warning');
      return;
    }

    // Agregar el producto con cantidad 1
    this.productosSeleccionados.push({
      producto: producto,
      cantidad: 1
    });

    this.cerrarSelectorProductos();
    Swal.fire('Producto agregado', `${producto.nombre} ha sido agregado a la venta.`, 'success');
  }

  incrementarCantidad(index: number): void {
    const item = this.productosSeleccionados[index];
    if (item.cantidad < item.producto.stock) {
      item.cantidad++;
    } else {
      Swal.fire('Stock insuficiente', `No hay más stock disponible para "${item.producto.nombre}".`, 'warning');
    }
  }

  decrementarCantidad(index: number): void {
    const item = this.productosSeleccionados[index];
    if (item.cantidad > 0) {
      item.cantidad--;
    }
  }

  removerProducto(index: number): void {
    const producto = this.productosSeleccionados[index].producto;
    Swal.fire({
      title: '¿Eliminar producto?',
      text: `¿Estás seguro de que quieres eliminar "${producto.nombre}" de la venta?`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#d33',
      cancelButtonColor: '#3085d6',
      confirmButtonText: 'Sí, eliminar',
      cancelButtonText: 'Cancelar'
    }).then((result) => {
      if (result.isConfirmed) {
        this.productosSeleccionados.splice(index, 1);
        Swal.fire('Producto eliminado', `${producto.nombre} ha sido eliminado de la venta.`, 'success');
      }
    });
  }

  calcularTotalVenta(): number {
    return this.productosSeleccionados.reduce((total, item) => {
      return total + (item.producto.precio * item.cantidad);
    }, 0);
  }
}
