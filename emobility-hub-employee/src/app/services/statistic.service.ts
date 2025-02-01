import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { environment } from "../config/environment";

@Injectable({
  providedIn: 'root',
})
export class StatisticService {
  constructor(private http: HttpClient) {}

  baseUrl: string = `${environment.baseUrl}${environment.statistics}`;
}
