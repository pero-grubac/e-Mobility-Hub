import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormsModule } from '@angular/forms';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { of, throwError } from 'rxjs';

import { PricingComponent } from './pricing.component';
import { VehicleService } from '../../services/vehicle.service';
import { VehicleSummary } from '../../models/vehicle.model';

describe('PricingComponent', () => {
  let fixture: ComponentFixture<PricingComponent>;
  let component: PricingComponent;
  let service: jasmine.SpyObj<VehicleService>;

  const scooter: VehicleSummary = {
    id: 6,
    type: 'SCOOTER',
    uniqueIdentifier: 'SCO-001',
    model: 'Mi Scooter 3',
    manufacturer: 'Xiaomi',
    purchasePrice: 500,
    rentPrice: 3,
    broken: false,
    rented: false,
    latitude: 44.77,
    longitude: 17.2,
    image: '',
  };

  beforeEach(async () => {
    service = jasmine.createSpyObj('VehicleService', ['getAll', 'updateRentPrice']);
    service.getAll.and.returnValue(
      of({
        content: [{ ...scooter }],
        page: { size: 10, number: 0, totalElements: 1, totalPages: 1 },
      }),
    );

    await TestBed.configureTestingModule({
      declarations: [PricingComponent],
      imports: [FormsModule],
      providers: [{ provide: VehicleService, useValue: service }],
      schemas: [NO_ERRORS_SCHEMA],
    }).compileComponents();

    fixture = TestBed.createComponent(PricingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('lists every vehicle with its current price', () => {
    expect(service.getAll).toHaveBeenCalledWith(0, 10, '');
    expect(component.drafts[6]).toBe(3);
    expect(fixture.nativeElement.textContent).toContain('SCO-001');
  });

  it('searches from the first page', () => {
    component.currentPage = 2;
    component.onSearch('SCO');
    expect(service.getAll).toHaveBeenCalledWith(0, 10, 'SCO');
  });

  it('saves a changed price', () => {
    service.updateRentPrice.and.returnValue(of({ ...scooter, rentPrice: 4.5 }));
    const vehicle = component.vehicles[0];
    component.drafts[6] = 4.5;

    component.save(vehicle);

    expect(service.updateRentPrice).toHaveBeenCalledWith(6, 4.5);
    expect(vehicle.rentPrice).toBe(4.5);
    expect(component.saved.has(6)).toBeTrue();
  });

  it('refuses a missing, zero or negative price without calling the API', () => {
    const vehicle = component.vehicles[0];
    for (const bad of [null, 0, -2]) {
      component.drafts[6] = bad;
      component.save(vehicle);
      expect(component.errors[6]).toContain('Enter a price');
    }
    expect(service.updateRentPrice).not.toHaveBeenCalled();
  });

  it('shows an error when the API refuses', () => {
    service.updateRentPrice.and.returnValue(throwError(() => ({ status: 403 })));
    component.drafts[6] = 5;

    component.save(component.vehicles[0]);

    expect(component.errors[6]).toBe('The price could not be saved.');
  });
});
