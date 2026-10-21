import { Component, EventEmitter, Input, OnChanges, Output } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Fault, FaultPage } from '../../../models/fault.models';
import { FaultService } from '../../../services/fault.service';
import { SessionService } from '../../../services/session.service';
import { AddFaultModalComponent } from '../add-fault-modal/add-fault-modal.component';
import { FaultDetailModalComponent } from '../fault-detail-modal/fault-detail-modal.component';

// All faults of one vehicle, shown on its detail page.
@Component({
  selector: 'app-fault-list',
  templateUrl: './fault-list.component.html',
})
export class FaultListComponent implements OnChanges {
  @Input() vehicleId!: number;
  // Adding or removing a fault changes the vehicle's broken flag.
  @Output() changed = new EventEmitter<void>();
  faults: Fault[] = [];
  currentPage: number = 0;
  totalPages: number = 0;
  pageSize: number = 5;
  searchTerm: string = '';

  constructor(
    private faultService: FaultService,
    private sessionService: SessionService,
    private dialog: MatDialog,
  ) {}

  ngOnChanges(): void {
    this.currentPage = 0;
    this.loadFaults();
  }

  loadFaults(): void {
    this.faultService
      .getAll(this.vehicleId, this.currentPage, this.pageSize, this.searchTerm)
      .subscribe({
        next: (data: FaultPage) => {
          this.faults = data.content;
          this.totalPages = data.page.totalPages;
        },
        error: (err) => console.error('Error fetching faults:', err),
      });
  }

  canManage(): boolean {
    return this.sessionService.hasRole(['ROLE_ADMIN', 'ROLE_MANAGER']);
  }

  onSearch(term: string): void {
    this.searchTerm = term;
    this.currentPage = 0;
    this.loadFaults();
  }

  onPageChange(page: number): void {
    this.currentPage = page;
    this.loadFaults();
  }

  addFault(): void {
    this.dialog
      .open(AddFaultModalComponent, { width: '400px', data: this.vehicleId })
      .afterClosed()
      .subscribe((added) => {
        if (added) {
          this.loadFaults();
          this.changed.emit();
        }
      });
  }

  openFaultDetails(fault: Fault): void {
    if (!this.canManage()) {
      return;
    }
    this.dialog
      .open(FaultDetailModalComponent, {
        width: '400px',
        data: { fault: { ...fault }, vehicleId: this.vehicleId },
      })
      .afterClosed()
      .subscribe((result) => {
        if (result) {
          this.loadFaults();
          this.changed.emit();
        }
      });
  }

  deleteFault(event: Event, fault: Fault): void {
    event.stopPropagation();
    if (!confirm('Delete this fault?')) {
      return;
    }
    this.faultService.delete(fault.id).subscribe({
      next: () => {
        this.loadFaults();
        this.changed.emit();
      },
      error: () => alert('The fault could not be deleted.'),
    });
  }
}
