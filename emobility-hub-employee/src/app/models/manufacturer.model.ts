import { PageMetadata } from './pageMetadata.model';
import { Vehicle } from './vehicle.model';

export interface Manufacturer {
  id: number;
  name: string;
  address: string;
  contactPhone: string;
  contactEmail: string;
  contactFax: string;
  country: string;
}
export interface BaseManufacturer {
  id: number;
  name: string;
}

export interface ManufacturerPage {
  content: Manufacturer[];
  page: PageMetadata;
}

export interface ManufacturerRequest {
  name: string;
  address: string;
  contactPhone: string;
  contactEmail: string;
  contactFax: string;
  country: string;
}

export interface DetailedManufacturer {
  id: number;
  name: string;
  address: string;
  contactPhone: string;
  contactEmail: string;
  contactFax: string;
  country: string;
  vehicles: Vehicle[];
}
