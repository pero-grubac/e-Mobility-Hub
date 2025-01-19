import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CarListComponent } from './car/car-list/car-list.component';
import { CarDetailComponent } from './car/car-detail/car-detail.component';
const routes: Routes = [
  { path: 'cars', component: CarListComponent },
  { path: 'car/:id', component: CarDetailComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
