import { Component, OnInit } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';
import { AuthService } from '../../services/auth';
import { UserProfile } from '../../models/auth.models';
import { CommonModule } from '@angular/common';
import { filter } from 'rxjs/operators';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-sidebar',
  imports: [CommonModule],
  templateUrl: './sidebar.html',
  styleUrl: './sidebar.css'
})
export class Sidebar implements OnInit {

  currentRoute: string = '';
  currentUser: UserProfile | null = null;

  constructor(
    private authService: AuthService,
    private router: Router
  ) {
    // Escuchar cambios de ruta para actualizar el indicador activo
    this.router.events
      .pipe(filter(event => event instanceof NavigationEnd))
      .subscribe((event: NavigationEnd) => {
        this.currentRoute = event.url;
      });
  }

  ngOnInit(): void {
    // Obtener información del usuario actual
    this.authService.currentUser$.subscribe(user => {
      this.currentUser = user;
    });
  }

  logout(): void {
    Swal.fire({
      title: '¿Cerrar sesión?',
      text: '¿Estás seguro de que quieres cerrar sesión?',
      icon: 'question',
      showCancelButton: true,
      confirmButtonColor: '#d33',
      cancelButtonColor: '#6c757d',
      confirmButtonText: 'Sí, cerrar sesión',
      cancelButtonText: 'Cancelar'
    }).then((result) => {
      if (result.isConfirmed) {
        this.authService.logout();
        this.router.navigate(['/auth']);

        Swal.fire({
          title: 'Sesión cerrada',
          text: 'Has cerrado sesión exitosamente',
          icon: 'success',
          confirmButtonColor: '#28a745',
          confirmButtonText: 'Aceptar'
        });
      }
    });
  }

  navigateTo(path: string): void {
    console.log('🔄 Navegando a:', path);
    this.router.navigate([path]).then(success => {
      if (success) {
        console.log('✅ Navegación exitosa a:', path);
      } else {
        console.error('❌ Error al navegar a:', path);
      }
    }).catch(error => {
      console.error('❌ Error de navegación:', error);
    });
  }

  isActive(path: string): boolean {
    return this.currentRoute === path;
  }
}
