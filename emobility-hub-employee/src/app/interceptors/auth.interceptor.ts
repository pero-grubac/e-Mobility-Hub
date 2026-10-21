import { Injectable, Injector } from '@angular/core';
import {
  HttpErrorResponse,
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest,
} from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, catchError, throwError } from 'rxjs';
import { SessionService } from '../services/session.service';

// The session cookie travels with every same-origin request by itself.
// When the backend answers 401 the session has expired: forget it and
// send the user to the login page.
@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  // SessionService needs HttpClient, which needs this interceptor, so it is
  // looked up lazily to avoid a circular dependency.
  constructor(
    private injector: Injector,
    private router: Router,
  ) {}

  intercept(
    req: HttpRequest<unknown>,
    next: HttpHandler,
  ): Observable<HttpEvent<unknown>> {
    return next.handle(req).pipe(
      catchError((error: unknown) => {
        if (
          error instanceof HttpErrorResponse &&
          error.status === 401 &&
          !req.url.includes('/auth/')
        ) {
          this.injector.get(SessionService).clear();
          this.router.navigate(['/login']);
        }
        return throwError(() => error);
      }),
    );
  }
}
