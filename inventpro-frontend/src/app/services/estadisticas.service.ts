import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ConfigService } from './config.service';

@Injectable({
  providedIn: 'root'
})
export class EstadisticasService {
  private API_URL: string;

  constructor(
    private http: HttpClient,
    private configService: ConfigService
  ) {
    // Usar ruta relativa para producción
    this.API_URL = '/api/estadisticas';
  }

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
