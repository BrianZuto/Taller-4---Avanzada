import { Routes } from '@angular/router';
import { AuthComponent } from './components/auth/auth';
import { DashboardComponent } from './components/dashboard/dashboard';
import { Categorias } from './components/categorias/categorias';
import { Productos } from './components/productos/productos';
import { Compras } from './components/compras/compras';
import { Ventas } from './components/ventas/ventas';
import { AuthGuard } from './guards/auth.guard';

export const routes: Routes = [
  { path: '', redirectTo: '/dashboard', pathMatch: 'full' },
  { path: 'auth', component: AuthComponent },
  { path: 'dashboard', component: DashboardComponent, canActivate: [AuthGuard] },
  { path: 'categorias', component: Categorias, canActivate: [AuthGuard] },
  { path: 'productos', component: Productos, canActivate: [AuthGuard] },
  { path: 'compras', component: Compras, canActivate: [AuthGuard] },
  { path: 'ventas', component: Ventas, canActivate: [AuthGuard] },
  { path: '**', redirectTo: '/dashboard' }
];
