import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Rental } from '../../../models/rental.model';

@Component({
  selector: 'app-rental-item',
  templateUrl: './rental-item.component.html',
  styleUrl: './rental-item.component.css',
})
export class RentalItemComponent implements OnInit {
  @Input() rental!: Rental;
  @Output() rentalClick = new EventEmitter<number>();

  ngOnInit(): void {}

  onRentalClick(): void {
    this.rentalClick.emit(this.rental.id); 
  }
}