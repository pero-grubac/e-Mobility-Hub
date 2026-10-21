import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { Bicycle } from '../../../models/bicycle.models';
import { VehicleColumn } from '../../../models/vehicle.model';
import { BicycleService } from '../../../services/bicycle.service';
import { AddBicycleModalComponent } from '../add-bicycle-modal/add-bicycle-modal.component';

@Component({
  selector: 'app-bicycle-list',
  templateUrl: './bicycle-list.component.html',
})
export class BicycleListComponent implements OnInit {
  readonly columns: VehicleColumn[] = [{ key: 'rangePerCharge', label: 'Range (km)' }];
  bicycles: Bicycle[] = [];
  currentPage: number = 0;
  totalPages: number = 0;
  pageSize: number = 10;
  searchTerm: string = '';

  constructor(
    private bicycleService: BicycleService,
    private router: Router,
    private dialog: MatDialog,
  ) {}

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.bicycleService
      .getBicycles(this.currentPage, this.pageSize, this.searchTerm)
      .subscribe((data) => {
        this.bicycles = data.content;
        this.totalPages = data.page.totalPages;
        this.currentPage = data.page.number;
      });
  }

  handleVehicleSelected(vehicleId: number): void {
    this.router.navigate(['/bicycle', vehicleId]);
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
      .open(AddBicycleModalComponent, { width: '600px' })
      .afterClosed()
      .subscribe((result) => {
        if (result) {
          this.load();
        }
      });
  }

  handleDelete(id: number): void {
    this.bicycleService.deleteBicycle(id).subscribe({
      next: () => this.load(),
      error: () => alert('The vehicle could not be deleted.'),
    });
  }
}
