import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Fault, FaultRequest } from '../../../models/fault.models';
import { FaultService } from '../../../services/fault.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-fault-detail-modal',
  templateUrl: './fault-detail-modal.component.html',
  styleUrl: './fault-detail-modal.component.css',
})
export class FaultDetailModalComponent {
  fault: Fault;
  vehicleId: number;
  constructor(
    public dialogRef: MatDialogRef<FaultDetailModalComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { fault: Fault; vehicleId: number },
    private faultService: FaultService,
    private route: ActivatedRoute
  ) {
    this.fault = data.fault;
    this.vehicleId = data.vehicleId;
  }

  onUpdate(): void {
    const update: FaultRequest = {
      id: this.fault.id,
      description: this.fault.description,
      vehicleId: this.vehicleId,
    };
    this.faultService.update(update).subscribe({
      next: () => {
        alert('Fault updated successfully!');
        this.dialogRef.close(true);
      },
      error: (err) => {
        console.error('Error updating fault:', err);
      },
    });
  }

  onDelete(): void {
    const confirmed = confirm('Are you sure you want to delete this fault?');
    if (confirmed) {
      this.faultService.delete(this.fault.id).subscribe({
        next: () => {
          alert('Fault deleted successfully!');
          this.dialogRef.close(true);
        },
        error: (err) => {
          console.error('Error deleting fault:', err);
        },
      });
    }
  }

  onCancel(): void {
    this.dialogRef.close(false);
  }
}
