import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CategoriaService } from '../../services/categoria.service';
import { ProductoService } from '../../services/producto.service';
import { Categoria, CategoriaRequest } from '../../models/producto.models';
import { Layout } from '../layout/layout';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-categorias',
  imports: [CommonModule, FormsModule, Layout],
  templateUrl: './categorias.html',
  styleUrl: './categorias.css'
})
export class Categorias implements OnInit {

  categorias: Categoria[] = [];
  busqueda: string = '';
  mostrarModal: boolean = false;
  categoriaEditando: Categoria | null = null;
  categoriaFormData: CategoriaRequest = {
    nombre: '',
    descripcion: ''
  };

  constructor(
    private categoriaService: CategoriaService,
    private productoService: ProductoService
  ) {}

  ngOnInit(): void {
    this.cargarCategorias();
  }

  cargarCategorias(): void {
    console.log('🔄 Cargando categorías...');
    this.categoriaService.getAllCategorias().subscribe({
      next: (categorias) => {
        console.log('✅ Categorías cargadas:', categorias);
        this.categorias = categorias;
        this.cargarConteoProductos();
      },
      error: (error) => {
        console.error('❌ Error al cargar categorías:', error);
      }
    });
  }

  cargarConteoProductos(): void {
    this.categorias.forEach(categoria => {
      this.productoService.getProductosByCategoria(categoria.id).subscribe({
        next: (productos) => {
          categoria.productosCount = productos.length;
        }
      });
    });
  }

  buscarCategorias(): void {
    if (this.busqueda.trim()) {
      this.categoriaService.buscarCategorias(this.busqueda).subscribe({
        next: (categorias) => {
          this.categorias = categorias;
          this.cargarConteoProductos();
        }
      });
    } else {
      this.cargarCategorias();
    }
  }

  abrirModalNuevaCategoria(): void {
    this.categoriaEditando = null;
    this.categoriaFormData = { nombre: '', descripcion: '' };
    this.mostrarModal = true;
  }

  editarCategoria(categoria: Categoria): void {
    this.categoriaEditando = categoria;
    this.categoriaFormData = {
      nombre: categoria.nombre,
      descripcion: categoria.descripcion || ''
    };
    this.mostrarModal = true;
  }

  guardarCategoria(): void {
    const esEdicion = this.categoriaEditando;
    const titulo = esEdicion ? 'Actualizando categoría...' : 'Creando categoría...';
    const mensajeExito = esEdicion ? 'Categoría actualizada exitosamente' : 'Categoría creada exitosamente';

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
      this.categoriaService.updateCategoria(this.categoriaEditando!.id, this.categoriaFormData).subscribe({
        next: () => {
          this.cerrarModal();
          this.cargarCategorias();

          // Mostrar mensaje de éxito
          Swal.fire({
            title: '¡Actualizada!',
            text: mensajeExito,
            icon: 'success',
            confirmButtonColor: '#28a745',
            confirmButtonText: 'Aceptar'
          });
        },
        error: (error) => {
          console.error('Error al actualizar categoría:', error);

          // Mostrar mensaje de error
          Swal.fire({
            title: 'Error',
            text: 'No se pudo actualizar la categoría. Por favor, inténtalo de nuevo.',
            icon: 'error',
            confirmButtonColor: '#dc3545',
            confirmButtonText: 'Aceptar'
          });
        }
      });
    } else {
      this.categoriaService.createCategoria(this.categoriaFormData).subscribe({
        next: () => {
          this.cerrarModal();
          this.cargarCategorias();

          // Mostrar mensaje de éxito
          Swal.fire({
            title: '¡Creada!',
            text: mensajeExito,
            icon: 'success',
            confirmButtonColor: '#28a745',
            confirmButtonText: 'Aceptar'
          });
        },
        error: (error) => {
          console.error('Error al crear categoría:', error);

          // Mostrar mensaje de error
          Swal.fire({
            title: 'Error',
            text: 'No se pudo crear la categoría. Por favor, inténtalo de nuevo.',
            icon: 'error',
            confirmButtonColor: '#dc3545',
            confirmButtonText: 'Aceptar'
          });
        }
      });
    }
  }

  eliminarCategoria(id: number): void {
    // Encontrar la categoría para mostrar su nombre en la confirmación
    const categoria = this.categorias.find(c => c.id === id);
    const nombreCategoria = categoria ? categoria.nombre : 'esta categoría';

    Swal.fire({
      title: '¿Estás seguro?',
      text: `¿Quieres eliminar "${nombreCategoria}"?`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#d33',
      cancelButtonColor: '#3085d6',
      confirmButtonText: 'Sí, eliminar',
      cancelButtonText: 'Cancelar',
      reverseButtons: true
    }).then((result) => {
      if (result.isConfirmed) {
        console.log('🗑️ Eliminando categoría con ID:', id);

        // Mostrar loading
        Swal.fire({
          title: 'Eliminando...',
          text: 'Por favor espera',
          allowOutsideClick: false,
          didOpen: () => {
            Swal.showLoading();
          }
        });

        this.categoriaService.deleteCategoria(id).subscribe({
          next: () => {
            console.log('✅ Categoría eliminada exitosamente');
            this.cargarCategorias();

            // Mostrar mensaje de éxito
            Swal.fire({
              title: '¡Eliminada!',
              text: 'La categoría ha sido eliminada exitosamente',
              icon: 'success',
              confirmButtonColor: '#28a745',
              confirmButtonText: 'Aceptar'
            });
          },
          error: (error) => {
            console.error('❌ Error al eliminar categoría:', error);

            // Mostrar mensaje de error
            Swal.fire({
              title: 'Error',
              text: 'No se pudo eliminar la categoría. Por favor, inténtalo de nuevo.',
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
    this.categoriaEditando = null;
    this.categoriaFormData = { nombre: '', descripcion: '' };
  }
}
