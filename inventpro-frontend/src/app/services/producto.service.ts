import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Producto, ProductoRequest } from '../models/producto.models';

@Injectable({
  providedIn: 'root'
})
export class ProductoService {
  private API_URL = 'http://localhost:8080/api/productos';

  constructor(private http: HttpClient) {}

  getAllProductos(): Observable<Producto[]> {
    return this.http.get<Producto[]>(this.API_URL);
  }

  getProductoById(id: number): Observable<Producto> {
    return this.http.get<Producto>(`${this.API_URL}/${id}`);
  }

  createProducto(producto: ProductoRequest): Observable<Producto> {
    return this.http.post<Producto>(this.API_URL, producto);
  }

  updateProducto(id: number, producto: ProductoRequest): Observable<Producto> {
    return this.http.put<Producto>(`${this.API_URL}/${id}`, producto);
  }

  deleteProducto(id: number): Observable<any> {
    return this.http.delete(`${this.API_URL}/${id}`);
  }

  buscarProductos(busqueda: string): Observable<Producto[]> {
    return this.http.get<Producto[]>(`${this.API_URL}/buscar`, {
      params: { busqueda }
    });
  }

  getProductosByCategoria(categoriaId: number): Observable<Producto[]> {
    return this.http.get<Producto[]>(`${this.API_URL}/categoria/${categoriaId}`);
  }

  getProductosStockBajo(): Observable<Producto[]> {
    return this.http.get<Producto[]>(`${this.API_URL}/stock-bajo`);
  }

  comprarProducto(productoId: number, cantidad: number): Observable<any> {
    return this.http.post(`${this.API_URL}/comprar`, {
      productoId: productoId,
      cantidad: cantidad
    });
  }
}
