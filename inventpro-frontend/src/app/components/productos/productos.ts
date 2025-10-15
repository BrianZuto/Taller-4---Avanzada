import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ProductoService } from '../../services/producto.service';
import { CategoriaService } from '../../services/categoria.service';
import { Producto, ProductoRequest, Categoria } from '../../models/producto.models';
import { Layout } from '../layout/layout';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-productos',
  imports: [CommonModule, FormsModule, Layout],
  templateUrl: './productos.html',
  styleUrl: './productos.css'
})
export class Productos implements OnInit {

  productos: Producto[] = [];
  categorias: Categoria[] = [];
  busqueda: string = '';
  mostrarModal: boolean = false;
  productoEditando: Producto | null = null;
  productoFormData: ProductoRequest = {
    nombre: '',
    presentacion: '',
    precio: 0,
    stock: 0,
    stockMinimo: 0,
    impuesto: 0,
    categoriaId: 0
  };

  constructor(
    private productoService: ProductoService,
    private categoriaService: CategoriaService
  ) {}

  ngOnInit(): void {
    this.cargarProductos();
    this.cargarCategorias();
  }

  cargarProductos(): void {
    console.log('🔄 Cargando productos...');
    this.productoService.getAllProductos().subscribe({
      next: (productos) => {
        console.log('✅ Productos cargados:', productos);
        this.productos = productos;
      },
      error: (error) => {
        console.error('❌ Error al cargar productos:', error);
      }
    });
  }

  cargarCategorias(): void {
    this.categoriaService.getAllCategorias().subscribe({
      next: (categorias) => {
        this.categorias = categorias;
      },
      error: (error) => {
        console.error('Error al cargar categorías:', error);
      }
    });
  }

  buscarProductos(): void {
    if (this.busqueda.trim()) {
      this.productoService.buscarProductos(this.busqueda).subscribe({
        next: (productos) => {
          this.productos = productos;
        }
      });
    } else {
      this.cargarProductos();
    }
  }

  abrirModalNuevoProducto(): void {
    this.productoEditando = null;
    this.productoFormData = {
      nombre: '',
      presentacion: '',
      precio: 0,
      stock: 0,
      stockMinimo: 0,
      impuesto: 0,
      categoriaId: 0
    };
    this.mostrarModal = true;
  }

  editarProducto(producto: Producto): void {
    this.productoEditando = producto;
    this.productoFormData = {
      nombre: producto.nombre,
      presentacion: producto.presentacion,
      precio: producto.precio,
      stock: producto.stock,
      stockMinimo: producto.stockMinimo,
      impuesto: producto.impuesto,
      categoriaId: producto.categoriaId
    };
    this.mostrarModal = true;
  }

  guardarProducto(): void {
    const esEdicion = this.productoEditando;
    const accion = esEdicion ? 'actualizando' : 'creando';
    const titulo = esEdicion ? 'Actualizando producto...' : 'Creando producto...';
    const mensajeExito = esEdicion ? 'Producto actualizado exitosamente' : 'Producto creado exitosamente';

    // Mostrar loading
    Swal.fire({
      title: titulo,
      text: 'Por favor espera',
      allowOutsideClick: false,
      didOpen: () => {
        Swal.showLoading();
      }
    });

    if (esEdicion) {
      this.productoService.updateProducto(this.productoEditando!.id, this.productoFormData).subscribe({
        next: () => {
          this.cerrarModal();
          this.cargarProductos();

          // Mostrar mensaje de éxito
          Swal.fire({
            title: '¡Actualizado!',
            text: mensajeExito,
            icon: 'success',
            confirmButtonColor: '#28a745',
            confirmButtonText: 'Aceptar'
          });
        },
        error: (error) => {
          console.error('Error al actualizar producto:', error);

          // Mostrar mensaje de error
          Swal.fire({
            title: 'Error',
            text: 'No se pudo actualizar el producto. Por favor, inténtalo de nuevo.',
            icon: 'error',
            confirmButtonColor: '#dc3545',
            confirmButtonText: 'Aceptar'
          });
        }
      });
    } else {
      this.productoService.createProducto(this.productoFormData).subscribe({
        next: () => {
          this.cerrarModal();
          this.cargarProductos();

          // Mostrar mensaje de éxito
          Swal.fire({
            title: '¡Creado!',
            text: mensajeExito,
            icon: 'success',
            confirmButtonColor: '#28a745',
            confirmButtonText: 'Aceptar'
          });
        },
        error: (error) => {
          console.error('Error al crear producto:', error);

          // Mostrar mensaje de error
          Swal.fire({
            title: 'Error',
            text: 'No se pudo crear el producto. Por favor, inténtalo de nuevo.',
            icon: 'error',
            confirmButtonColor: '#dc3545',
            confirmButtonText: 'Aceptar'
          });
        }
      });
    }
  }

  eliminarProducto(id: number): void {
    // Encontrar el producto para mostrar su nombre en la confirmación
    const producto = this.productos.find(p => p.id === id);
    const nombreProducto = producto ? producto.nombre : 'este producto';

    Swal.fire({
      title: '¿Estás seguro?',
      text: `¿Quieres eliminar "${nombreProducto}"?`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#d33',
      cancelButtonColor: '#3085d6',
      confirmButtonText: 'Sí, eliminar',
      cancelButtonText: 'Cancelar',
      reverseButtons: true
    }).then((result) => {
      if (result.isConfirmed) {
        console.log('🗑️ Eliminando producto con ID:', id);

        // Mostrar loading
        Swal.fire({
          title: 'Eliminando...',
          text: 'Por favor espera',
          allowOutsideClick: false,
          didOpen: () => {
            Swal.showLoading();
          }
        });

        this.productoService.deleteProducto(id).subscribe({
          next: () => {
            console.log('✅ Producto eliminado exitosamente');
            this.cargarProductos();

            // Mostrar mensaje de éxito
            Swal.fire({
              title: '¡Eliminado!',
              text: 'El producto ha sido eliminado exitosamente',
              icon: 'success',
              confirmButtonColor: '#28a745',
              confirmButtonText: 'Aceptar'
            });
          },
          error: (error) => {
            console.error('❌ Error al eliminar producto:', error);

            // Mostrar mensaje de error
            Swal.fire({
              title: 'Error',
              text: 'No se pudo eliminar el producto. Por favor, inténtalo de nuevo.',
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
    this.productoEditando = null;
    this.productoFormData = {
      nombre: '',
      presentacion: '',
      precio: 0,
      stock: 0,
      stockMinimo: 0,
      impuesto: 0,
      categoriaId: 0
    };
  }
}
