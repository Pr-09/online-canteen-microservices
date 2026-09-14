import { Component, OnInit } from '@angular/core';
import { UserProfile } from '../models/UserProfile';
import { AuthService } from '../services/auth.service';
import { Navbar } from "../shared/navbar/navbar";
@Component({
  selector: 'app-profile',
  imports: [Navbar],
  templateUrl: './profile.html',
  styleUrl: './profile.css',
})
export class Profile implements OnInit {
  user: UserProfile | null = null;

  constructor(private authService: AuthService) {}

  ngOnInit(): void {
    this.user = this.authService.getUser();

    /*
     * If user data is not loaded yet,
     * request it from backend.
     */
    if (!this.user) {
      this.authService.getCurrentUser().subscribe({
        next: (user) => {
          this.user = user;
        },

        error: (err) => {
          console.error('Failed to load profile:', err);
        },
      });
    }
  }
}
