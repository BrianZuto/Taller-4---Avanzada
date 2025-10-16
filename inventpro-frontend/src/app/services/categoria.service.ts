import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Categoria, CategoriaRequest } from '../models/producto.models';
import { ConfigService } from './config.service';

@Injectable({
  providedIn: 'root'
})
export class CategoriaService {
  private API_URL: string;

  constructor(
    private http: HttpClient,
    private configService: ConfigService
  ) {
    this.API_URL = this.configService.getCategoriasUrl();
  }

  getAllCategorias(): Observable<Categoria[]> {
    return this.http.get<Categoria[]>(this.API_URL);
  }

  getCategoriaById(id: number): Observable<Categoria> {
    return this.http.get<Categoria>(`${this.API_URL}/${id}`);
  }

  createCategoria(categoria: CategoriaRequest): Observable<Categoria> {
    return this.http.post<Categoria>(this.API_URL, categoria);
  }

  updateCategoria(id: number, categoria: CategoriaRequest): Observable<Categoria> {
    return this.http.put<Categoria>(`${this.API_URL}/${id}`, categoria);
  }

  deleteCategoria(id: number): Observable<any> {
    return this.http.delete(`${this.API_URL}/${id}`);
  }

  buscarCategorias(busqueda: string): Observable<Categoria[]> {
    return this.http.get<Categoria[]>(`${this.API_URL}/buscar`, {
      params: { busqueda }
    });
  }
}
