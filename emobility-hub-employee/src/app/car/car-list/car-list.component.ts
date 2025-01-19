import { Component, OnInit } from '@angular/core';
import { Car } from '../../models/car.models';
import { CarService } from '../../services/car.service';
import { Router } from '@angular/router';

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

  constructor(private carService: CarService, private router: Router) {}

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
    console.log('Add button clicked');
  }

  handleUpload(): void {
    console.log('Upload button clicked');
  }

  handlePageChange(page: number): void {
    this.currentPage = page;
    this.loadCars();
  }
}
