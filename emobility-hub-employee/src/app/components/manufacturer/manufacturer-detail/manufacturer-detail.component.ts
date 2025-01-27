import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { DetailedManufacturer } from '../../../models/manufacturer.model';
import { ManufacturerService } from '../../../services/manufacturer.service';

@Component({
  selector: 'app-manufacturer-detail',
  templateUrl: './manufacturer-detail.component.html',
  styleUrl: './manufacturer-detail.component.css',
})
export class ManufacturerDetailComponent implements OnInit {
  manufacturer!: DetailedManufacturer;

  constructor(
    private route: ActivatedRoute,
    private manufacturerService: ManufacturerService
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.loadManufacturerDetails(Number(id));
    }
  }

  loadManufacturerDetails(id: number): void {
    this.manufacturerService.getManufacturerById(id).subscribe({
      next: (data) => (this.manufacturer = data),
      error: (err) => console.error('Error loading manufacturer:', err),
    });
  }

  onUpdate(): void {
    this.manufacturerService.updateManufacturer(this.manufacturer).subscribe({
      next: () => {
        alert('Manufacturer updated successfully!');
      },
      error: (err) => {
        console.error('Error updating manufacturer:', err);
      },
    });
  }

  onDelete(): void {
    const confirmed = confirm(
      `Are you sure you want to delete ${this.manufacturer.name}?`
    );
    if (confirmed) {
      this.manufacturerService
        .deleteManufacturer(this.manufacturer.id)
        .subscribe({
          next: () => {
            alert('Manufacturer deleted successfully!');
          },
          error: (err) => {
            console.error('Error deleting manufacturer:', err);
          },
        });
    }
  }

  onVehicleSelected(vehicleId: number): void {
    console.log('Selected vehicle ID:', vehicleId);
  }
}
