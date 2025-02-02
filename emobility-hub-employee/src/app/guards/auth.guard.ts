import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivateFn, Router } from '@angular/router';
import { jwtDecode } from 'jwt-decode';
import { CustomJwtPayload } from '../models/jwt.models';

export const authGuard: CanActivateFn = (route: ActivatedRouteSnapshot) => {
  const router = inject(Router);

  const token = localStorage.getItem('jwt');
  if (token) {
    try {
      const decodedToken = jwtDecode<CustomJwtPayload>(token);
      const userRole = decodedToken.role;
      const requiredRoles: string[] = route.data['roles'] || [];

      if (requiredRoles.length === 0 || requiredRoles.includes(userRole)) {
        return true;
      } else {
        return router.createUrlTree(['/login']);
      }
    } catch (error) {
      console.error('Invalid token:', error);
      return router.createUrlTree(['/login']);
    }
  } else {
    return router.createUrlTree(['/login']);
  }
};
