import { Component } from '@angular/core';
import { CommonModule } from '@angular/common'; 
import { AdminNavbar } from '../admin-navbar/admin-navbar';  
@Component({
  selector: 'app-dashboard',
  imports: [CommonModule, AdminNavbar],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css',
})
export class Dashboard {}
