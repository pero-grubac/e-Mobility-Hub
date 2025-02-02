import { Component } from '@angular/core';
import { AuthRequest } from '../../models/auth.models';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { JWTService } from '../../services/jwt.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
})
export class LoginComponent {
  authRequest: AuthRequest = { username: '', password: '' };
  errorMessage: string = '';

  constructor(
    private authService: AuthService,
    private router: Router,
    private jwtService: JWTService
  ) {}

  onSubmit(): void {
    this.authService.login(this.authRequest).subscribe({
      next: (response) => {
        const token = response.token;
        this.jwtService.setJWT(token);
        this.router.navigate(['/']);
      },
      error: (error) => {
        console.error('Login failed:', error);
        this.errorMessage = 'Invalid username or password';
      },
    });
  }
}
