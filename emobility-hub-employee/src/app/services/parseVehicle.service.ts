import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../config/environment';

@Injectable({
  providedIn: 'root',
})
export class ParseVehicle {
  constructor(private http: HttpClient) {}

  uploadCsv(file: File): Observable<any> {
    const formData = new FormData();
    formData.append('csv', file);

    return this.http.post(
      `${environment.baseUrl}${environment.parseVehicle}`,
      formData
    );
  }
}
