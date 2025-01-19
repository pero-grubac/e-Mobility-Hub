import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Vehicle } from '../../../models/vehicle.model';

@Component({
  selector: 'app-vehicle-item',
  templateUrl: './vehicle-item.component.html',
  styleUrl: './vehicle-item.component.css',
})
export class VehicleItemComponent {
  @Input() vehicle!: Vehicle;
  @Output() vehicleClick = new EventEmitter<number>();

  onVehicleClick(): void {
    this.vehicleClick.emit(this.vehicle.id);
  }
}
