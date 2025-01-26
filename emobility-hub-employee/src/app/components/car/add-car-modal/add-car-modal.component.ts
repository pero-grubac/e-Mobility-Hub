import { Component } from '@angular/core';
import { BaseManufacturer } from '../../../models/manufacturer.model';
import { CarService } from '../../../services/car.service';
import { ManufacturerService } from '../../../services/manufacturer.service';
import { MatDialogRef } from '@angular/material/dialog';
import { v4 as uuidv4 } from 'uuid';

@Component({
  selector: 'app-add-car-modal',
  templateUrl: './add-car-modal.component.html',
  styleUrl: './add-car-modal.component.css',
})
export class AddCarModalComponent {
  car = {
    uniqueIdentifier: '',
    model: '',
    purchasePrice: null,
    rentPrice: null,
    purchaseDate: '',
    description: '',
    manufacturerId: null,
  };
  manufacturers: BaseManufacturer[] = [];
  selectedImageFile: File | null = null;

  constructor(
    private dialogRef: MatDialogRef<AddCarModalComponent>,
    private carService: CarService,
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
    formData.append('model', this.car.model);
    formData.append('purchasePrice', this.car.purchasePrice.toString());
    formData.append('rentPrice', this.car.rentPrice.toString());
    formData.append('purchaseDate', this.car.purchaseDate);
    formData.append('description', this.car.description);
    formData.append('manufacturerId', this.car.manufacturerId);
    formData.append('uniqueIdentifier', this.car.uniqueIdentifier);

    if (this.selectedImageFile) {
      formData.append('image', this.selectedImageFile);
    }

    this.carService.addCar(formData).subscribe(() => {
      this.dialogRef.close(true);
    });
  }

  onCancel(): void {
    this.dialogRef.close();
  }
  isFormValid(): boolean {
    return (
      this.car.model.trim() !== '' &&
      this.car.purchasePrice !== null &&
      this.car.rentPrice !== null &&
      this.car.purchaseDate.trim() !== '' &&
      this.car.description.trim() !== '' &&
      this.car.manufacturerId !== null
    );
  }
  generateUniqueId(): void {
    this.car.uniqueIdentifier = uuidv4();
  }
}
