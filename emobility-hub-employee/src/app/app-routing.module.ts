import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { BicycleDetailComponent } from './components/bicycle/bicycle-detail/bicycle-detail.component';
import { BicycleListComponent } from './components/bicycle/bicycle-list/bicycle-list.component';
import { CarDetailComponent } from './components/car/car-detail/car-detail.component';
import { CarListComponent } from './components/car/car-list/car-list.component';
import { ScooterListComponent } from './components/scooter/scooter-list/scooter-list.component';
import { ScooterDetailComponent } from './components/scooter/scooter-detail/scooter-detail.component';
import { ManufacturerListComponent } from './components/manufacturer/manufacturer-list/manufacturer-list.component';
import { ManufacturerDetailComponent } from './components/manufacturer/manufacturer-detail/manufacturer-detail.component';
import { ClientListComponent } from './components/client/client-list/client-list.component';
import { ClientDetailComponent } from './components/client/client-detail/client-detail.component';
import { EmployeeListComponent } from './components/employee/employee-list/employee-list.component';
import { EmployeeDetailComponent } from './components/employee/employee-detail/employee-detail.component';
import { FaultListComponent } from './components/faults/fault-list/fault-list.component';
import { RentalListComponent } from './components/rental/rental-list/rental-list.component';
import { RentalDetailComponent } from './components/rental/rental-detail/rental-detail.component';

const routes: Routes = [
  { path: 'cars', component: CarListComponent },
  { path: 'car/:id', component: CarDetailComponent },
  { path: 'bicycles', component: BicycleListComponent },
  { path: 'bicycle/:id', component: BicycleDetailComponent },
  { path: 'scooters', component: ScooterListComponent },
  { path: 'scooter/:id', component: ScooterDetailComponent },
  { path: 'manufacturers', component: ManufacturerListComponent },
  { path: 'manufacturer/:id', component: ManufacturerDetailComponent },
  { path: 'clients', component: ClientListComponent },
  { path: 'client/:id', component: ClientDetailComponent },
  { path: 'employees', component: EmployeeListComponent },
  { path: 'employee/:id', component: EmployeeDetailComponent },
  { path: 'faults/:id', component: FaultListComponent },
  { path: 'rentals/:id', component: RentalListComponent },
  { path: 'rental/:id', component: RentalDetailComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
