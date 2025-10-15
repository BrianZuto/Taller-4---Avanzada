export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  nombreCompleto: string;
  email: string;
  password: string;
}

export interface AuthResponse {
  token: string;
  tipo: string;
  id: number;
  nombreCompleto: string;
  email: string;
}

export interface UserProfile {
  id: number;
  nombreCompleto: string;
  email: string;
  fechaCreacion: string;
}
