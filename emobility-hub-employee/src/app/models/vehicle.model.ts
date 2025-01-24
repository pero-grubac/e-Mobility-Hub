export interface Vehicle {
  id: number;
  uniqueIdentifier: string;
  model: string;
  purchasePrice: number;
  image: string;
  isBroken: boolean;
  isRented: boolean;
  rentPrice: number;
}

export interface VehicleRequest {
  id: number;
  uniqueIdentifier: string;
  model: string;
  purchasePrice: number;
  image: File;
  manufacturerId:string;
  isBroken: boolean;
  isRented: boolean;
  rentPrice: number;
}