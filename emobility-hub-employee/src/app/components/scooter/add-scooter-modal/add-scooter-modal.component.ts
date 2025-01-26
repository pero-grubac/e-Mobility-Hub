import { Component } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { v4 as uuidv4 } from 'uuid';
import { BaseManufacturer } from '../../../models/manufacturer.model';
import { ManufacturerService } from '../../../services/manufacturer.service';
import { ScooterService } from '../../../services/scooter.service';

@Component({
  selector: 'app-add-scooter-modal',
  templateUrl: './add-scooter-modal.component.html',
  styleUrl: './add-scooter-modal.component.css',
})
export class AddScooterModalComponent {
  scooter = {
    uniqueIdentifier: '',
    model: '',
    purchasePrice: null,
    rentPrice: null,
    maxSpeed: '',
    manufacturerId: null,
  };
  manufacturers: BaseManufacturer[] = [];
  selectedImageFile: File | null = null;

  constructor(
    private dialogRef: MatDialogRef<AddScooterModalComponent>,
    private scooterService: ScooterService,
    private manufacturerService: ManufacturerService
  ) {}

  ngOnInit(): void {
    this.loadManufacturers();
  }

  loadManufacturers(): void {
    this.manufacturerService.getAll().subscribe((manufacturers) => {
      this.manufacturers = manufacturers;
    });
  }

  onImageSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.selectedImageFile = input.files[0];
    }
  }

  onAdd(): void {
    const formData = new FormData();
    formData.append('model', this.scooter.model);
    formData.append('purchasePrice', this.scooter.purchasePrice.toString());
    formData.append('rentPrice', this.scooter.rentPrice.toString());
    formData.append('maxSpeed', this.scooter.maxSpeed);
    formData.append('manufacturerId', this.scooter.manufacturerId);
    formData.append('uniqueIdentifier', this.scooter.uniqueIdentifier);

    if (this.selectedImageFile) {
      formData.append('image', this.selectedImageFile);
    }

    this.scooterService.addScooter(formData).subscribe(() => {
      this.dialogRef.close(true);
    });
  }

  onCancel(): void {
    this.dialogRef.close();
  }
  isFormValid(): boolean {
    return (
      this.scooter.uniqueIdentifier.trim() !== '' &&
      this.scooter.model.trim() !== '' &&
      this.scooter.purchasePrice !== null &&
      this.scooter.rentPrice !== null &&
      this.scooter.maxSpeed !== null &&
      this.scooter.manufacturerId !== null
    );
  }
  generateUniqueId(): void {
    this.scooter.uniqueIdentifier = uuidv4();
  }
}
