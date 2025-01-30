import { PageMetadata } from './pageMetadata.model';

export interface FaultRequest {
  description: string;
  vehicleId: number;
}

export interface Fault {
  id: number;
  description: string;
  creationDateTime: string;
  updateDateTime: string;
}

export interface FaultPage {
  content: Fault[];
  page: PageMetadata;
}
