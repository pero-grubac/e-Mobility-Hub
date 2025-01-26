import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Car } from '../../../models/car.models';
import { CarService } from '../../../services/car.service';
import { AddCarModalComponent } from '../add-car-modal/add-car-modal.component';
import { MatDialog } from '@angular/material/dialog';

@Component({
  selector: 'app-car-list',
  templateUrl: './car-list.component.html',
  styleUrl: './car-list.component.css',
})
export class CarListComponent implements OnInit {
  cars: Car[] = [];
  currentPage: number = 0;
  totalPages: number = 0;
  pageSize: number = 12;
  searchTerm: string = '';

  constructor(
    private carService: CarService,
    private router: Router,
    private dialog: MatDialog
  ) {}

  ngOnInit(): void {
    this.loadCars();
  }
  handleVehicleSelected(vehicleId: number): void {
    this.router.navigate(['/car', vehicleId]);
  }
  loadCars(searchTerm: string = ''): void {
    this.carService
      .getCars(this.currentPage, this.pageSize, searchTerm)
      .subscribe((data) => {
        this.cars = data.content;
        this.totalPages = data.page.totalPages;
        this.currentPage = data.page.number;
      });
  }

  handleSearch(term: string): void {
    this.currentPage = 0;
    this.loadCars(term);
  }

  handleAdd(): void {
    const dialogRef = this.dialog.open(AddCarModalComponent, {
      width: '600px',
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.loadCars();
      }
    });
  }

  handlePageChange(page: number): void {
    this.currentPage = page;
    this.loadCars();
  }
}
