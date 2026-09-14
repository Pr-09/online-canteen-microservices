import { Injectable } from '@angular/core';
import { CartItem } from '../models/cart-item';
import { MenuItem } from '../models/menu-item';

@Injectable({
  providedIn: 'root',
})
export class CartService {
  private cart: CartItem[] = [];

  constructor() {
    const savedCart = localStorage.getItem('cart');

    if (savedCart) {
      this.cart = JSON.parse(savedCart);
    }
  }

  addToCart(item: MenuItem) {
    const existingItem = this.cart.find((c) => c.item.id === item.id);

    if (existingItem) {
      existingItem.quantity++;
    } else {
      this.cart.push({
        item,
        quantity: 1,
      });
    }

    this.saveCart();
  }

  getCartItems(): CartItem[] {
    return this.cart;
  }

  removeItem(itemId: number) {
    this.cart = this.cart.filter((x) => x.item.id !== itemId);

    this.saveCart();
  }

  increase(itemId: number) {
    const item = this.cart.find((x) => x.item.id === itemId);

    if (item) {
      item.quantity++;

      this.saveCart();
    }
  }

  decrease(itemId: number) {
    const item = this.cart.find((x) => x.item.id === itemId);

    if (item && item.quantity > 1) {
      item.quantity--;

      this.saveCart();
    }
  }

  getTotal(): number {
    return this.cart.reduce(
      (sum, item) => sum + item.item.price * item.quantity,

      0,
    );
  }

  clearCart() {
    this.cart = [];

    localStorage.removeItem('cart');
  }

  private saveCart() {
    localStorage.setItem('cart', JSON.stringify(this.cart));
  }
}
