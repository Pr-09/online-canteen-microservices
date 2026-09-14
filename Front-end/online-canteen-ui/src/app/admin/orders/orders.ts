import { Component, OnInit } from '@angular/core';
import { OrderService } from '../../services/order.service';
import { CommonModule } from '@angular/common';
// import { RouterLink } from '@angular/router';
import { AdminNavbar } from '../admin-navbar/admin-navbar';
import { RouterLink } from '@angular/router';
@Component({
  selector: 'app-orders',
  imports: [CommonModule, AdminNavbar],
  templateUrl: './orders.html',
  styleUrl: './orders.css',
})
export class Orders implements OnInit {
  orders: any[] = [];

  constructor(private orderService: OrderService) {}

  ngOnInit(): void {
    this.loadOrders();
  }

  loadOrders() {
    this.orderService.getAllOrders().subscribe({
      next: (res: any) => {
        this.orders = res;
      },
    });
  }

  updateStatus(orderId: number, status: string) {
    this.orderService.updateOrderStatus(orderId, status).subscribe({
      next: () => {
        this.loadOrders();
      },
    });
  }
}
