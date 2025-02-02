import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

export const authGuard: CanActivateFn = (route, state) => {
  const router = inject(Router);

  const token = sessionStorage.getItem('jwt'); 
  if (token) {
    return true; 
  } else {
    return router.createUrlTree(['/login']);
  }
};
