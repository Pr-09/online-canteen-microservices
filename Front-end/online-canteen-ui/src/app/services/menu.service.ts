import { Injectable } from '@angular/core';
import { MenuItem } from '../models/menu-item';
import { HttpClient } from '@angular/common/http';
import { environment } from '../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class MenuService {
  constructor(private http: HttpClient) {}
  getAllMenuItems() {
    return this.http.get(`${environment.apiGateway}/api/menu/all`);
  }

  addMenuItem(formData: FormData) {
    return this.http.post(
      `${environment.apiGateway}/api/menu`,

      formData,
    );
  }

  updateMenuItem(id: number, item: any) {
    return this.http.put(`${environment.apiGateway}/api/menu/${id}`, item);
  }

  deleteMenuItem(id: number) {
    return this.http.delete(`${environment.apiGateway}/api/menu/${id}`);
  }
  // getMenu(): MenuItem[] {
  //   return [
  //     {
  //       id: 1,
  //       name: 'Veg Burger',
  //       description: 'Fresh Veg Burger',
  //       price: 120,
  //       prepTime: 10,
  //       imageUrl: 'https://picsum.photos/300/200?1',
  //       category: 'Snacks',
  //       available: true,
  //     },

  //     {
  //       id: 2,
  //       name: 'Cold Coffee',
  //       description: 'Chilled Coffee',
  //       price: 90,
  //       prepTime: 5,
  //       imageUrl: 'https://picsum.photos/300/200?2',
  //       category: 'Beverages',
  //       available: true,
  //     },

  //     {
  //       id: 3,
  //       name: 'Pizza',
  //       description: 'Cheese Pizza',
  //       price: 220,
  //       prepTime: 15,
  //       imageUrl: 'https://picsum.photos/300/200?3',
  //       category: 'Meals',
  //       available: true,
  //     },
  //   ];
  // }
}
