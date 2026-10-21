import { Component, Input, OnChanges, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Rental, RentalPage } from '../../../models/rental.model';
import { RentalService } from '../../../services/rental.service';

// All rentals (the Rentals page) or the rentals of one vehicle (embedded on
// its detail page).
@Component({
  selector: 'app-rental-list',
  templateUrl: './rental-list.component.html',
})
export class RentalListComponent implements OnInit, OnChanges {
  @Input() vehicleId?: number;
  rentals: Rental[] = [];
  currentPage: number = 0;
  totalPages: number = 0;
  pageSize: number = 10;
  searchTerm: string = '';

  constructor(
    private rentalService: RentalService,
    private router: Router,
  ) {}

  ngOnInit(): void {
    if (this.vehicleId === undefined) {
      this.loadRentals();
    }
  }

  ngOnChanges(): void {
    if (this.vehicleId !== undefined) {
      this.pageSize = 5;
      this.currentPage = 0;
      this.loadRentals();
    }
  }

  get embedded(): boolean {
    return this.vehicleId !== undefined;
  }

  loadRentals(): void {
    const request = this.embedded
      ? this.rentalService.getAllByVehicleId(this.currentPage, this.pageSize, this.vehicleId!, this.searchTerm)
      : this.rentalService.getAll(this.currentPage, this.pageSize, this.searchTerm);
    request.subscribe({
      next: (data: RentalPage) => {
        this.rentals = data.content;
        this.totalPages = data.page.totalPages;
      },
      error: (err) => console.error('Error fetching rentals:', err),
    });
  }

  onSearch(term: string): void {
    this.searchTerm = term;
    this.currentPage = 0;
    this.loadRentals();
  }

  onPageChange(page: number): void {
    this.currentPage = page;
    this.loadRentals();
  }

  onRentalSelected(rentalId: number): void {
    this.router.navigate(['/rental', rentalId]);
  }
}
