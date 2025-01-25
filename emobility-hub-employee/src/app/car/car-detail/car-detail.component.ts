import { Component, OnInit } from '@angular/core';
import { DetailedCar } from '../../models/car.models';
import { ActivatedRoute, Router } from '@angular/router';
import { CarService } from '../../services/car.service';
import { BaseManufacturer } from '../../models/manufacturer.model';
import { ManufacturerService } from '../../services/manufacturer.service';
import { UtilService } from '../../services/util.service';

@Component({
  selector: 'app-car-detail',
  templateUrl: './car-detail.component.html',
  styleUrl: './car-detail.component.css',
})
export class CarDetailComponent implements OnInit {
  car!: DetailedCar;
  manufacturers: BaseManufacturer[] = []; // Lista svih proizvođača
  selectedManufacturerId: number | null = null; // Selektovani proizvođač
  selectedImageFile: File | null = null;
  selectedImagePreview: string | null = null;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private carService: CarService,
    private manufacturerService: ManufacturerService,
    private utilService: UtilService
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.loadCarDetails(id);
    this.loadManufacturers();
  }

  loadCarDetails(id: number): void {
    this.carService.getById(id).subscribe({
      next: (car) => {
        this.car = car;
        this.selectedManufacturerId = car.manufacturer.id;
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
        this.car.manufacturer.id = selectedManufacturer.id; 
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

  updateCar(): void {
    const formData = new FormData();
    const formattedDate = this.utilService.formatDate(new Date(this.car.purchaseDate));

    formData.append('id', this.car.id.toString());
    formData.append('uniqueIdentifier', this.car.uniqueIdentifier);
    formData.append('manufacturerId', this.selectedManufacturerId.toString());
    formData.append('model', this.car.model);
    formData.append('purchasePrice', this.car.purchasePrice.toString());
    formData.append('rentPrice', this.car.rentPrice.toString());
    formData.append('purchaseDate', formattedDate);
    formData.append('description', this.car.description);

    if (this.selectedImageFile) {
      formData.append('image', this.selectedImageFile);
    }

    this.carService.updateCar(formData).subscribe({
      next: () => {
        alert('Car updated successfully!');
        this.router.navigate(['/cars']);
      },
      error: (err) => {
        console.error('Error updating car:', err);
      },
    });
  }

  deleteCar(): void {
    this.carService.deleteCar(this.car.id).subscribe({
      next: () => {
        alert('Car deleted successfully!');
        this.router.navigate(['/cars']);
      },
      error: (err) => {
        console.error('Error deleting car:', err);
      },
    });
  }


}
