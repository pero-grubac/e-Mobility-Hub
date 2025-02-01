import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../config/environment';
import { Observable } from 'rxjs';
import {
  DailyRevenue,
  FaultsPerVehicle,
  RevenueByVehicleType,
} from '../models/statistic.models';

@Injectable({
  providedIn: 'root',
})
export class StatisticService {
  constructor(private http: HttpClient) {}

  baseUrl: string = `${environment.baseUrl}${environment.statistics}`;

  getDailyRevenue(start: string, end: string): Observable<DailyRevenue[]> {
    let params = new HttpParams()
      .set('start', start.toString())
      .set('end', end.toString());
    return this.http.get<DailyRevenue[]>(`${this.baseUrl}/getDailyRevenue`, {
      params,
    });
  }
  getRevenueByVehicleType(): Observable<RevenueByVehicleType[]> {
    return this.http.get<RevenueByVehicleType[]>(
      `${this.baseUrl}/getRevenueByVehicleType`
    );
  }
  getFaultsPerVehicle(): Observable<FaultsPerVehicle[]> {
    return this.http.get<FaultsPerVehicle[]>(
      `${this.baseUrl}/getFaultsPerVehicle`
    );
  }
}
