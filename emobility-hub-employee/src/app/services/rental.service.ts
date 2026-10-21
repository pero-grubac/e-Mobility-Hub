import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../config/environment';
import { Observable } from 'rxjs';
import { DetailedRental, RentalPage } from '../models/rental.model';

@Injectable({
  providedIn: 'root',
})
export class RentalService {
  baseUrl: string = `${environment.baseUrl}${environment.rentals}`;

  constructor(private http: HttpClient) {}

  getAllByVehicleId(
    page: number,
    size: number,
    id: number,
    search?: string,
  ): Observable<RentalPage> {
    return this.http.get<RentalPage>(
      `${this.baseUrl}/getAllByVehicleId/${id}`,
      { params: this.pageParams(page, size, search) },
    );
  }

  getById(id: number): Observable<DetailedRental> {
    return this.http.get<DetailedRental>(`${this.baseUrl}/${id}`);
  }

  getAll(page: number, size: number, search?: string): Observable<RentalPage> {
    return this.http.get<RentalPage>(`${this.baseUrl}`, {
      params: this.pageParams(page, size, search),
    });
  }

  private pageParams(page: number, size: number, search?: string): HttpParams {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sort', 'id,desc');
    if (search) {
      params = params.set('search', search);
    }
    return params;
  }
}
