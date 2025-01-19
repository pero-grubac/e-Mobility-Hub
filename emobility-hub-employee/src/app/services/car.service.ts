import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CarPage } from '../models/car.models';
import { environment } from '../config/environment';

@Injectable({
  providedIn: 'root',
})
export class CarService {
  constructor(private http: HttpClient) {}

  getCars(page: number, size: number, search?: string): Observable<CarPage> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    if (search) {
      params = params.set('search', search);
    }
 
    return this.http.get<CarPage>(
      `${environment.baseUrl}${environment.cars}/getByModel`,
      { params }
    );
  }
}
