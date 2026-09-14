import { Routes } from '@angular/router';

import { Home } from './pages/home/home';
// import { Menu } from './pages/menu/menu';
// import { Cart } from './pages/cart/cart';
// import { Checkout } from './pages/checkout/checkout';
import { Login } from './pages/login/login';
import { authGuard } from './core/guards/auth.guard';
// import { OrderTracking } from './pages/order-tracking/order-tracking';
// import { MyOrders } from './pages/my-orders/my-orders';
// import { Dashboard } from './admin/dashboard/dashboard';
// import { Orders } from './admin/orders/orders';
// import { MenuManagement } from './admin/menu-management/menu-management';
// import { UdharManagement } from './admin/udhar-management/udhar-management';

export const routes: Routes = [
  {
    path: '',
    component: Home,
  },
  {
    path: 'menu',
    loadComponent: () => import('./pages/menu/menu').then((m) => m.Menu),
  },
  {
    path: 'cart',
    loadComponent: () => import('./pages/cart/cart').then((m) => m.Cart),
  },
  {
    path: 'checkout',
    loadComponent: () => import('./pages/checkout/checkout').then((m) => m.Checkout),
    canActivate: [authGuard],
  },
  {
    path: 'login',
    component: Login,
  },
  {
    path: 'order-tracking/:id',
    loadComponent: () =>
      import('./pages/order-tracking/order-tracking').then((m) => m.OrderTracking),
    canActivate: [authGuard],
  },

  {
    path: 'my-orders',
    loadComponent: () => import('./pages/my-orders/my-orders').then((m) => m.MyOrders),
    canActivate: [authGuard],
  },
  {
    path: 'admin/dashboard',
    loadComponent: () => import('./admin/dashboard/dashboard').then((m) => m.Dashboard),
    canActivate: [authGuard],
  },
  {
    path: 'admin/orders',
    loadComponent: () => import('./admin/orders/orders').then((m) => m.Orders),
    canActivate: [authGuard],
  },
  {
    path: 'admin/menu-management',
    loadComponent: () =>
      import('./admin/menu-management/menu-management').then((m) => m.MenuManagement),
    canActivate: [authGuard],
  },
  {
    path: 'admin/udhar-management',
    loadComponent: () =>
      import('./admin/udhar-management/udhar-management').then((m) => m.UdharManagement),
    canActivate: [authGuard],
  },
  {
    path: 'profile',
    loadComponent: () => import('./profile/profile').then((m) => m.Profile),
    canActivate: [authGuard],
  },
  {
    path: '**',
    redirectTo: '',
  },
  // {
  //   path: 'admin/dashboard',
  //   component: Dashboard,
  //   canActivate: [authGuard],
  // },
];
