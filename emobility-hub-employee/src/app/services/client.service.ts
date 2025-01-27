import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../config/environment';
import { Client, ClientPage } from '../models/client.models';
import { Observable } from 'rxjs/internal/Observable';

@Injectable({
  providedIn: 'root',
})
export class ClientService {
  constructor(private http: HttpClient) {}
  baseUrl: string = `${environment.baseUrl}${environment.clients}`;

  getClients(
    page: number,
    size: number,
    search?: string
  ): Observable<ClientPage> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sort', 'id,desc');
    if (search) {
      params = params.set('search', search);
    }
    return this.http.get<ClientPage>(`${this.baseUrl}/getAllByUsername`, {
      params,
    });
  }
  getById(id: number): Observable<Client> {
    return this.http.get<Client>(`${this.baseUrl}/${id}`);
  }
  block(id: number, isBlocked: boolean): Observable<any> {
    const body = { isBlocked };
    return this.http.put<any>(`${this.baseUrl}/block/${id}`, isBlocked, {
      headers: { 'Content-Type': 'application/json' },
    });
  }
}
