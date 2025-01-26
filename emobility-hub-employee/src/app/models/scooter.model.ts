import { Manufacturer } from './manufacturer.model';
import { PageMetadata } from './pageMetadata.model';
import { Vehicle, VehicleRequest } from './vehicle.model';

export interface Scooter extends Vehicle {
  maxSpeed: number;
}

export interface ScooterPage {
  content: Scooter[];
  page: PageMetadata;
}

export interface DetailedScooter extends Scooter {
  manufacturer: Manufacturer;
}

export interface ScooterRequest extends VehicleRequest {
  maxSpeed: number;
}
