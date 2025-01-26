import { Component, OnInit } from '@angular/core';
import { DetailedScooter } from '../../../models/scooter.model';
import { BaseManufacturer } from '../../../models/manufacturer.model';
import { ActivatedRoute, Router } from '@angular/router';
import { ManufacturerService } from '../../../services/manufacturer.service';
import { ScooterService } from '../../../services/scooter.service';

@Component({
  selector: 'app-scooter-detail',
  templateUrl: './scooter-detail.component.html',
  styleUrl: './scooter-detail.component.css',
})
export class ScooterDetailComponent implements OnInit {
  scooter!: DetailedScooter;
  manufacturers: BaseManufacturer[];
  selectedManufacturerId: number | null = null;
  selectedImageFile: File | null = null;
  selectedImagePreview: string | null = null;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private scooterService: ScooterService,
    private manufacturerService: ManufacturerService
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.loadScooterDetailes(id);
    this.loadManufacturers();
  }

  loadScooterDetailes(id: number): void {
    this.scooterService.getById(id).subscribe({
      next: (bicycle) => {
        this.scooter = bicycle;
        this.selectedManufacturerId = bicycle.manufacturer.id;
      },
      error: (err) => {
        console.error('Error fetching car details:', err);
      },
    });
  }

  loadManufacturers(): void {
    this.manufacturerService.getAll().subscribe({
      next: (manufacturers) => {
        this.manufacturers = manufacturers;
      },
      error: (err) => {
        console.error('Error fetching manufacturers:', err);
      },
    });
  }
  onManufacturerChange(): void {
    if (this.selectedManufacturerId) {
      const selectedManufacturer = this.manufacturers.find(
        (m) => m.id === this.selectedManufacturerId
      );
      if (selectedManufacturer) {
        this.scooter.manufacturer.id = selectedManufacturer.id;
      }
    }
  }

  onImageSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.selectedImageFile = input.files[0];

      const reader = new FileReader();
      reader.onload = (e: ProgressEvent<FileReader>) => {
        this.selectedImagePreview = e.target?.result as string;
      };
      reader.readAsDataURL(this.selectedImageFile);
    }
  }
  updateScooter(): void {
    const formData = new FormData();

    formData.append('id', this.scooter.id.toString());
    formData.append('uniqueIdentifier', this.scooter.uniqueIdentifier);
    formData.append('manufacturerId', this.selectedManufacturerId.toString());
    formData.append('model', this.scooter.model);
    formData.append('purchasePrice', this.scooter.purchasePrice.toString());
    formData.append('rentPrice', this.scooter.rentPrice.toString());
    formData.append('maxSpeed', this.scooter.maxSpeed.toString());

    if (this.selectedImageFile) {
      formData.append('image', this.selectedImageFile);
    }
    this.scooterService.updateScooter(formData).subscribe({
      next: () => {
        alert('Scooter updated successfully!');
        this.router.navigate(['/scooters']);
      },
      error: (err) => {
        console.error('Error updating scooter:', err);
      },
    });
  }

  deleteScooter(): void {
    this.scooterService.deleteScooter(this.scooter.id).subscribe({
      next: () => {
        alert('Scooter deleted successfully!');
        this.router.navigate(['/scooters']);
      },
      error: (err) => {
        console.error('Error deleting scooter:', err);
      },
    });
  }
}
