import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Venta, VentaRequest } from '../models/venta.models';

@Injectable({
  providedIn: 'root'
})
export class VentaService {
  private API_URL = 'http://localhost:8080/api/ventas';

  constructor(private http: HttpClient) {}

  getAllVentas(): Observable<Venta[]> {
    return this.http.get<Venta[]>(this.API_URL);
  }

  getVentaById(id: number): Observable<Venta> {
    return this.http.get<Venta>(`${this.API_URL}/${id}`);
  }

  createVenta(venta: VentaRequest): Observable<Venta> {
    return this.http.post<Venta>(this.API_URL, venta);
  }

  updateVenta(id: number, venta: VentaRequest): Observable<Venta> {
    return this.http.put<Venta>(`${this.API_URL}/${id}`, venta);
  }

  deleteVenta(id: number): Observable<any> {
    return this.http.delete(`${this.API_URL}/${id}`);
  }

  buscarVentas(busqueda: string): Observable<Venta[]> {
    return this.http.get<Venta[]>(`${this.API_URL}/buscar`, {
      params: { busqueda }
    });
  }
}
