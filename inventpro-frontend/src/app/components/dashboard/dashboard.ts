import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth';
import { UserProfile } from '../../models/auth.models';
import { EstadisticasService } from '../../services/estadisticas.service';
import { Layout } from '../layout/layout';

@Component({
  selector: 'app-dashboard',
  imports: [CommonModule, Layout],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css'
})
export class DashboardComponent implements OnInit {

  currentUser: UserProfile | null = null;

  // Datos dinámicos del dashboard
  estadisticas: any = {
    totalVentas: 0,
    totalProductos: 0,
    totalCategorias: 0,
    ventasHoy: 0,
    productosStockBajo: 0
  };

  // Reportes de ventas
  salesReports: any = {
    ventasPorDia: [],
    ventasPorMes: [],
    topProductosVendidos: []
  };

  // Reportes de productos
  productReports: any = {
    productosPorCategoria: [],
    productosStockBajo: []
  };

  // Reportes de inventario
  inventoryReports: any = {
    valorTotalInventario: 0,
    productosStockBajo: 0,
    productosStockMedio: 0,
    productosStockAlto: 0
  };

  // Variables para cálculos de gráficos
  maxVentaDia = 0;
  maxVentaMes = 0;
  maxCantidadVendida = 0;
  maxProductosCategoria = 0;

  loading = true;
  error = false;

  constructor(
    private authService: AuthService,
    private router: Router,
    private estadisticasService: EstadisticasService
  ) {}

  ngOnInit(): void {
    // Verificar si el usuario está logueado
    if (!this.authService.isLoggedIn()) {
      this.router.navigate(['/auth']);
      return;
    }

    // Obtener información del usuario actual
    this.authService.currentUser$.subscribe(user => {
      this.currentUser = user;
    });

    // Cargar datos del dashboard
    this.cargarDatosDashboard();
  }

  cargarDatosDashboard(): void {
    this.loading = true;
    this.error = false;

    // Cargar todos los datos en paralelo
    Promise.all([
      this.cargarEstadisticasBasicas(),
      this.cargarReportesVentas(),
      this.cargarReportesProductos(),
      this.cargarReportesInventario()
    ]).then(() => {
      this.loading = false;
    }).catch((error) => {
      console.error('Error al cargar datos del dashboard:', error);
      this.error = true;
      this.loading = false;
    });
  }

  private cargarEstadisticasBasicas(): Promise<void> {
    return new Promise((resolve, reject) => {
      this.estadisticasService.getDashboardData().subscribe({
        next: (data: any) => {
          this.estadisticas = {
            totalVentas: data.totalVentas || 0,
            totalProductos: data.totalProductos || 0,
            totalCategorias: data.totalCategorias || 0,
            ventasHoy: data.ventasHoy || 0,
            productosStockBajo: data.productosStockBajo || 0
          };
          resolve();
        },
        error: (error: any) => {
          console.error('Error al cargar estadísticas básicas:', error);
          reject(error);
        }
      });
    });
  }

  private cargarReportesVentas(): Promise<void> {
    return new Promise((resolve, reject) => {
      this.estadisticasService.getSalesReports().subscribe({
        next: (data: any) => {
          this.salesReports = {
            ventasPorDia: data.ventasPorDia || [],
            ventasPorMes: data.ventasPorMes || [],
            topProductosVendidos: data.topProductosVendidos || []
          };
          this.calcularMaximosVentas();
          resolve();
        },
        error: (error: any) => {
          console.error('Error al cargar reportes de ventas:', error);
          reject(error);
        }
      });
    });
  }

  private cargarReportesProductos(): Promise<void> {
    return new Promise((resolve, reject) => {
      this.estadisticasService.getProductReports().subscribe({
        next: (data: any) => {
          this.productReports = {
            productosPorCategoria: data.productosPorCategoria || [],
            productosStockBajo: data.productosStockBajo || []
          };
          this.calcularMaximosProductos();
          resolve();
        },
        error: (error: any) => {
          console.error('Error al cargar reportes de productos:', error);
          reject(error);
        }
      });
    });
  }

  private cargarReportesInventario(): Promise<void> {
    return new Promise((resolve, reject) => {
      this.estadisticasService.getInventoryReports().subscribe({
        next: (data: any) => {
          this.inventoryReports = {
            valorTotalInventario: data.valorTotalInventario || 0,
            productosStockBajo: data.productosStockBajo || 0,
            productosStockMedio: data.productosStockMedio || 0,
            productosStockAlto: data.productosStockAlto || 0
          };
          resolve();
        },
        error: (error: any) => {
          console.error('Error al cargar reportes de inventario:', error);
          reject(error);
        }
      });
    });
  }

  private calcularMaximosVentas(): void {
    // Calcular máximo de ventas por día
    if (this.salesReports.ventasPorDia && this.salesReports.ventasPorDia.length > 0) {
      this.maxVentaDia = Math.max(...this.salesReports.ventasPorDia.map((item: any) => item.total));
    }

    // Calcular máximo de ventas por mes
    if (this.salesReports.ventasPorMes && this.salesReports.ventasPorMes.length > 0) {
      this.maxVentaMes = Math.max(...this.salesReports.ventasPorMes.map((item: any) => item.total));
    }

    // Calcular máximo de cantidad vendida
    if (this.salesReports.topProductosVendidos && this.salesReports.topProductosVendidos.length > 0) {
      this.maxCantidadVendida = Math.max(...this.salesReports.topProductosVendidos.map((item: any) => item.cantidadVendida));
    }
  }

  private calcularMaximosProductos(): void {
    // Calcular máximo de productos por categoría
    if (this.productReports.productosPorCategoria && this.productReports.productosPorCategoria.length > 0) {
      this.maxProductosCategoria = Math.max(...this.productReports.productosPorCategoria.map((item: any) => item.cantidad));
    }
  }

  formatearMoneda(valor: number): string {
    return new Intl.NumberFormat('es-CO', {
      style: 'currency',
      currency: 'COP',
      minimumFractionDigits: 0
    }).format(valor);
  }

  recargarDatos(): void {
    console.log('🔄 Recargando todos los datos del dashboard...');
    this.cargarDatosDashboard();
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/auth']);
  }
}
