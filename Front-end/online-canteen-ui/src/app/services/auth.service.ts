import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';

import { environment } from '../environments/environment';
import { LoginResponse } from '../models/LoginResponse';
import { UserProfile } from '../models/UserProfile';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private userSubject = new BehaviorSubject<UserProfile | null>(null);

  user$ = this.userSubject.asObservable();

  constructor(private http: HttpClient) {}

  googleLogin(token: string) {
    return this.http.post<LoginResponse>(`${environment.apiGateway}/api/auth/google`, {
      token: token,
    });
  }

  getCurrentUser(): Observable<UserProfile> {
    
    console.log('Fetching current user from backend...', this.getJwt());

    return this.http.get<UserProfile>(`${environment.apiGateway}/api/auth/me`);
  }

  saveJwt(jwt: string): void {
    localStorage.setItem('jwt', jwt);
  }

  getJwt(): string | null {
    return localStorage.getItem('jwt');
  }

  loadCurrentUser(): void {
    if (!this.isLoggedIn()) {
      this.userSubject.next(null);
      return;
    }

    this.getCurrentUser().subscribe({
      next: (user) => {
        this.userSubject.next(user);
      },
      error: (err) => {
        console.error('Failed to load current user:', err);

        this.userSubject.next(null);

        // If backend says token is invalid/expired,
        // remove it so the application treats the user as logged out.
        if (err.status === 401 || err.status === 403) {
          this.logout();
        }
      },
    });
  }

  getUser(): UserProfile | null {
    return this.userSubject.value;
  }

  getRole(): string {
    return this.userSubject.value?.role ?? '';
  }

  logout(): void {
    localStorage.removeItem('jwt');
    this.userSubject.next(null);
  }

  isLoggedIn(): boolean {
    return !!localStorage.getItem('jwt');
  }
}
