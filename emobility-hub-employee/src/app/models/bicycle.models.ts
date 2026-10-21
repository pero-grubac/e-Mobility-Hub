import { Manufacturer } from './manufacturer.model';
import { PageMetadata } from './pageMetadata.model';
import { Vehicle } from './vehicle.model';

export interface Bicycle extends Vehicle {
  rangePerCharge: number;
}

export interface BicyclePage {
  content: Bicycle[];
  page: PageMetadata;
}

export interface DetailedBicycle extends Bicycle {
  manufacturer: Manufacturer;
}
