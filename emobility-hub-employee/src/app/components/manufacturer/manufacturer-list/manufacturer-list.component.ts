import { Component, OnInit } from '@angular/core';
import {
  Manufacturer,
  ManufacturerPage,
} from '../../../models/manufacturer.model';
import { ManufacturerService } from '../../../services/manufacturer.service';
import { MatDialog } from '@angular/material/dialog';
import { AddManufacturerModalComponent } from '../add-manufacturer-modal/add-manufacturer-modal.component';
import { Router } from '@angular/router';

@Component({
  selector: 'app-manufacturer-list',
  templateUrl: './manufacturer-list.component.html',
  styleUrl: './manufacturer-list.component.css',
})
export class ManufacturerListComponent implements OnInit {
  manufacturers: Manufacturer[] = [];
  currentPage: number = 0;
  totalPages: number = 0;
  pageSize: number = 12;
  searchTerm: string = '';

  constructor(
    private manufacturerService: ManufacturerService,
    private dialog: MatDialog,
    private router: Router,
  ) {}

  ngOnInit(): void {
    this.loadManufacturers();
  }

  loadManufacturers(): void {
    this.manufacturerService
      .getManufacturers(this.currentPage, this.pageSize, this.searchTerm)
      .subscribe({
        next: (data: ManufacturerPage) => {
          this.manufacturers = data.content;
          this.totalPages = data.page.totalPages;
        },
        error: (err) => {
          console.error('Error fetching manufacturers:', err);
        },
      });
  }

  onSearch(term: string): void {
    this.searchTerm = term;
    this.currentPage = 0;
    this.loadManufacturers();
  }

  onAdd(): void {
    const dialogRef = this.dialog.open(AddManufacturerModalComponent, {
      width: '500px',
    });
    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.loadManufacturers();
      }
    });
  }

  onManufacturerSelected(manufacturerId: number): void {
    this.router.navigate(['/manufacturer', manufacturerId]);
  }

  onPageChange(page: number): void {
    this.currentPage = page;
    this.loadManufacturers();
  }
}
