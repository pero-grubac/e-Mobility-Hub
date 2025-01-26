import { Component } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { BaseManufacturer } from '../../../models/manufacturer.model';
import { BicycleService } from '../../../services/bicycle.service';
import { ManufacturerService } from '../../../services/manufacturer.service';
import { v4 as uuidv4 } from 'uuid';

@Component({
  selector: 'app-add-bicycle-modal',
  templateUrl: './add-bicycle-modal.component.html',
  styleUrl: './add-bicycle-modal.component.css',
})
export class AddBicycleModalComponent {
  bicycle = {
    uniqueIdentifier: '',
    model: '',
    purchasePrice: null,
    rentPrice: null,
    rangePerCharge: '',
    manufacturerId: null,
  };
  manufacturers: BaseManufacturer[] = [];
  selectedImageFile: File | null = null;

  constructor(
    private dialogRef: MatDialogRef<AddBicycleModalComponent>,
    private bicycleService: BicycleService,
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
    formData.append('model', this.bicycle.model);
    formData.append('purchasePrice', this.bicycle.purchasePrice.toString());
    formData.append('rentPrice', this.bicycle.rentPrice.toString());
    formData.append('rangePerCharge', this.bicycle.rangePerCharge);
    formData.append('manufacturerId', this.bicycle.manufacturerId);
    formData.append('uniqueIdentifier', this.bicycle.uniqueIdentifier);

    if (this.selectedImageFile) {
      formData.append('image', this.selectedImageFile);
    }

    this.bicycleService.addBicycle(formData).subscribe(() => {
      this.dialogRef.close(true);
    });
  }

  onCancel(): void {
    this.dialogRef.close();
  }
  isFormValid(): boolean {
    return (
      this.bicycle.uniqueIdentifier.trim() !== '' &&
      this.bicycle.model.trim() !== '' &&
      this.bicycle.purchasePrice !== null &&
      this.bicycle.rentPrice !== null &&
      this.bicycle.rangePerCharge.trim() !== '' &&
      this.bicycle.manufacturerId !== null
    );
  }
  generateUniqueId(): void {
    this.bicycle.uniqueIdentifier = uuidv4();
  }
}
