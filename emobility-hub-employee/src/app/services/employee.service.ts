import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../config/environment';
import { Observable } from 'rxjs';
import { DetailedUserRequest, User, UserPage } from '../models/user.model';

@Injectable({
  providedIn: 'root',
})
export class EmployeeService {
  constructor(private http: HttpClient) {}

  baseUrl: string = `${environment.baseUrl}${environment.employees}`;

  getEmployees(
    page: number,
    size: number,
    search?: string
  ): Observable<UserPage> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sort', 'id,desc');
    if (search) {
      params = params.set('search', search);
    }
    return this.http.get<UserPage>(`${this.baseUrl}/getAllByUsername`, {
      params,
    });
  }
  getById(id: number): Observable<User> {
    return this.http.get<User>(`${this.baseUrl}/${id}`);
  }
  update(user: DetailedUserRequest): Observable<any> {
    return this.http.put<any>(`${this.baseUrl}`, user);
  }
  delete(id: number): Observable<any> {
    return this.http.delete<any>(`${this.baseUrl}/${id}`);
  }
  changeRole(id: number, role: string): Observable<any> {
    return this.http.put<any>(`${this.baseUrl}/changeRole/${id}`, role, {
      headers: { 'Content-Type': 'application/json' },
    });
  }
  register(user: DetailedUserRequest): Observable<any> {
    return this.http.post<any>(`${this.baseUrl}/register`, user);
  }
}
