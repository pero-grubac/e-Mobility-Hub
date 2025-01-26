import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Bicycle } from '../../../models/bicycle.models';
import { BicycleService } from '../../../services/bicycle.service';
import { AddBicycleModalComponent } from '../add-bicycle-modal/add-bicycle-modal.component';
import { MatDialog } from '@angular/material/dialog';

@Component({
  selector: 'app-bicycle-list',
  templateUrl: './bicycle-list.component.html',
  styleUrl: './bicycle-list.component.css',
})
export class BicycleListComponent implements OnInit {
  bicycles: Bicycle[];
  currentPage: number = 0;
  totalPages: number = 0;
  pageSize: number = 12;
  searchTerm: string = '';

  constructor(
    private bicycleService: BicycleService,
    private router: Router,
    private dialog: MatDialog
  ) {}

  ngOnInit(): void {
    this.loadBicycles();
  }
  handleVehicleSelected(vehicleId: number): void {
    console.log(vehicleId);
    this.router.navigate(['/bicycle', vehicleId]);
  }
  loadBicycles(searchTerm: string = ''): void {
    this.bicycleService
      .getBicycles(this.currentPage, this.pageSize, searchTerm)
      .subscribe((data) => {
        this.bicycles = data.content;
        this.totalPages = data.page.totalPages;
        this.currentPage = data.page.number;
      });
  }
  handleSearch(term: string): void {
    this.currentPage = 0;
    this.loadBicycles(term);
  }

  handleAdd(): void {
    const dialogRef = this.dialog.open(AddBicycleModalComponent, {
      width: '600px',
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.loadBicycles();
      }
    });
  }

  handlePageChange(page: number): void {
    this.currentPage = page;
    this.loadBicycles();
  }
}
