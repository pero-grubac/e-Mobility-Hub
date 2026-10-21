import { Client } from './client.models';
import { Location } from './location.model';
import { PageMetadata } from './pageMetadata.model';
import { Vehicle } from './vehicle.model';

export interface Rental {
  id: number;
  clientUsername: string;
  vehicleUniqueIdentifier: string;
  rentalStart: string;
  // null while the ride is still going
  rentalEnd: string | null;
  duration: number | null;
  distance: number | null;
  price: number | null;
  documentType: 'ID_CARD' | 'PASSPORT' | null;
  documentNumber: string | null;
  driverLicenseNumber: string | null;
  paymentCardLast4: string | null;
  startLocation: Location;
  endLocation: Location | null;
}

export interface RentalPage {
  content: Rental[];
  page: PageMetadata;
}

export interface DetailedRental extends Rental {
  client: Client;
  vehicle: Vehicle;
}
