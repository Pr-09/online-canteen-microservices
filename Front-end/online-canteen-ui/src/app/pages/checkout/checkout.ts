import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { Navbar } from '../../shared/navbar/navbar';
import { AdminService } from '../../services/admin.service';
import { CartService } from '../../services/cart.service';
import { OrderService } from '../../services/order.service';
import { Router, RouterEvent, RouterLink } from '@angular/router';
import { PaymentService } from '../../services/payment.service';
import { environment } from '../../environments/environment';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-checkout',
  standalone: true,
  imports: [CommonModule, FormsModule, Navbar],
  templateUrl: './checkout.html',
  styleUrl: './checkout.css',
})
export class Checkout implements OnInit {
  tableNumber = 1;

  paymentType = 'ONLINE';

  customerName = '';

  email = '';

  udharCode = '';

  total = 0;

  constructor(
    private cartService: CartService,
    private orderService: OrderService,
    private paymentService: PaymentService,
    private adminService: AdminService,
    private authService: AuthService,
    private router: Router,
  ) {
    this.total = this.cartService.getTotal();
  }

  ngOnInit(): void {
    const user = this.authService.getUser();

    if (user) {
      this.customerName = user.name;
      this.email = user.email;
    } else {
      /*
       * If user information is not loaded yet,
       * get it from backend.
       */
      this.authService.getCurrentUser().subscribe({
        next: (user) => {
          this.customerName = user.name;
          this.email = user.email;
        },

        error: (err) => {
          console.error('Failed to load user:', err);
        },
      });
    }
  }

  placeOrder() {
    const items = this.cartService.getCartItems().map((x) => ({
      menuId: x.item.id,
      quantity: x.quantity,
    }));

    const orderRequest = {
      tableNumber: this.tableNumber,
      paymentType: this.paymentType,
      customerName: this.customerName,
      email: this.email,
      udharCode: this.udharCode,
      items: items,
    };

    if (this.paymentType === 'UDHAR') {
      this.adminService.validateUdharCode(this.udharCode).subscribe({
        next: (res: any) => {
          if (res.valid) {
            this.createOrder(orderRequest);
          } else {
            alert(res.message);
          }
        },

        error: (err) => {
          console.error(err);
          alert('Udhar validation failed');
        },
      });

      return;
    }

    this.createOrder(orderRequest);
  }

  createOrder(orderRequest: any) {
    this.orderService.placeOrder(orderRequest).subscribe({
      next: (order: any) => {
        console.log('ORDER RESPONSE', order);

        if (this.paymentType === 'ONLINE') {
          this.startPayment(order);
        } else {
          this.cartService.clearCart();

          alert('Udhar Order Created Successfully');

          this.router.navigate(['/order-tracking', order.id]);
        }
      },

      error: (err) => {
        console.error(err);

        alert('Order creation failed');
      },
    });
  }

  startPayment(order: any) {
    const paymentRequest = {
      orderId: order.id,
      userId: order.userId,
      amount: order.totalAmount,
      paymentMethod: 'ONLINE',
    };

    this.paymentService.createPaymentOrder(paymentRequest).subscribe({
      next: (payment: any) => {
        this.openRazorpay(payment, order);
      },

      error: (err) => {
        console.error(err);
        alert('Payment order creation failed');
      },
    });
  }

  openRazorpay(payment: any, order: any) {
    const options = {
      key: environment.rozarpayKeyId,

      amount: order.totalAmount * 100,

      currency: 'INR',

      name: 'Online Canteen',

      description: 'Food Order',

      order_id: payment.razorpayOrderId,

      handler: (response: any) => {
        this.verifyPayment(response, order.id);
      },

      theme: {
        color: '#2563EB',
      },
    };

    const razorpay = new (window as any).Razorpay(options);

    razorpay.open();
  }

  verifyPayment(response: any, orderId: number) {
    const request = {
      razorpayOrderId: response.razorpay_order_id,

      razorpayPaymentId: response.razorpay_payment_id,

      razorpaySignature: response.razorpay_signature,
    };

    this.paymentService.verifyPayment(request).subscribe({
      next: () => {
        this.cartService.clearCart();

        this.router.navigate(['/order-tracking', orderId]);
      },

      error: (err) => {
        console.error(err);

        alert('Payment verification failed');
      },
    });
  }
}
