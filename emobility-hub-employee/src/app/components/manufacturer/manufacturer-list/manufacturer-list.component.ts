import {
  Component,
  EventEmitter,
  Input,
  OnChanges,
  OnInit,
  Output,
} from '@angular/core';
import {
  Manufacturer,
  ManufacturerPage,
} from '../../../models/manufacturer.model';
import { ManufacturerService } from '../../../services/manufacturer.service';

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

  displayedPages: (number | string)[] = [];

  constructor(private manufacturerService: ManufacturerService) {}

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
          this.updateDisplayedPages();
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
    console.log('Add button clicked');
  }

  onManufacturerSelected(manufacturerId: number): void {
    console.log('Selected manufacturer ID:', manufacturerId);
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

  goToPage(page: number): void {
    if (page >= 0 && page < this.totalPages) {
      this.currentPage = page;
      this.loadManufacturers();
    }
  }

  handlePageClick(page: number | string): void {
    if (typeof page === 'number') this.goToPage(page);
  }

  isNumber(page: number | string): boolean {
    return typeof page === 'number';
  }
}
