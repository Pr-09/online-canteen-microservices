import { Injectable } from '@angular/core';
import { environment } from '../environments/environment';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class AdminService {
  constructor(private http: HttpClient) {}

  getUdharCustomers() {
    return this.http.get(`${environment.apiGateway}/api/admin/udhar/customers`);
  }

  addCustomer(customer: any) {
    return this.http.post(`${environment.apiGateway}/api/admin/udhar/customer`, customer);
  }

  generateCode(userId: number, amount: number) {
    console.log('userId:', userId, 'amount:', amount);

    return this.http.post(
      `${environment.apiGateway}/api/admin/udhar/generate-code?userId=${userId}&amount=${amount}`,
      {},
    );
  }

  validateUdharCode(code: string) {
    return this.http.post(`${environment.apiGateway}/api/admin/udhar/validate`, {
      code: code,
    });
  }

  updateLimit(userId: number, newLimit: number) {
    return this.http.put(
      `${environment.apiGateway}/api/admin/udhar/update-limit?userId=${userId}&newLimit=${newLimit}`,
      {},
    );
  }

  paymentReceived(userId: number, amount: number) {
    return this.http.put(
      `${environment.apiGateway}/api/admin/udhar/payment-received?userId=${userId}&amount=${amount}`,
      {},
    );
  }
}
