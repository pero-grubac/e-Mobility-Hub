import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Vehicle } from '../../models/vehicle.model';

@Component({
  selector: 'app-vehicle-detail',
  templateUrl: './vehicle-detail.component.html',
  styleUrl: './vehicle-detail.component.css',
})
export class VehicleDetailComponent implements OnInit {
  @Input() vehicle!: Vehicle;
  @Input() additionalData: any;
  @Input() isEditMode: boolean = false;

  @Output() save = new EventEmitter<Vehicle>();
  @Output() delete = new EventEmitter<number>();
  @Output() imageUpload = new EventEmitter<File>();

  objectKeys = Object.keys;

  selectedImageFile: File | null = null;

  ngOnInit(): void {}

  onImageSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.selectedImageFile = input.files[0];
    }
  }

  uploadImage(): void {
    if (this.selectedImageFile) {
      this.imageUpload.emit(this.selectedImageFile);
    }
  }

  saveDetails(): void {
    this.save.emit(this.vehicle);
  }

  editDetails(): void {
    this.isEditMode = true;
  }

  deleteVehicle(): void {
    this.delete.emit(this.vehicle.id);
  }
}
