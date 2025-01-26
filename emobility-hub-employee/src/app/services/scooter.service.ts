import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../config/environment';
import { DetailedScooter, ScooterPage } from '../models/scooter.model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ScooterService {
  constructor(private http: HttpClient) {}

  baseUrl: string = `${environment.baseUrl}${environment.scooters}`;

  getScooters(
    page: number,
    size: number,
    search?: string
  ): Observable<ScooterPage> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sort', 'id,desc');
    if (search) {
      params = params.set('search', search);
    }
    return this.http.get<ScooterPage>(`${this.baseUrl}/getByModel`, {
      params,
    });
  }
  getById(id: number): Observable<DetailedScooter> {
    return this.http.get<DetailedScooter>(`${this.baseUrl}/${id}`);
  }
  updateScooter(scooterData: FormData): Observable<any> {
    return this.http.put<any>(`${this.baseUrl}`, scooterData);
  }
  deleteScooter(id: number) {
    return this.http.delete(`${this.baseUrl}/${id}`);
  }
}
