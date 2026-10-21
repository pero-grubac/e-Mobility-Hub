import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { Car } from '../../../models/car.models';
import { VehicleColumn } from '../../../models/vehicle.model';
import { CarService } from '../../../services/car.service';
import { AddCarModalComponent } from '../add-car-modal/add-car-modal.component';

@Component({
  selector: 'app-car-list',
  templateUrl: './car-list.component.html',
})
export class CarListComponent implements OnInit {
  readonly columns: VehicleColumn[] = [
    { key: 'purchaseDate', label: 'Purchase date', format: 'date' },
    { key: 'description', label: 'Description' },
  ];
  cars: Car[] = [];
  currentPage: number = 0;
  totalPages: number = 0;
  pageSize: number = 10;
  searchTerm: string = '';

  constructor(
    private carService: CarService,
    private router: Router,
    private dialog: MatDialog,
  ) {}

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.carService
      .getCars(this.currentPage, this.pageSize, this.searchTerm)
      .subscribe((data) => {
        this.cars = data.content;
        this.totalPages = data.page.totalPages;
        this.currentPage = data.page.number;
      });
  }

  handleVehicleSelected(vehicleId: number): void {
    this.router.navigate(['/car', vehicleId]);
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
      .open(AddCarModalComponent, { width: '600px' })
      .afterClosed()
      .subscribe((result) => {
        if (result) {
          this.load();
        }
      });
  }

  handleDelete(id: number): void {
    this.carService.deleteCar(id).subscribe({
      next: () => this.load(),
      error: () => alert('The vehicle could not be deleted.'),
    });
  }
}
