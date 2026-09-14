import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class OrderService {
  constructor(private http: HttpClient) {} 

  placeOrder(order: any) {
    return this.http.post(`${environment.apiGateway}/api/order`, order);
  }

  getOrderById(orderId: number) {
    return this.http.get(`${environment.apiGateway}/api/order/${orderId}`);
  }
  
  getMyOrders() {
    return this.http.get(`${environment.apiGateway}/api/order/my-orders`);
  }
   
  getAllOrders() {
    return this.http.get(`${environment.apiGateway}/api/order/all`);
  }

  updateOrderStatus(orderId: number, status: string) {
    return this.http.put(
      `${environment.apiGateway}/api/order/${orderId}/status`,

      {
        status,
      },
    );
  }
}
