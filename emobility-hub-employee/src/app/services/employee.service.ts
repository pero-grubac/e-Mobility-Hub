import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../config/environment';
import { Observable } from 'rxjs';
import { UserPage } from '../models/user.model';

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
}
