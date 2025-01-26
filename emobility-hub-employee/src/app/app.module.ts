import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

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
import { VehicleItemComponent } from './components/vehicle/vehicle-list/vehicle-item/vehicle-item.component';
import { VehicleListComponent } from './components/vehicle/vehicle-list/vehicle-list.component';
import { ScooterListComponent } from './components/scooter/scooter-list/scooter-list.component';
import { ScooterDetailComponent } from './components/scooter/scooter-detail/scooter-detail.component';

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
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    MatToolbarModule,
  ],
  providers: [provideAnimationsAsync()],
  bootstrap: [AppComponent],
})
export class AppModule {}
