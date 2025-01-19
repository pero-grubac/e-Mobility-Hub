import { Vehicle } from './vehicle.model';

export interface Car extends Vehicle {
  purchaseDate: string;
  description: string;
}
