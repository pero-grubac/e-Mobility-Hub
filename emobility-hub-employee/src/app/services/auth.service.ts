import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../config/environment';
import { AuthRequest, AuthResponse } from '../models/auth.models';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  constructor(private http: HttpClient) {}

  baseUrl: string = `${environment.baseUrl}${environment.auth}`;
  login(data: AuthRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.baseUrl}/login`, data);
  }
}
