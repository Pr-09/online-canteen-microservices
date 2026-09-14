import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class PaymentService {
  constructor(private http: HttpClient) {}

  createPaymentOrder(request: any) {
    return this.http.post(`${environment.apiGateway}/api/payment/create-order`, request);
  }

  verifyPayment(request: any) {
    return this.http.post(`${environment.apiGateway}/api/payment/verify`, request);
  }
}
