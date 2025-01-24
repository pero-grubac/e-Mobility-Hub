import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../config/environment';
import { BaseManufacturer, Manufacturer } from '../models/manufacturer.model';

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
}
