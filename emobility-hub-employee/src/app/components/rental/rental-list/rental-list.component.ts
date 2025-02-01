import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Rental, RentalPage } from '../../../models/rental.model';
import { RentalService } from '../../../services/rental.service';

@Component({
  selector: 'app-rental-list',
  templateUrl: './rental-list.component.html',
  styleUrl: './rental-list.component.css',
})
export class RentalListComponent implements OnInit {
  rentals: Rental[] = [];
  currentPage: number = 0;
  totalPages: number = 0;
  pageSize: number = 12;
  displayedPages: (number | string)[] = [];
  vehicleId!: number;

  constructor(
    private rentalService: RentalService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.vehicleId = Number(this.route.snapshot.paramMap.get('id'));
    if (this.vehicleId) {
      this.loadRentals();
    } else {
      console.error('Vehicle ID not found in URL.');
    }
  }

  loadRentals(): void {
    this.rentalService
      .getAllByVehicleId(this.currentPage, this.pageSize, this.vehicleId)
      .subscribe({
        next: (data: RentalPage) => {
          this.rentals = data.content;
          this.totalPages = data.page.totalPages;
          this.updateDisplayedPages();
        },
        error: (err) => {
          console.error('Error fetching rentals:', err);
        },
      });
  }

  goToPage(page: number): void {
    if (page >= 0 && page < this.totalPages) {
      this.currentPage = page;
      this.loadRentals();
    }
  }

  handlePageClick(page: number | string): void {
    if (typeof page === 'number') this.goToPage(page);
  }

  private updateDisplayedPages(): void {
    const delta = 2;
    const range: (number | string)[] = [];
    const start = Math.max(0, this.currentPage - delta);
    const end = Math.min(this.totalPages - 1, this.currentPage + delta);

    if (start > 0) {
      range.push(0);
      if (start > 1) range.push('...');
    }

    for (let i = start; i <= end; i++) range.push(i);

    if (end < this.totalPages - 1) {
      if (end < this.totalPages - 2) range.push('...');
      range.push(this.totalPages - 1);
    }

    this.displayedPages = range;
  }

  isNumber(page: number | string): boolean {
    return typeof page === 'number';
  }

  onRentalSelected(rentalId: number): void {
    this.router.navigate([`/rental/${rentalId}`]);
  }
}
