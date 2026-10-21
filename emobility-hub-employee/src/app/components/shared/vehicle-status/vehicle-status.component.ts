import { Component, Input } from '@angular/core';

// Broken wins over rented: a broken vehicle cannot be rented out.
@Component({
  selector: 'app-vehicle-status',
  template: `
    <span *ngIf="vehicle.broken" class="status status-broken">Broken</span>
    <span *ngIf="!vehicle.broken && vehicle.rented" class="status status-rented">Rented</span>
    <span *ngIf="!vehicle.broken && !vehicle.rented" class="status status-available">Available</span>
  `,
})
export class VehicleStatusComponent {
  @Input() vehicle!: { broken: boolean; rented: boolean };
}
