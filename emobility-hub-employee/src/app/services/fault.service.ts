import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../config/environment';
import { Observable } from 'rxjs';
import { FaultPage, FaultRequest } from '../models/fault.models';

@Injectable({
  providedIn: 'root',
})
export class FaultService {
  constructor(private http: HttpClient) {}

  baseUrl: string = `${environment.baseUrl}${environment.faults}`;
  add(data: FaultRequest): Observable<any> {
    console.log(data);
    return this.http.post<any>(`${this.baseUrl}`, data);
  }
  getAll(id: number, page: number, size: number): Observable<FaultPage> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sort', 'updateDateTime,desc');
    return this.http.get<FaultPage>(`${this.baseUrl}/getAllByVehicleId/${id}`, {
      params,
    });
  }
}
