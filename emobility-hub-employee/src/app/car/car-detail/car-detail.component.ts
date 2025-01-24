import { Component, OnInit } from '@angular/core';
import { DetailedCar } from '../../models/car.models';
import { ActivatedRoute, Router } from '@angular/router';
import { CarService } from '../../services/car.service';

@Component({
  selector: 'app-car-detail',
  templateUrl: './car-detail.component.html',
  styleUrl: './car-detail.component.css',
})
export class CarDetailComponent implements OnInit {
  car!: DetailedCar;
  selectedImageFile: File | null = null;
  selectedImagePreview: string | null = null;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private carService: CarService
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.carService.getById(id).subscribe({
      next: (car) => {
        this.car = car;
      },
      error: (err) => {
        console.error('Error fetching car details:', err);
      },
    });
  }

  onImageSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.selectedImageFile = input.files[0];

      // Prikaz slike
      const reader = new FileReader();
      reader.onload = (e: ProgressEvent<FileReader>) => {
        this.selectedImagePreview = e.target?.result as string;
      };
      reader.readAsDataURL(this.selectedImageFile);
    }
  }

  updateCar(): void {
    const formData = new FormData();

    const formattedDate = this.formatDate(new Date(this.car.purchaseDate));

    formData.append('id', this.car.id.toString());
    formData.append('uniqueIdentifier', this.car.uniqueIdentifier);
    formData.append('manufacturerId', this.car.manufacturer.id.toString());
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
  private formatDate(date: Date): string {
    const year = date.getFullYear();
    const month = (date.getMonth() + 1).toString().padStart(2, '0'); // Meseci su 0-indeksirani
    const day = date.getDate().toString().padStart(2, '0');
    const hours = date.getHours().toString().padStart(2, '0');
    const minutes = date.getMinutes().toString().padStart(2, '0');
    const seconds = date.getSeconds().toString().padStart(2, '0');

    return `${year}-${month}-${day}T${hours}:${minutes}:${seconds}`;
  }
}
