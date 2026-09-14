import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { MenuService } from '../../services/menu.service';

import { Navbar } from '../../shared/navbar/navbar';
import { MenuItem } from '../../models/menu-item';
import { CartService } from '../../services/cart.service';

@Component({
  selector: 'app-menu',
  imports: [CommonModule, Navbar],
  templateUrl: './menu.html',
  styleUrl: './menu.css',
})
export class Menu implements OnInit {
  menuItems: MenuItem[] = [];

  constructor(
    private menuService: MenuService,
    private cartService: CartService,
  ) {}

  addToCart(item: MenuItem) {
    this.cartService.addToCart(item);

    alert('Added to cart');
  }

  ngOnInit(): void {
    this.menuService.getAllMenuItems().subscribe({
      next: (res: any) => {
        this.menuItems = res;
      },
    });
  }
}
