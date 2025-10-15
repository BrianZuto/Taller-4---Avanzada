import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { tap } from 'rxjs/operators';
import { LoginRequest, RegisterRequest, AuthResponse, UserProfile } from '../models/auth.models';
import { TokenStorageService } from './token-storage';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly API_URL = 'http://localhost:8080/api/auth';
  private currentUserSubject = new BehaviorSubject<UserProfile | null>(null);
  public currentUser$ = this.currentUserSubject.asObservable();

  constructor(
    private http: HttpClient,
    private tokenStorage: TokenStorageService
  ) {
    this.loadCurrentUser();
  }

  login(loginRequest: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.API_URL}/login`, loginRequest)
      .pipe(
        tap(response => {
          this.tokenStorage.saveToken(response.token);
          // Crear el perfil del usuario desde la respuesta del login
          const userProfile: UserProfile = {
            id: response.id,
            nombreCompleto: response.nombreCompleto,
            email: response.email,
            fechaCreacion: new Date().toISOString() // Usar fecha actual como fallback
          };
          this.currentUserSubject.next(userProfile);
        })
      );
  }

  register(registerRequest: RegisterRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.API_URL}/register`, registerRequest)
      .pipe(
        tap(response => {
          this.tokenStorage.saveToken(response.token);
          // Crear el perfil del usuario desde la respuesta del registro
          const userProfile: UserProfile = {
            id: response.id,
            nombreCompleto: response.nombreCompleto,
            email: response.email,
            fechaCreacion: new Date().toISOString() // Usar fecha actual como fallback
          };
          this.currentUserSubject.next(userProfile);
        })
      );
  }

  logout(): void {
    this.tokenStorage.signOut();
    this.currentUserSubject.next(null);
  }

  isLoggedIn(): boolean {
    const token = this.tokenStorage.getToken();
    if (!token) {
      return false;
    }

    // Verificar que el token no esté expirado (básico)
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      const currentTime = Date.now() / 1000;
      return payload.exp > currentTime;
    } catch (error) {
      console.warn('Error al verificar token:', error);
      return false;
    }
  }

  getCurrentUser(): UserProfile | null {
    return this.currentUserSubject.value;
  }

  private loadCurrentUser(): void {
    if (this.isLoggedIn()) {
      // Intentar cargar el perfil, pero si falla, no hacer logout automático
      this.loadUserProfile();
    }
  }

  private loadUserProfile(): void {
    this.http.get<UserProfile>(`${this.API_URL}/profile`).subscribe({
      next: (profile) => {
        this.currentUserSubject.next(profile);
      },
      error: (error) => {
        console.warn('No se pudo cargar el perfil del usuario:', error);
        // No hacer logout automático, solo limpiar el usuario actual
        this.currentUserSubject.next(null);
      }
    });
  }
}
