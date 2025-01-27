import { PageMetadata } from './pageMetadata.model';

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
