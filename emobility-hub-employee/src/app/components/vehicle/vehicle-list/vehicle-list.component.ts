import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Vehicle, VehicleColumn } from '../../../models/vehicle.model';
import { ParseVehicle } from '../../../services/parseVehicle.service';
import { SessionService } from '../../../services/session.service';

interface ParseError {
  rowNumber: number;
  message: string;
}

// The fleet table for one vehicle type, with search, create, delete and CSV
// import. The parent page owns loading and the type-specific columns.
@Component({
  selector: 'app-vehicle-list',
  templateUrl: './vehicle-list.component.html',
  styleUrls: ['./vehicle-list.component.css'],
})
export class VehicleListComponent {
  @Input() vehicles: Vehicle[] = [];
  @Input() columns: VehicleColumn[] = [];
  @Input() totalPages: number = 0;
  @Input() currentPage: number = 0;
  @Output() searchEvent = new EventEmitter<string>();
  @Output() addEvent = new EventEmitter<void>();
  @Output() deleteEvent = new EventEmitter<number>();
  @Output() pageChange = new EventEmitter<number>();
  @Output() uploaded = new EventEmitter<void>();
  @Output() vehicleSelected = new EventEmitter<number>();
  isModalOpen: boolean = false;
  selectedFile: File | null = null;
  uploadStatus: string | null = null;
  uploadErrors: ParseError[] = [];
  isUploading: boolean = false;

  constructor(
    private parseVehicle: ParseVehicle,
    private sessionService: SessionService,
  ) {}

  valueOf(vehicle: Vehicle, key: string): unknown {
    return (vehicle as unknown as Record<string, unknown>)[key];
  }

  onVehicleSelected(vehicleId: number): void {
    this.vehicleSelected.emit(vehicleId);
  }

  onDelete(event: Event, vehicleId: number): void {
    event.stopPropagation();
    if (confirm('Delete this vehicle?')) {
      this.deleteEvent.emit(vehicleId);
    }
  }

  openModal(): void {
    this.isModalOpen = true;
  }

  closeModal(): void {
    this.isModalOpen = false;
    this.selectedFile = null;
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      const file = input.files[0];

      if (!file.name.toLowerCase().endsWith('.csv')) {
        alert('Please upload a valid CSV file.');
        return;
      }
      if (file.size > 5 * 1024 * 1024) {
        alert('File size exceeds the 5MB limit.');
        return;
      }
      this.selectedFile = file;
    }
  }

  uploadFile(): void {
    if (this.selectedFile) {
      this.isUploading = true;
      this.uploadStatus = 'Uploading...';
      this.uploadErrors = [];
      this.parseVehicle.uploadCsv(this.selectedFile).subscribe({
        next: (result) => {
          this.isUploading = false;
          const imported = result.parsedVehicles?.length ?? 0;
          this.uploadErrors = result.errors ?? [];
          this.uploadStatus = `Imported ${imported} vehicle(s), ${this.uploadErrors.length} row(s) rejected.`;
          this.closeModal();
          this.uploaded.emit();
        },
        error: (error) => {
          console.error('Upload failed:', error);
          this.isUploading = false;
          this.uploadStatus = 'Upload failed. Please try again.';
        },
      });
    }
  }

  hasRole(requiredRoles: string[]): boolean {
    return this.sessionService.hasRole(requiredRoles);
  }

  onSearch(term: string): void {
    this.searchEvent.emit(term);
  }

  onAdd(): void {
    this.addEvent.emit();
  }
}
