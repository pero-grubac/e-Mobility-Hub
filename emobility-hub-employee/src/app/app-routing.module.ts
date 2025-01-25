import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CarListComponent } from './car/car-list/car-list.component';
import { CarDetailComponent } from './car/car-detail/car-detail.component';
import { BicycleListComponent } from './bicycle/bicycle-list/bicycle-list.component';
import { BicycleDetailComponent } from './bicycle/bicycle-detail/bicycle-detail.component';
const routes: Routes = [
  { path: 'cars', component: CarListComponent },
  { path: 'car/:id', component: CarDetailComponent },
  { path: 'bicycles', component: BicycleListComponent },
  { path: 'bicycle/:id', component: BicycleDetailComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
