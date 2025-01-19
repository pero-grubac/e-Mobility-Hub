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
