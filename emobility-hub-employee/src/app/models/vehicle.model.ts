import { PageMetadata } from './pageMetadata.model';

export interface Vehicle {
  id: number;
  uniqueIdentifier: string;
  model: string;
  manufacturerName: string;
  purchasePrice: number;
  image: string;
  broken: boolean;
  rented: boolean;
  rentPrice: number;
  latitude: number | null;
  longitude: number | null;
}

// A type-specific column in the fleet tables.
export interface VehicleColumn {
  key: string;
  label: string;
  format?: 'date' | 'number';
}

export type VehicleType = 'CAR' | 'BICYCLE' | 'SCOOTER';

// Any vehicle type, as returned by /vehicles (overview, map, pricing).
export interface VehicleSummary {
  id: number;
  type: VehicleType;
  uniqueIdentifier: string;
  model: string;
  manufacturer: string;
  purchasePrice: number;
  rentPrice: number;
  broken: boolean;
  rented: boolean;
  latitude: number | null;
  longitude: number | null;
  image: string;
}

export interface VehicleSummaryPage {
  content: VehicleSummary[];
  page: PageMetadata;
}

// Detail page route for each vehicle type.
export const VEHICLE_ROUTES: Record<VehicleType, string> = {
  CAR: '/car',
  BICYCLE: '/bicycle',
  SCOOTER: '/scooter',
};
