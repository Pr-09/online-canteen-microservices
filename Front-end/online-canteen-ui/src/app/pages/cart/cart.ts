import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';

import { Navbar } from '../../shared/navbar/navbar';

import { CartService } from '../../services/cart.service';

import { CartItem } from '../../models/cart-item';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [CommonModule, Navbar, RouterLink],
  templateUrl: './cart.html',
  styleUrl: './cart.css',
})
export class Cart implements OnInit {
  cartItems: CartItem[] = [];

  total = 0;

  constructor(private cartService: CartService) {}

  ngOnInit(): void {
    this.loadCart();
  }

  loadCart() {
    this.cartItems = this.cartService.getCartItems();

    this.total = this.cartService.getTotal();
  }

  increase(id: number) {
    this.cartService.increase(id);

    this.loadCart();
  }

  decrease(id: number) {
    this.cartService.decrease(id);

    this.loadCart();
  }

  remove(id: number) {
    this.cartService.removeItem(id);

    this.loadCart();
  }
}
