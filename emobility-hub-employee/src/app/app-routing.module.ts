import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { BicycleDetailComponent } from './components/bicycle/bicycle-detail/bicycle-detail.component';
import { BicycleListComponent } from './components/bicycle/bicycle-list/bicycle-list.component';
import { CarDetailComponent } from './components/car/car-detail/car-detail.component';
import { CarListComponent } from './components/car/car-list/car-list.component';
import { ScooterListComponent } from './components/scooter/scooter-list/scooter-list.component';
import { ScooterDetailComponent } from './components/scooter/scooter-detail/scooter-detail.component';
import { ManufacturerListComponent } from './components/manufacturer/manufacturer-list/manufacturer-list.component';

const routes: Routes = [
  { path: 'cars', component: CarListComponent },
  { path: 'car/:id', component: CarDetailComponent },
  { path: 'bicycles', component: BicycleListComponent },
  { path: 'bicycle/:id', component: BicycleDetailComponent },
  { path: 'scooters', component: ScooterListComponent },
  { path: 'scooter/:id', component: ScooterDetailComponent },
  { path: 'manufacturers', component: ManufacturerListComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
