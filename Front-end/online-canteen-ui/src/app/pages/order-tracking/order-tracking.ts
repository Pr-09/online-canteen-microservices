import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { OrderService } from '../../services/order.service';
import { Navbar } from '../../shared/navbar/navbar';
// import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-order-tracking',
  imports: [Navbar],
  templateUrl: './order-tracking.html',
  styleUrl: './order-tracking.css',
})
export class OrderTracking implements OnInit {
  order: any;

  orderId!: number;

  constructor(
    private route: ActivatedRoute,

    private orderService: OrderService,
  ) {}
  ngOnInit(): void {
    this.orderId = Number(this.route.snapshot.paramMap.get('id'));
    //
    this.loadOrder();

    console.log('orderId:', this.orderId);
    console.log('order prince :', this.order.status);

    /*
      polling
      later replace by websocket
    */
    this.loadOrder();
    // setInterval(() => {
    //   this.loadOrder();
    // }, 5000);
  }

  loadOrder() {
    this.orderService.getOrderById(this.orderId).subscribe({
      next: (res) => {
        this.order = res;
        console.log('order:', this.order);
        console.log('order 123:', this.order.status);
      },
    });
  }

  getStep(status: string) {
    switch (status) {
      case 'PENDING':
        return 1;

      case 'ACCEPTED':
        return 2;

      case 'PREPARING':
        return 3;

      case 'READY':
        return 4;

      case 'DELIVERED':
        return 5;

      default:
        return 0;
    }
  }
}
