import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-admin-navbar',
  imports: [RouterLink],
  templateUrl: './admin-navbar.html',
  styleUrl: './admin-navbar.css',
})
export class AdminNavbar {
  constructor(private authService: AuthService) {}
  isAdmin = false;

  // Need to be change in ADMIN only admin will come. intead of Role_Admin
  ngOnInit() {
    this.isAdmin = this.authService.getRole() === 'ROLE_ADMIN';
  }
}
