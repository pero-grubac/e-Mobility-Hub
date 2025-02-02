import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../config/environment';
import { AuthRequest } from '../models/auth.models';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  constructor(private http: HttpClient) {}

  baseUrl: string = `${environment.baseUrl}${environment.auth}`;
  login(data: AuthRequest): Observable<string> {
    return this.http.post<any>(`${this.baseUrl}/login`, data);
  }
}
