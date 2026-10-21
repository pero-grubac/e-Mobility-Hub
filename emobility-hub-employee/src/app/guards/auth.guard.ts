import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivateFn, Router } from '@angular/router';
import { map } from 'rxjs';
import { SessionService } from '../services/session.service';

export const authGuard: CanActivateFn = (route: ActivatedRouteSnapshot) => {
  const router = inject(Router);
  const sessionService = inject(SessionService);
  const requiredRoles: string[] = route.data['roles'] || [];

  return sessionService.ensureLoaded().pipe(
    map((session) => {
      if (!session) {
        return router.createUrlTree(['/login']);
      }
      if (requiredRoles.length === 0 || requiredRoles.includes(session.role)) {
        return true;
      }
      // Signed in, but not allowed here: back to the start page.
      return router.createUrlTree(['/']);
    }),
  );
};
