import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ConfigService {
  private readonly API_URL = environment.apiUrl;

  getApiUrl(): string {
    return this.API_URL;
  }

  getAuthUrl(): string {
    return `${this.API_URL}/auth`;
  }

  getProductosUrl(): string {
    return `${this.API_URL}/productos`;
  }

  getCategoriasUrl(): string {
    return `${this.API_URL}/categorias`;
  }

  getVentasUrl(): string {
    return `${this.API_URL}/ventas`;
  }

  getEstadisticasUrl(): string {
    return `${this.API_URL}/estadisticas`;
  }
}
