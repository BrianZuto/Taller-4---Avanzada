export interface Categoria {
  id: number;
  nombre: string;
  descripcion: string;
  fechaCreacion: string;
  activo: boolean;
  productosCount?: number;
}

export interface Producto {
  id: number;
  nombre: string;
  presentacion: string;
  precio: number;
  stock: number;
  stockMinimo: number;
  impuesto: number;
  fechaCreacion: string;
  activo: boolean;
  categoriaId: number;
  categoriaNombre: string;
}

export interface CategoriaRequest {
  nombre: string;
  descripcion: string;
}

export interface ProductoRequest {
  nombre: string;
  presentacion: string;
  precio: number;
  stock: number;
  stockMinimo: number;
  impuesto: number;
  categoriaId: number;
}
