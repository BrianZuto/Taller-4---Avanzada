import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth';
import { LoginRequest, RegisterRequest } from '../../models/auth.models';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-auth',
  imports: [CommonModule, FormsModule],
  templateUrl: './auth.html',
  styleUrl: './auth.css'
})
export class AuthComponent implements OnInit {

  isLoginMode = true;
  isLoading = false;
  errorMessage = '';

  loginRequest: LoginRequest = {
    email: '',
    password: ''
  };

  registerRequest: RegisterRequest = {
    nombreCompleto: '',
    email: '',
    password: ''
  };

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    // Si ya está logueado, redirigir al dashboard
    if (this.authService.isLoggedIn()) {
      this.router.navigate(['/dashboard']);
    }
  }

  setLoginMode(isLogin: boolean): void {
    this.isLoginMode = isLogin;
    this.errorMessage = '';
    this.clearForms();
  }

  onLogin(): void {
    if (!this.loginRequest.email || !this.loginRequest.password) {
      Swal.fire({
        title: 'Campos requeridos',
        text: 'Por favor, completa todos los campos',
        icon: 'warning',
        confirmButtonColor: '#3085d6',
        confirmButtonText: 'Aceptar'
      });
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';

    // Mostrar loading
    Swal.fire({
      title: 'Iniciando sesión...',
      text: 'Por favor espera',
      allowOutsideClick: false,
      didOpen: () => {
        Swal.showLoading();
      }
    });

    this.authService.login(this.loginRequest).subscribe({
      next: (response) => {
        this.isLoading = false;
        console.log('Login exitoso:', response);

        // Mostrar mensaje de éxito
        Swal.fire({
          title: '¡Bienvenido!',
          text: 'Has iniciado sesión exitosamente',
          icon: 'success',
          confirmButtonColor: '#28a745',
          confirmButtonText: 'Continuar'
        }).then(() => {
          this.router.navigate(['/dashboard']);
        });
      },
      error: (error) => {
        this.isLoading = false;
        console.error('Error en login:', error);

        // Mostrar mensaje de error
        Swal.fire({
          title: 'Error de autenticación',
          text: error.error?.mensaje || 'Error al iniciar sesión',
          icon: 'error',
          confirmButtonColor: '#dc3545',
          confirmButtonText: 'Aceptar'
        });
      }
    });
  }

  onRegister(): void {
    if (!this.registerRequest.nombreCompleto || !this.registerRequest.email || !this.registerRequest.password) {
      Swal.fire({
        title: 'Campos requeridos',
        text: 'Por favor, completa todos los campos',
        icon: 'warning',
        confirmButtonColor: '#3085d6',
        confirmButtonText: 'Aceptar'
      });
      return;
    }

    if (this.registerRequest.password.length < 6) {
      Swal.fire({
        title: 'Contraseña muy corta',
        text: 'La contraseña debe tener al menos 6 caracteres',
        icon: 'warning',
        confirmButtonColor: '#3085d6',
        confirmButtonText: 'Aceptar'
      });
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';

    // Mostrar loading
    Swal.fire({
      title: 'Creando cuenta...',
      text: 'Por favor espera',
      allowOutsideClick: false,
      didOpen: () => {
        Swal.showLoading();
      }
    });

    this.authService.register(this.registerRequest).subscribe({
      next: (response) => {
        this.isLoading = false;
        console.log('Registro exitoso:', response);

        // Mostrar mensaje de éxito
        Swal.fire({
          title: '¡Cuenta creada!',
          text: 'Tu cuenta ha sido creada exitosamente',
          icon: 'success',
          confirmButtonColor: '#28a745',
          confirmButtonText: 'Continuar'
        }).then(() => {
          this.router.navigate(['/dashboard']);
        });
      },
      error: (error) => {
        this.isLoading = false;
        console.error('Error en registro:', error);

        // Mostrar mensaje de error
        Swal.fire({
          title: 'Error de registro',
          text: error.error?.mensaje || 'Error al crear la cuenta',
          icon: 'error',
          confirmButtonColor: '#dc3545',
          confirmButtonText: 'Aceptar'
        });
      }
    });
  }

  private clearForms(): void {
    this.loginRequest = { email: '', password: '' };
    this.registerRequest = { nombreCompleto: '', email: '', password: '' };
  }
}
