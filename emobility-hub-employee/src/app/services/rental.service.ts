import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../config/environment';
import { Observable } from 'rxjs';
import { DetailedRental, RentalPage } from '../models/rental.model';

@Injectable({
  providedIn: 'root',
})
export class RentalService {
  constructor(private http: HttpClient) {}

  baseUrl: string = `${environment.baseUrl}${environment.rentals}`;
  getAllByVehicleId(
    page: number,
    size: number,
    id: number
  ): Observable<RentalPage> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sort', 'id,desc');
    return this.http.get<RentalPage>(
      `${this.baseUrl}/getAllByVehicleId/${id}`,
      {
        params,
      }
    );
  }

  getAllByClientId(
    page: number,
    size: number,
    id: number
  ): Observable<RentalPage> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sort', 'id,desc');
    return this.http.get<RentalPage>(`${this.baseUrl}/getAllByClientId/${id}`, {
      params,
    });
  }

  getById(id: number): Observable<DetailedRental> {
    return this.http.get<DetailedRental>(`${this.baseUrl}/${id}`);
  }
  getAll(page: number, size: number): Observable<RentalPage> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sort', 'id,desc');
    return this.http.get<RentalPage>(`${this.baseUrl}`, {
      params,
    });
  }
}
