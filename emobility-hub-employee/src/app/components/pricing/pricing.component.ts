import { Component, OnInit } from '@angular/core';
import { VehicleSummary } from '../../models/vehicle.model';
import { VehicleService } from '../../services/vehicle.service';

// The manager's page for rental prices: every vehicle, price per hour.
@Component({
  selector: 'app-pricing',
  templateUrl: './pricing.component.html',
})
export class PricingComponent implements OnInit {
  readonly typeLabels: Record<string, string> = {
    CAR: 'Car',
    BICYCLE: 'Bicycle',
    SCOOTER: 'Scooter',
  };

  vehicles: VehicleSummary[] = [];
  // Edited prices by vehicle id, until saved.
  drafts: Record<number, number | null> = {};
  saved = new Set<number>();
  errors: Record<number, string> = {};
  currentPage = 0;
  totalPages = 0;
  pageSize = 10;
  searchTerm = '';

  constructor(private vehicleService: VehicleService) {}

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.vehicleService
      .getAll(this.currentPage, this.pageSize, this.searchTerm)
      .subscribe((page) => {
        this.vehicles = page.content;
        this.totalPages = page.page.totalPages;
        this.drafts = {};
        this.errors = {};
        this.saved.clear();
        for (const v of this.vehicles) {
          this.drafts[v.id] = v.rentPrice;
        }
      });
  }

  onSearch(term: string): void {
    this.searchTerm = term;
    this.currentPage = 0;
    this.load();
  }

  onPageChange(page: number): void {
    this.currentPage = page;
    this.load();
  }

  isValid(id: number): boolean {
    const price = this.drafts[id];
    return price !== null && price !== undefined && price > 0 && price <= 10000;
  }

  isChanged(vehicle: VehicleSummary): boolean {
    return this.drafts[vehicle.id] !== vehicle.rentPrice;
  }

  save(vehicle: VehicleSummary): void {
    if (!this.isValid(vehicle.id)) {
      this.errors[vehicle.id] = 'Enter a price between 0.01 and 10000.';
      return;
    }
    delete this.errors[vehicle.id];
    this.vehicleService
      .updateRentPrice(vehicle.id, this.drafts[vehicle.id]!)
      .subscribe({
        next: (updated) => {
          vehicle.rentPrice = updated.rentPrice;
          this.drafts[vehicle.id] = updated.rentPrice;
          this.saved.add(vehicle.id);
        },
        error: () => (this.errors[vehicle.id] = 'The price could not be saved.'),
      });
  }
}
