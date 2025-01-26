import { Component, OnInit } from '@angular/core';
import { Scooter } from '../../../models/scooter.model';
import { Router } from '@angular/router';
import { ScooterService } from '../../../services/scooter.service';

@Component({
  selector: 'app-scooter-list',
  templateUrl: './scooter-list.component.html',
  styleUrl: './scooter-list.component.css',
})
export class ScooterListComponent implements OnInit {
  scooters: Scooter[];
  currentPage: number = 0;
  totalPages: number = 0;
  pageSize: number = 12;
  searchTerm: string = '';

  constructor(private scooterService: ScooterService, private router: Router) {}
  ngOnInit(): void {
    this.loadScooters();
  }

  handleVehicleSelected(vehicleId: number): void {
    console.log(vehicleId);
    this.router.navigate(['/scooter', vehicleId]);
  }
  loadScooters(searchTerm: string = ''): void {
    this.scooterService
      .getScooters(this.currentPage, this.pageSize, searchTerm)
      .subscribe((data) => {
        this.scooters = data.content;
        this.totalPages = data.page.totalPages;
        this.currentPage = data.page.number;
      });
  }
  handleSearch(term: string): void {
    this.currentPage = 0;
    this.loadScooters(term);
  }

  handleAdd(): void {
    console.log('Add button clicked');
  }

  handlePageChange(page: number): void {
    this.currentPage = page;
    this.loadScooters();
  }
}
