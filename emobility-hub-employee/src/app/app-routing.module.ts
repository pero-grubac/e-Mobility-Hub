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
import { StatisticsComponent } from './components/statistics/statistics.component';
import { LoginComponent } from './components/login/login.component';
import { authGuard } from './guards/auth.guard';
import { WelcomeComponent } from './components/welcome/welcome.component';

const routes: Routes = [
  {
    path: 'cars',
    component: CarListComponent,
    canActivate: [authGuard],
    data: { roles: ['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_OPERATOR'] },
  },
  {
    path: 'car/:id',
    component: CarDetailComponent,
    canActivate: [authGuard],
    data: { roles: ['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_OPERATOR'] },
  },
  {
    path: 'bicycles',
    component: BicycleListComponent,
    canActivate: [authGuard],
    data: { roles: ['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_OPERATOR'] },
  },
  {
    path: 'bicycle/:id',
    component: BicycleDetailComponent,
    canActivate: [authGuard],
    data: { roles: ['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_OPERATOR'] },
  },
  {
    path: 'scooters',
    component: ScooterListComponent,
    canActivate: [authGuard],
    data: { roles: ['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_OPERATOR'] },
  },
  {
    path: 'scooter/:id',
    component: ScooterDetailComponent,
    canActivate: [authGuard],
    data: { roles: ['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_OPERATOR'] },
  },
  {
    path: 'manufacturers',
    component: ManufacturerListComponent,
    canActivate: [authGuard],
    data: { roles: ['ROLE_ADMIN', 'ROLE_MANAGER'] },
  },
  {
    path: 'manufacturer/:id',
    component: ManufacturerDetailComponent,
    canActivate: [authGuard],
    data: { roles: ['ROLE_ADMIN', 'ROLE_MANAGER'] },
  },
  {
    path: 'clients',
    component: ClientListComponent,
    canActivate: [authGuard],
    data: { roles: ['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_OPERATOR'] },
  },
  {
    path: 'client/:id',
    component: ClientDetailComponent,
    canActivate: [authGuard],
    data: { roles: ['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_OPERATOR'] },
  },
  {
    path: 'employees',
    component: EmployeeListComponent,
    canActivate: [authGuard],
    data: { roles: ['ROLE_ADMIN', 'ROLE_MANAGER'] },
  },
  {
    path: 'employee/:id',
    component: EmployeeDetailComponent,
    canActivate: [authGuard],
    data: { roles: ['ROLE_ADMIN', 'ROLE_MANAGER'] },
  },
  {
    path: 'faults/:id',
    component: FaultListComponent,
    canActivate: [authGuard],
    data: { roles: ['ROLE_ADMIN', 'ROLE_MANAGER'] },
  },
  {
    path: 'rentals',
    component: RentalListComponent,
    canActivate: [authGuard],
    data: { roles: ['ROLE_OPERATOR', 'ROLE_MANAGER'] },
  },
  {
    path: 'rentals/:id',
    component: RentalListComponent,
    canActivate: [authGuard],
    data: { roles: ['ROLE_ADMIN', 'ROLE_MANAGER'] },
  },
  {
    path: 'rental/:id',
    component: RentalDetailComponent,
    canActivate: [authGuard],
    data: { roles: ['ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_OPERATOR'] },
  },
  {
    path: 'statistics',
    component: StatisticsComponent,
    canActivate: [authGuard],
  },
  { path: 'login', component: LoginComponent },
  {
    path: '',
    component: WelcomeComponent,
    canActivate: [authGuard],
    data: { roles: [] },
  },
  { path: '**', redirectTo: '/' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
