import { Client } from './client.models';
import { Location } from './location.model';
import { PageMetadata } from './pageMetadata.model';
import { Vehicle } from './vehicle.model';

export interface Rental {
  id: number;
  rentalStart: string;
  rentalEnd: string;
  duration: number;
  distance: number;
  price: number;
  startLocation: Location;
  endLocation: Location;
}

export interface RentalPage {
  content: Rental[];
  page: PageMetadata;
}

export interface DetiledRental extends Rental {
  client: Client;
  vehicle: Vehicle;
}
