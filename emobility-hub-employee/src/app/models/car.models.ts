import { Manufacturer } from './manufacturer.model';
import { PageMetadata } from './pageMetadata.model';
import { Vehicle, VehicleRequest } from './vehicle.model';

export interface Car extends Vehicle {
  purchaseDate: string;
  description: string;
}
export interface CarPage {
  content: Car[];
  page: PageMetadata;
}

export interface DetailedCar extends Car {
  manufacturer: Manufacturer;
}

export interface CarRequest extends VehicleRequest {
  purchaseDate: string;
  description: string;
}
