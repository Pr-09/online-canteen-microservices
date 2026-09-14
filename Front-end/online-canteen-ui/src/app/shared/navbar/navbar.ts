import { Component, OnInit } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';

import { AuthService } from '../../services/auth.service';
import { UserProfile } from '../../models/UserProfile';

declare const google: any;

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [RouterLink, CommonModule],
  templateUrl: './navbar.html',
  styleUrl: './navbar.css',
})
export class Navbar implements OnInit {
  user: UserProfile | null = null;

  profileMenuOpen = false;

  constructor(
    private authService: AuthService,
    private router: Router,
  ) {}

  ngOnInit(): void {
  
    this.authService.loadCurrentUser();

   
    this.authService.user$.subscribe({
      next: (user) => {
        this.user = user;
      },
    });

    
    if (typeof google !== 'undefined' && google.accounts) {
      google.accounts.id.initialize({
        client_id: '64355227379-s2254hgh8j58cg6p48v9qr3.apps.googleusercontent.com',
        callback: this.handleGoogleSuccess.bind(this),
      });
    }
  }

  login(): void {
    if (typeof google !== 'undefined' && google.accounts) {
      google.accounts.id.prompt();
    } else {
      console.error('Google Identity Services is not loaded.');
    }
  }

  handleGoogleSuccess(response: any): void {
    const googleToken = response.credential;

    this.authService.googleLogin(googleToken).subscribe({
      next: (res) => {
        /*
         * Store JWT after successful login.
         */
        this.authService.saveJwt(res.token);

        /*
         * Get actual user information from backend.
         */
        this.authService.loadCurrentUser();

        this.router.navigate(['/']);
      },

      error: (err) => {
        console.error('Google login failed:', err);

        alert('Login failed. Please try again.');
      },
    });
  }

  toggleProfile(): void {
    this.profileMenuOpen = !this.profileMenuOpen;
  }

  openProfile(): void {
    this.profileMenuOpen = false;

    this.router.navigate(['/profile']);
  }

  openOrders(): void {
    this.profileMenuOpen = false;

    this.router.navigate(['/my-orders']);
  }

  logout(): void {
    this.profileMenuOpen = false;

    this.authService.logout();

    this.router.navigate(['/']);
  }
}
