import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { environment } from '../../environments/environment';
//The package I mentioned earlier (angularx-social-login) is useful, but for your architecture I recommend using Google Identity Services (GIS) directly. It is Google's latest approach and works better with Angular 21.
//import{SocialAuthService} from '@abacritt/angularx-social-login';
//import { GoogleLoginProvider } from '@abacritt/angularx-social-login';

declare const google: any;

@Component({
  selector: 'app-login',
  standalone: true,
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login implements OnInit {
  constructor(
    private authService: AuthService,
    private router: Router,
    // private socialAuthService: SocialAuthService
  ) {}
  //YOUR_GOOGLE_CLIENT_ID provice this from index.html file in the head section on GPT phase 5 <meta name="google-signin-client_id" content="YOUR_GOOGLE_CLIENT_ID">
  ngOnInit(): void {
    google.accounts.id.initialize({
      client_id: environment.googleClientId,

      callback: (response: any) => {
        this.handleGoogleResponse(response);
      },
    });

    google.accounts.id.renderButton(
      document.getElementById('google-btn'),

      {
        theme: 'outline',
        size: 'large',
        width: 300,
      },
    );
  }

  handleGoogleResponse(response: any) {
    const googleToken = response.credential;
    console.log('Google Token:', googleToken);

    this.authService.googleLogin(googleToken).subscribe({
      next: (res: any) => {
        this.authService.saveJwt(res.token);
        console.log('JWT Token saved:', res.token);
        this.router.navigate(['/menu']);
      },

      error: (err) => {
        console.error(err);
      },
    });
  }
}
