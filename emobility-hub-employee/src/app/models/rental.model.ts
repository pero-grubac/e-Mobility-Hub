import { Location } from './location.model';
import { PageMetadata } from './pageMetadata.model';

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
