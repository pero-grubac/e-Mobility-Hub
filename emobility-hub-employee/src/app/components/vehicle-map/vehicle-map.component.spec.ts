import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { VehicleMapComponent } from './vehicle-map.component';
import { VehicleService } from '../../services/vehicle.service';
import { VehicleSummary } from '../../models/vehicle.model';

describe('VehicleMapComponent', () => {
  let fixture: ComponentFixture<VehicleMapComponent>;
  let component: VehicleMapComponent;

  const vehicle = (id: number, type: VehicleSummary['type'], broken = false, rented = false): VehicleSummary => ({
    id,
    type,
    uniqueIdentifier: `${type}-${id}`,
    model: 'M',
    manufacturer: 'X',
    purchasePrice: 1,
    rentPrice: 5,
    broken,
    rented,
    latitude: 44.77 + id / 1000,
    longitude: 17.19,
    image: '',
  });

  const markers = () =>
    fixture.nativeElement.querySelectorAll('path.leaflet-interactive').length;

  beforeEach(async () => {
    const service = jasmine.createSpyObj('VehicleService', ['getLocations']);
    service.getLocations.and.returnValue(
      of([vehicle(1, 'CAR'), vehicle(2, 'BICYCLE', true), vehicle(3, 'SCOOTER', false, true)]),
    );
    await TestBed.configureTestingModule({
      declarations: [VehicleMapComponent],
      imports: [RouterTestingModule],
      providers: [{ provide: VehicleService, useValue: service }],
    }).compileComponents();

    fixture = TestBed.createComponent(VehicleMapComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('puts every vehicle on the map', () => {
    expect(markers()).toBe(3);
    expect(fixture.nativeElement.textContent).toContain('3 of 3 vehicles');
  });

  it('derives the status, broken first', () => {
    expect(component.statusOf(vehicle(1, 'CAR', true, true))).toBe('broken');
    expect(component.statusOf(vehicle(1, 'CAR', false, true))).toBe('rented');
    expect(component.statusOf(vehicle(1, 'CAR'))).toBe('available');
  });

  it('filters by type and status', () => {
    component.toggleType('CAR');
    expect(markers()).toBe(2);

    component.toggleStatus('broken');
    expect(markers()).toBe(1);

    component.toggleType('CAR');
    expect(markers()).toBe(2);
  });
});
