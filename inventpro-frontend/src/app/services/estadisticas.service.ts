import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class EstadisticasService {
  private API_URL = 'http://localhost:8080/api/estadisticas';

  constructor(private http: HttpClient) { }

  getDashboardData(): Observable<any> {
    return this.http.get<any>(`${this.API_URL}/dashboard`);
  }

  getSalesReports(): Observable<any> {
    return this.http.get<any>(`${this.API_URL}/ventas`);
  }

  getProductReports(): Observable<any> {
    return this.http.get<any>(`${this.API_URL}/productos`);
  }

  getInventoryReports(): Observable<any> {
    return this.http.get<any>(`${this.API_URL}/inventario`);
  }
}
