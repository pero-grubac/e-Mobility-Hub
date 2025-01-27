import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../config/environment';
import {
  BaseManufacturer,
  DetailedManufacturer,
  Manufacturer,
  ManufacturerPage,
  ManufacturerRequest,
} from '../models/manufacturer.model';

@Injectable({
  providedIn: 'root',
})
export class ManufacturerService {
  constructor(private http: HttpClient) {}

  baseUrl: string = `${environment.baseUrl}${environment.manufacturers}`;

  getAll(): Observable<BaseManufacturer[]> {
    return this.http.get<BaseManufacturer[]>(`${this.baseUrl}/getAll`);
  }
  getById(id: number): Observable<Manufacturer> {
    return this.http.get<Manufacturer>(`${this.baseUrl}/${id}`);
  }
  getManufacturers(
    page: number,
    size: number,
    search?: string
  ): Observable<ManufacturerPage> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sort', 'id,desc');
    if (search) {
      params = params.set('search', search);
    }
    return this.http.get<ManufacturerPage>(`${this.baseUrl}/getAllByName`, {
      params,
    });
  }
  addManufacturer(manufacturer: ManufacturerRequest): Observable<any> {
    return this.http.post<any>(`${this.baseUrl}`, manufacturer);
  }
  getManufacturerById(id: number): Observable<DetailedManufacturer> {
    return this.http.get<DetailedManufacturer>(`${this.baseUrl}/${id}`);
  }

  updateManufacturer(manufacturer: Manufacturer) {
    return this.http.put(`${this.baseUrl}`, manufacturer);
  }

  deleteManufacturer(id: number) {
    return this.http.delete(`${this.baseUrl}/${id}`);
  }
}
