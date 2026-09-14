import { Component, OnInit } from '@angular/core';
import { OrderService } from '../../services/order.service';
import { Navbar } from '../../shared/navbar/navbar';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-my-orders',
  imports: [Navbar, CommonModule, RouterLink],
  templateUrl: './my-orders.html',
  styleUrl: './my-orders.css',
})
export class MyOrders implements OnInit {
  orders: any[] = [];

  constructor(private orderService: OrderService) {}

  ngOnInit(): void {
    this.loadOrders();
  }

  loadOrders() {
    this.orderService.getMyOrders().subscribe({
      next: (res: any) => {
        this.orders = res;
        for (let order of this.orders) {
          console.log('id :', order.id, 'order :', order);
        }
      },
    });
  }
}
