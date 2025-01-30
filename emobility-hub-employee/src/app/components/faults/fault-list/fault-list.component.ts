import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Fault, FaultPage } from '../../../models/fault.models';
import { FaultService } from '../../../services/fault.service';

@Component({
  selector: 'app-fault-list',
  templateUrl: './fault-list.component.html',
  styleUrl: './fault-list.component.css',
})
export class FaultListComponent implements OnInit {
  faults: Fault[] = [];
  currentPage: number = 0;
  totalPages: number = 0;
  pageSize: number = 12;
  displayedPages: (number | string)[] = [];
  vehicleId!: number;

  constructor(
    private faultService: FaultService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.vehicleId = Number(this.route.snapshot.paramMap.get('id'));
    this.loadFaults();
  }

  loadFaults(): void {
    if (!this.vehicleId) {
      console.error('No vehicle ID found in URL.');
      return;
    }

    this.faultService
      .getAll(this.vehicleId, this.currentPage, this.pageSize)
      .subscribe({
        next: (data: FaultPage) => {
          this.faults = data.content;
          this.totalPages = data.page.totalPages;
          this.updateDisplayedPages();
        },
        error: (err) => {
          console.error('Error fetching faults:', err);
        },
      });
  }

  goToPage(page: number): void {
    if (page >= 0 && page < this.totalPages) {
      this.currentPage = page;
      this.loadFaults();
    }
  }

  handlePageClick(page: number | string): void {
    if (typeof page === 'number') this.goToPage(page);
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

  isNumber(page: number | string): boolean {
    return typeof page === 'number';
  }
}