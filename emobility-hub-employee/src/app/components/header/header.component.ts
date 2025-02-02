import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { jwtDecode } from 'jwt-decode';
import { CustomJwtPayload } from '../../models/jwt.models';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrl: './header.component.css',
})
export class HeaderComponent implements OnInit {
  userRole: string | null = null;

  constructor(private router: Router) {}

  ngOnInit(): void {
    const token = localStorage.getItem('jwt');
    if (token) {
      try {
        const decodedToken = jwtDecode<CustomJwtPayload>(token);
        this.userRole = decodedToken.role;
      } catch (error) {
        console.error('Invalid token:', error);
        this.userRole = null;
      }
    }
  }

  onLogout(): void {
    localStorage.removeItem('jwt');

    this.router.navigate(['/login']);
  }
  hasRole(requiredRoles: string[]): boolean {
    return this.userRole ? requiredRoles.includes(this.userRole) : false;
  }
}
