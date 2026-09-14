import { Component, OnInit } from '@angular/core';
import { AdminService } from '../../services/admin.service';
//import { Router } from '@angular/router';
import { AdminNavbar } from '../admin-navbar/admin-navbar';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-udhar-management',
  imports: [AdminNavbar, FormsModule],
  templateUrl: './udhar-management.html',
  styleUrl: './udhar-management.css',
})
export class UdharManagement implements OnInit {
  customers: any[] = [];

  customer = {
    userId: 0,

    name: '',

    email: '',

    mobile: '',

    studentId: '',
  };

  constructor(private adminService: AdminService) {}

  ngOnInit(): void {
    this.loadCustomers();
  }

  loadCustomers() {
    this.adminService.getUdharCustomers().subscribe({
      next: (res: any) => {
        this.customers = res;
      },
    });
  }

  addCustomer() {
    this.adminService.addCustomer(this.customer).subscribe({
      next: () => {
        this.loadCustomers();
      },
    });
  }

  generateCode(customer: any) {
    const amount = prompt('Enter Amount');

    if (!amount) return;
    console.log('Uhdar amoount', amount);

    this.adminService.generateCode(customer.userId, Number(amount)).subscribe({
      next: (res: any) => {
        alert('Code : ' + res.code);
      },
    });
  }

  increaseLimit(customer: any) {
    const limit = prompt('New Limit');

    if (!limit) return;

    this.adminService.updateLimit(customer.userId, Number(limit)).subscribe({
      next: () => {
        this.loadCustomers();
      },
    });
  }

  markPaid(customer: any) {
    const amount = prompt('Received Amount');

    if (!amount) return;

    this.adminService.paymentReceived(customer.userId, Number(amount)).subscribe({
      next: () => {
        this.loadCustomers();
      },
    });
  }
}
