import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, catchError, map, of, tap } from 'rxjs';
import { environment } from '../config/environment';
import { Session } from '../models/auth.models';

// The JWT lives in an httpOnly cookie the app cannot read. The backend tells
// us who is signed in through /auth/me; we cache that here.
@Injectable({
  providedIn: 'root',
})
export class SessionService {
  private readonly baseUrl = `${environment.baseUrl}${environment.auth}`;
  private session: Session | null = null;

  constructor(private http: HttpClient) {}

  // Resolves to the current session, asking the backend only when needed.
  ensureLoaded(): Observable<Session | null> {
    if (this.isActive()) {
      return of(this.session);
    }
    return this.http.get<Session>(`${this.baseUrl}/me`).pipe(
      tap((session) => this.set(session)),
      map(() => (this.isActive() ? this.session : null)),
      catchError(() => {
        this.clear();
        return of(null);
      }),
    );
  }

  set(session: Session): void {
    this.session = {
      username: session.username,
      role: session.role,
      expiresAt: session.expiresAt,
    };
  }

  clear(): void {
    this.session = null;
  }

  isActive(): boolean {
    return (
      this.session !== null &&
      Date.parse(this.session.expiresAt) > Date.now()
    );
  }

  getRole(): string | null {
    return this.isActive() ? this.session!.role : null;
  }

  hasRole(requiredRoles: string[]): boolean {
    const role = this.getRole();
    return role ? requiredRoles.includes(role) : false;
  }

  logout(): Observable<void> {
    this.clear();
    return this.http.post<void>(`${this.baseUrl}/logout`, {});
  }
}
