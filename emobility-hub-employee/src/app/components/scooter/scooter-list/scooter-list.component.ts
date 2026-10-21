import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { Scooter } from '../../../models/scooter.model';
import { VehicleColumn } from '../../../models/vehicle.model';
import { ScooterService } from '../../../services/scooter.service';
import { AddScooterModalComponent } from '../add-scooter-modal/add-scooter-modal.component';

@Component({
  selector: 'app-scooter-list',
  templateUrl: './scooter-list.component.html',
})
export class ScooterListComponent implements OnInit {
  readonly columns: VehicleColumn[] = [{ key: 'maxSpeed', label: 'Max speed (km/h)' }];
  scooters: Scooter[] = [];
  currentPage: number = 0;
  totalPages: number = 0;
  pageSize: number = 10;
  searchTerm: string = '';

  constructor(
    private scooterService: ScooterService,
    private router: Router,
    private dialog: MatDialog,
  ) {}

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.scooterService
      .getScooters(this.currentPage, this.pageSize, this.searchTerm)
      .subscribe((data) => {
        this.scooters = data.content;
        this.totalPages = data.page.totalPages;
        this.currentPage = data.page.number;
      });
  }

  handleVehicleSelected(vehicleId: number): void {
    this.router.navigate(['/scooter', vehicleId]);
  }

  handleSearch(term: string): void {
    this.searchTerm = term;
    this.currentPage = 0;
    this.load();
  }

  handlePageChange(page: number): void {
    this.currentPage = page;
    this.load();
  }

  handleAdd(): void {
    this.dialog
      .open(AddScooterModalComponent, { width: '600px' })
      .afterClosed()
      .subscribe((result) => {
        if (result) {
          this.load();
        }
      });
  }

  handleDelete(id: number): void {
    this.scooterService.deleteScooter(id).subscribe({
      next: () => this.load(),
      error: () => alert('The vehicle could not be deleted.'),
    });
  }
}
