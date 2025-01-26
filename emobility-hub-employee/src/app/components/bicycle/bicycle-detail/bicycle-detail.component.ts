import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { DetailedBicycle } from '../../../models/bicycle.models';
import { BaseManufacturer } from '../../../models/manufacturer.model';
import { BicycleService } from '../../../services/bicycle.service';
import { ManufacturerService } from '../../../services/manufacturer.service';
import { UtilService } from '../../../services/util.service';

@Component({
  selector: 'app-bicycle-detail',
  templateUrl: './bicycle-detail.component.html',
  styleUrl: './bicycle-detail.component.css',
})
export class BicycleDetailComponent implements OnInit {
  bicycle!: DetailedBicycle;
  manufacturers: BaseManufacturer[];
  selectedManufacturerId: number | null = null;
  selectedImageFile: File | null = null;
  selectedImagePreview: string | null = null;
  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private bicycleService: BicycleService,
    private manufacturerService: ManufacturerService,
    private utilService: UtilService
  ) {}
  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.loadBicycleDetailes(id);
    this.loadManufacturers();
  }

  loadBicycleDetailes(id: number): void {
    this.bicycleService.getById(id).subscribe({
      next: (bicycle) => {
        this.bicycle = bicycle;
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
        this.bicycle.manufacturer.id = selectedManufacturer.id;
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
  updateBicycle(): void {}
}
