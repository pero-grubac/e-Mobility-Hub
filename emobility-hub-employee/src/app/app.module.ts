import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HeaderComponent } from './header/header.component';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { MatToolbarModule } from '@angular/material/toolbar';
import { VehicleListComponent } from './vehicle/vehicle-list/vehicle-list.component';
import { VehicleDetailComponent } from './vehicle/vehicle-detail/vehicle-detail.component';
import { VehicleItemComponent } from './vehicle/vehicle-list/vehicle-item/vehicle-item.component';
import { CarListComponent } from './car/car-list/car-list.component';
import { CarDetailComponent } from './car/car-detail/car-detail.component';
import { HttpClientModule } from '@angular/common/http';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    VehicleListComponent,
    VehicleDetailComponent,
    VehicleItemComponent,
    CarListComponent,
    CarDetailComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    MatToolbarModule,
    HttpClientModule,
  ],
  providers: [provideAnimationsAsync()],
  bootstrap: [AppComponent],
})
export class AppModule {}
