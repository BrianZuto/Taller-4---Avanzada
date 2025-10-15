export interface Venta {
  id: number;
  fechaVenta: string;
  total: number;
  cliente: string;
  productos: VentaProducto[];
  activo: boolean;
}

export interface VentaProducto {
  id: number;
  productoId: number;
  productoNombre: string;
  cantidad: number;
  precioUnitario: number;
  subtotal: number;
}

export interface VentaRequest {
  cliente: string;
  productos: VentaProductoRequest[];
}

export interface VentaProductoRequest {
  productoId: number;
  cantidad: number;
}
