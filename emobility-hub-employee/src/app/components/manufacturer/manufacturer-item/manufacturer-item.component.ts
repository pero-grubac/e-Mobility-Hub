import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Manufacturer } from '../../../models/manufacturer.model';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-manufacturer-item',
  templateUrl: './manufacturer-item.component.html',
  styleUrl: './manufacturer-item.component.css',
})
export class ManufacturerItemComponent implements OnInit {
  @Input() manufacturer!: Manufacturer;
  @Output() manufacturerClick = new EventEmitter<number>();

  flagUrl: string | null = null;
  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.fetchFlagUrl();
  }
  fetchFlagUrl(): void {
    const countryName = this.manufacturer.country;
    this.http
      .get<any[]>(`https://restcountries.com/v3.1/name/${countryName}`)
      .subscribe({
        next: (response) => {
          if (response && response.length > 0) {
            this.flagUrl = response[0].flags.svg; // URL za SVG zastavu
          } else {
            this.flagUrl = null; // U slučaju da API ne vrati rezultat
          }
        },
        error: (err) => {
          console.error(`Error fetching flag for ${countryName}:`, err);
          this.flagUrl = null; // Greška pri dohvaćanju
        },
      });
  }
  onManufacturerClick(): void {
    this.manufacturerClick.emit(this.manufacturer.id);
  }
}
