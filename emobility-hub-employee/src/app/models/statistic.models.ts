export interface DailyRevenue {
  totalRevenue: number;
  rentalDay: string;
}

export interface RevenueByVehicleType {
  totalRevenue: number;
  vehicleType: String;
}

export interface FaultsPerVehicle {
  image: string;
  model: string;
  vehicleId: string;
  uniqueIdentifier: string;
  faultCount: number;
}
