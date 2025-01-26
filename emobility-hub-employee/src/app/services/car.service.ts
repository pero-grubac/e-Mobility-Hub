import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CarPage, DetailedCar } from '../models/car.models';
import { environment } from '../config/environment';

@Injectable({
  providedIn: 'root',
})
export class CarService {
  constructor(private http: HttpClient) {}

  baseUrl: string = `${environment.baseUrl}${environment.cars}`;

  getCars(page: number, size: number, search?: string): Observable<CarPage> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sort', 'id,desc');
    if (search) {
      params = params.set('search', search);
    }

    return this.http.get<CarPage>(`${this.baseUrl}/getByModel`, { params });
  }
  getById(id: number): Observable<DetailedCar> {
    return this.http.get<DetailedCar>(`${this.baseUrl}/${id}`);
  }
  updateCar(carData: FormData): Observable<any> {
    return this.http.put<any>(`${this.baseUrl}`, carData);
  }
  deleteCar(id: number) {
    return this.http.delete(`${this.baseUrl}/${id}`);
  }
  addCar(carData: FormData): Observable<any> {
    return this.http.post<any>(`${this.baseUrl}`, carData);
  }
}
