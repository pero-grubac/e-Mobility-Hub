import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { MatDialogModule } from '@angular/material/dialog';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { MatToolbarModule } from '@angular/material/toolbar';
import { HeaderComponent } from './components/header/header.component';
import { BicycleDetailComponent } from './components/bicycle/bicycle-detail/bicycle-detail.component';
import { BicycleListComponent } from './components/bicycle/bicycle-list/bicycle-list.component';
import { CarDetailComponent } from './components/car/car-detail/car-detail.component';
import { CarListComponent } from './components/car/car-list/car-list.component';
import { VehicleListComponent } from './components/vehicle/vehicle-list/vehicle-list.component';
import { ScooterListComponent } from './components/scooter/scooter-list/scooter-list.component';
import { ScooterDetailComponent } from './components/scooter/scooter-detail/scooter-detail.component';
import { AddCarModalComponent } from './components/car/add-car-modal/add-car-modal.component';
import { AddBicycleModalComponent } from './components/bicycle/add-bicycle-modal/add-bicycle-modal.component';
import { AddScooterModalComponent } from './components/scooter/add-scooter-modal/add-scooter-modal.component';
import { VehicleItemComponent } from './components/vehicle/vehicle-item/vehicle-item.component';
import { ManufacturerItemComponent } from './components/manufacturer/manufacturer-item/manufacturer-item.component';
import { ManufacturerListComponent } from './components/manufacturer/manufacturer-list/manufacturer-list.component';
import { ManufacturerDetailComponent } from './components/manufacturer/manufacturer-detail/manufacturer-detail.component';
import { AddManufacturerModalComponent } from './components/manufacturer/add-manufacturer-modal/add-manufacturer-modal.component';
import { ScrollingModule } from '@angular/cdk/scrolling';
import { ClientItemComponent } from './components/client/client-item/client-item.component';
import { ClientListComponent } from './components/client/client-list/client-list.component';
import { EmployeeItemComponent } from './components/employee/employee-item/employee-item.component';
import { EmployeeListComponent } from './components/employee/employee-list/employee-list.component';
import { EmployeeDetailComponent } from './components/employee/employee-detail/employee-detail.component';
import { ClientDetailComponent } from './components/client/client-detail/client-detail.component';
import { AddEmployeeModalComponent } from './components/employee/add-employee-modal/add-employee-modal.component';
import { AddFaultModalComponent } from './components/faults/add-fault-modal/add-fault-modal.component';
import { FaultItemComponent } from './components/faults/fault-item/fault-item.component';
import { FaultListComponent } from './components/faults/fault-list/fault-list.component';
import { FaultDetailModalComponent } from './components/faults/fault-detail-modal/fault-detail-modal.component';
import { RentalItemComponent } from './components/rental/rental-item/rental-item.component';
import { RentalListComponent } from './components/rental/rental-list/rental-list.component';
import { RentalDetailComponent } from './components/rental/rental-detail/rental-detail.component';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    VehicleListComponent,
    VehicleItemComponent,
    CarListComponent,
    CarDetailComponent,
    BicycleListComponent,
    BicycleDetailComponent,
    ScooterListComponent,
    ScooterDetailComponent,
    AddCarModalComponent,
    AddBicycleModalComponent,
    AddScooterModalComponent,
    ManufacturerItemComponent,
    ManufacturerListComponent,
    ManufacturerDetailComponent,
    AddManufacturerModalComponent,
    ClientItemComponent,
    ClientListComponent,
    EmployeeItemComponent,
    EmployeeListComponent,
    EmployeeDetailComponent,
    ClientDetailComponent,
    AddEmployeeModalComponent,
    AddFaultModalComponent,
    FaultItemComponent,
    FaultListComponent,
    FaultDetailModalComponent,
    RentalItemComponent,
    RentalListComponent,
    RentalDetailComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    BrowserAnimationsModule,
    FormsModule,
    MatToolbarModule,
    MatDialogModule,
    ScrollingModule,
  ],
  providers: [provideAnimationsAsync()],
  bootstrap: [AppComponent],
})
export class AppModule {}
