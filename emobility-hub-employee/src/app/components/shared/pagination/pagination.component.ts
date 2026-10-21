import { Component, EventEmitter, Input, OnChanges, Output } from '@angular/core';

// Previous · 0 1 … 7 · Next, shared by the table pages.
@Component({
  selector: 'app-pagination',
  template: `
    <nav *ngIf="totalPages > 1" class="pagination">
      <button
        (click)="go(currentPage - 1)"
        [disabled]="currentPage === 0"
        type="button"
      >
        Previous
      </button>
      <button
        (click)="go(page)"
        *ngFor="let page of pages"
        [class.active]="page === currentPage"
        [disabled]="page === null"
        type="button"
      >
        {{ page === null ? '…' : page + 1 }}
      </button>
      <button
        (click)="go(currentPage + 1)"
        [disabled]="currentPage + 1 >= totalPages"
        type="button"
      >
        Next
      </button>
    </nav>
  `,
})
export class PaginationComponent implements OnChanges {
  @Input() currentPage = 0;
  @Input() totalPages = 0;
  @Output() pageChange = new EventEmitter<number>();

  // null marks a gap.
  pages: (number | null)[] = [];

  ngOnChanges(): void {
    const delta = 2;
    const start = Math.max(0, this.currentPage - delta);
    const end = Math.min(this.totalPages - 1, this.currentPage + delta);
    const pages: (number | null)[] = [];
    if (start > 0) {
      pages.push(0);
      if (start > 1) pages.push(null);
    }
    for (let i = start; i <= end; i++) pages.push(i);
    if (end < this.totalPages - 1) {
      if (end < this.totalPages - 2) pages.push(null);
      pages.push(this.totalPages - 1);
    }
    this.pages = pages;
  }

  go(page: number | null): void {
    if (page !== null && page >= 0 && page < this.totalPages && page !== this.currentPage) {
      this.pageChange.emit(page);
    }
  }
}
