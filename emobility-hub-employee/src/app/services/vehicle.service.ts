import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../config/environment';
import { VehicleSummary, VehicleSummaryPage } from '../models/vehicle.model';

// Every vehicle type at once: the map and the pricing page.
@Injectable({
  providedIn: 'root',
})
export class VehicleService {
  baseUrl: string = `${environment.baseUrl}${environment.vehicles}`;

  constructor(private http: HttpClient) {}

  getAll(
    page: number,
    size: number,
    search?: string,
  ): Observable<VehicleSummaryPage> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sort', 'uniqueIdentifier,asc');
    if (search) {
      params = params.set('search', search);
    }
    return this.http.get<VehicleSummaryPage>(this.baseUrl, { params });
  }

  getLocations(): Observable<VehicleSummary[]> {
    return this.http.get<VehicleSummary[]>(`${this.baseUrl}/locations`);
  }

  updateRentPrice(id: number, rentPrice: number): Observable<VehicleSummary> {
    return this.http.put<VehicleSummary>(`${this.baseUrl}/${id}/rent-price`, {
      rentPrice,
    });
  }
}
