import { Component } from '@angular/core';
import { AuthRequest } from '../../models/auth.models';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { SessionService } from '../../services/session.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
})
export class LoginComponent {
  authRequest: AuthRequest = { username: '', password: '' };
  errorMessage: string = '';
  showPassword: boolean = false;
  submitting: boolean = false;

  constructor(
    private authService: AuthService,
    private router: Router,
    private sessionService: SessionService,
  ) {}

  onSubmit(): void {
    this.submitting = true;
    this.errorMessage = '';
    this.authService.login(this.authRequest).subscribe({
      next: (response) => {
        this.submitting = false;
        // The backend already set the httpOnly session cookie.
        this.sessionService.set(response);
        this.router.navigate(['/']);
      },
      error: (error) => {
        this.submitting = false;
        console.error('Login failed:', error);
        this.errorMessage = 'Invalid username or password';
      },
    });
  }
}
