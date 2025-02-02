import { Injectable } from '@angular/core';
import { jwtDecode } from 'jwt-decode';
import { CustomJwtPayload } from '../models/jwt.models';

@Injectable({
  providedIn: 'root',
})
export class JWTService {
  private getToken(): string | null {
    return localStorage.getItem('jwt');
  }

  decodeToken(): CustomJwtPayload | null {
    const token = this.getToken();
    if (!token) return null;

    try {
      return jwtDecode<CustomJwtPayload>(token);
    } catch (error) {
      console.error('Invalid token:', error);
      return null;
    }
  }
  userRole: string | null = null;

  getRole(): string | null {
    const decodedToken = this.decodeToken();
    return decodedToken ? decodedToken.role : null;
  }

  hasRole(requiredRoles: string[]): boolean {
    const role = this.getRole();
    return role ? requiredRoles.includes(role) : false;
  }

  removeJWT(): void {
    localStorage.removeItem('jwt');
  }
  setJWT(token: string): void {
    localStorage.setItem('jwt', token);
  }
}
