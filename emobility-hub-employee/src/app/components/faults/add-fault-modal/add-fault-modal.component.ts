import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { FaultService } from '../../../services/fault.service';
import { FaultRequest } from '../../../models/fault.models';

@Component({
  selector: 'app-add-fault-modal',
  templateUrl: './add-fault-modal.component.html',
  styleUrl: './add-fault-modal.component.css',
})
export class AddFaultModalComponent {
  description: string = '';
  showError: boolean = false;

  constructor(
    public dialogRef: MatDialogRef<AddFaultModalComponent>,
    @Inject(MAT_DIALOG_DATA) public carId: number,
    private faultService: FaultService
  ) {}

  validateDescription(): void {
    this.showError = this.description.trim() === '';
  }

  onAdd(): void {
    if (this.description.trim() === '') {
      this.showError = true;
      return;
    }

    const faultRequest: FaultRequest = {
      id: 0,
      description: this.description,
      vehicleId: this.carId,
    };

    this.faultService.add(faultRequest).subscribe({
      next: () => {
        alert('Fault reported successfully!');
        this.dialogRef.close(true);
      },
      error: (err) => {
        console.error('Error reporting fault:', err);
      },
    });
  }

  onCancel(): void {
    this.dialogRef.close(false);
  }
}
