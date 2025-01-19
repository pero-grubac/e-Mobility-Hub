import { Component, OnInit } from '@angular/core';
import { DetailedCar } from '../../models/car.models';
import { ActivatedRoute } from '@angular/router';
import { CarService } from '../../services/car.service';

@Component({
  selector: 'app-car-detail',
  templateUrl: './car-detail.component.html',
  styleUrl: './car-detail.component.css',
})
export class CarDetailComponent implements OnInit {
  car!: DetailedCar;
  additionalData: any;
  isEditMode: boolean = false;
  isLoading: boolean = true;

  constructor(private route: ActivatedRoute, private carService: CarService) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.carService.getById(id).subscribe({
      next: (car) => {
        console.log(car);
        this.car = car;
        this.additionalData = {
          'Purchase Date': car.purchaseDate,
          Description: car.description,
          Manufacturer: car.manufacturer.name,
          Address: car.manufacturer.address,
          'Contact Phone': car.manufacturer.contactPhone,
          'Contact Email': car.manufacturer.contactEmail,
        };
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error fetching car details:', err);
        this.isLoading = false;
      },
    });
  }

  handleSave(updatedVehicle: DetailedCar): void {
    console.log('Save vehicle:', updatedVehicle);
  }

  handleDelete(vehicleId: number): void {
    console.log('Delete vehicle with ID:', vehicleId);
  }

  handleImageUpload(file: File): void {
    console.log('Upload image:', file);
  }
}
