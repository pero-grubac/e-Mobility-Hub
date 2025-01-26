import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../config/environment';
import { BicyclePage, DetailedBicycle } from '../models/bicycle.models';

@Injectable({
  providedIn: 'root',
})
export class BicycleService {
  constructor(private http: HttpClient) {}

  baseUrl: string = `${environment.baseUrl}${environment.bicycles}`;

  getBicycles(
    page: number,
    size: number,
    search?: string
  ): Observable<BicyclePage> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sort', 'id,desc');
    if (search) {
      params = params.set('search', search);
    }
    return this.http.get<BicyclePage>(`${this.baseUrl}/getByModel`, {
      params,
    });
  }
  getById(id: number): Observable<DetailedBicycle> {
    return this.http.get<DetailedBicycle>(`${this.baseUrl}/${id}`);
  }
  updateBicycle(bicycleData: FormData): Observable<any> {
    return this.http.put<any>(`${this.baseUrl}`, bicycleData);
  }
  deleteBicycle(id: number) {
    return this.http.delete(`${this.baseUrl}/${id}`);
  }
  addBicycle(bicycleData: FormData): Observable<any> {
    return this.http.post<any>(`${this.baseUrl}`, bicycleData);
  }
}
