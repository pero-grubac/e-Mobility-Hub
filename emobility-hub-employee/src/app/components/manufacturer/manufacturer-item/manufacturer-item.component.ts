import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Manufacturer } from '../../../models/manufacturer.model';

@Component({
  selector: 'app-manufacturer-item',
  templateUrl: './manufacturer-item.component.html',
  styleUrl: './manufacturer-item.component.css',
})
export class ManufacturerItemComponent {
  @Input() manufacturer!: Manufacturer;
  @Output() manufacturerClick = new EventEmitter<number>();

  onManufacturerClick(): void {
    this.manufacturerClick.emit(this.manufacturer.id);
  }
}
