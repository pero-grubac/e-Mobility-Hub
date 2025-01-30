import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../config/environment';
import { Observable } from 'rxjs';
import { FaultRequest } from '../models/fault.models';

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
}
