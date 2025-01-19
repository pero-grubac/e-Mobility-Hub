import {
  Component,
  EventEmitter,
  Input,
  OnInit,
  Output,
  OnChanges,
} from '@angular/core';
import { Vehicle } from '../../models/vehicle.model';

@Component({
  selector: 'app-vehicle-list',
  templateUrl: './vehicle-list.component.html',
  styleUrls: ['./vehicle-list.component.css'],
})
export class VehicleListComponent implements OnInit, OnChanges {
  @Input() vehicles: Vehicle[] = [];
  @Input() entityType: string = '';

  @Input() totalPages: number = 0;
  @Input() currentPage: number = 0;
  @Input() onPageChange!: (page: number) => void;

  @Output() searchEvent = new EventEmitter<string>();
  @Output() addEvent = new EventEmitter<void>();
  @Output() uploadEvent = new EventEmitter<void>();

  displayedPages: (number | string)[] = [];

  ngOnInit(): void {
    this.updateDisplayedPages();
  }

  ngOnChanges(): void {
    this.updateDisplayedPages();
  }

  onSearch(term: string): void {
    this.searchEvent.emit(term);
  }

  onAdd(): void {
    this.addEvent.emit();
  }

  onUpload(): void {
    this.uploadEvent.emit();
  }

  private updateDisplayedPages(): void {
    const delta = 2;
    const range: (number | string)[] = [];
    const start = Math.max(0, this.currentPage - delta);
    const end = Math.min(this.totalPages - 1, this.currentPage + delta);

    if (start > 0) {
      range.push(0);
      if (start > 1) {
        range.push('...');
      }
    }

    for (let i = start; i <= end; i++) {
      range.push(i);
    }

    if (end < this.totalPages - 1) {
      if (end < this.totalPages - 2) {
        range.push('...');
      }
      range.push(this.totalPages - 1);
    }

    this.displayedPages = range;
  }

  goToPage(page: number): void {
    if (page >= 0 && page < this.totalPages) {
      this.currentPage = page;
      this.onPageChange(page);
      this.updateDisplayedPages();
    }
  }

  isNumber(page: number | string): boolean {
    return typeof page === 'number';
  }

  handlePageClick(page: number | string): void {
    if (this.isNumber(page)) {
      this.goToPage(page as number);
    }
  }
}
