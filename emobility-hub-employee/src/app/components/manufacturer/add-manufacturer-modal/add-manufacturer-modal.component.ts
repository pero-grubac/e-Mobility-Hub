import { Component } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { ManufacturerService } from '../../../services/manufacturer.service';

@Component({
  selector: 'app-add-manufacturer-modal',
  templateUrl: './add-manufacturer-modal.component.html',
  styleUrl: './add-manufacturer-modal.component.css',
})
export class AddManufacturerModalComponent {
  manufacturer = {
    name: '',
    address: '',
    contactPhone: '',
    contactEmail: '',
    contactFax: '',
    country: '',
  };

  constructor(
    private dialogRef: MatDialogRef<AddManufacturerModalComponent>,
    private manufacturerService: ManufacturerService
  ) {}

  onCreate(): void {
    this.manufacturerService.addManufacturer(this.manufacturer).subscribe({
      next: () => {
        this.dialogRef.close(true);
      },
      error: (err) => {
        console.error('Error adding manufacturer:', err);
      },
    });
  }

  onCancel(): void {
    this.dialogRef.close();
  }
}
