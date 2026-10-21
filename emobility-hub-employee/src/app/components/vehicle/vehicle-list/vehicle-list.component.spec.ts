import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { of } from 'rxjs';

import { VehicleListComponent } from './vehicle-list.component';
import { VehicleStatusComponent } from '../../shared/vehicle-status/vehicle-status.component';
import { SessionService } from '../../../services/session.service';
import { ParseVehicle } from '../../../services/parseVehicle.service';
import { Vehicle } from '../../../models/vehicle.model';
import { fakeSession } from '../../../../testing/session';

describe('VehicleListComponent', () => {
  let fixture: ComponentFixture<VehicleListComponent>;
  let component: VehicleListComponent;

  const car = (id: number, extra: Partial<Vehicle> = {}) =>
    ({
      id,
      uniqueIdentifier: `CAR-00${id}`,
      model: 'Model S',
      manufacturerName: 'Tesla',
      purchasePrice: 80000,
      rentPrice: 50,
      image: '',
      broken: false,
      rented: false,
      latitude: null,
      longitude: null,
      description: 'Long range',
      ...extra,
    }) as Vehicle;

  const render = (role: string, vehicles: Vehicle[]) => {
    TestBed.inject(SessionService).set(fakeSession(role));
    fixture = TestBed.createComponent(VehicleListComponent);
    component = fixture.componentInstance;
    component.columns = [{ key: 'description', label: 'Description' }];
    component.vehicles = vehicles;
    fixture.detectChanges();
    return fixture.nativeElement as HTMLElement;
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [VehicleListComponent, VehicleStatusComponent],
      imports: [HttpClientTestingModule],
      schemas: [NO_ERRORS_SCHEMA],
    }).compileComponents();
  });

  it('shows every field of a vehicle in a table row', () => {
    const page = render('ROLE_ADMIN', [car(1)]);
    const headers = Array.from(page.querySelectorAll('th')).map((th) => th.textContent!.trim());
    const cells = Array.from(page.querySelectorAll('tbody tr td')).map((td) => td.textContent!.trim());

    expect(headers).toContain('Description');
    expect(headers).toContain('Status');
    expect(cells).toContain('CAR-001');
    expect(cells).toContain('Tesla');
    expect(cells).toContain('Long range');
    expect(cells).toContain('Available');
  });

  it('shows the status of broken and rented vehicles', () => {
    const page = render('ROLE_OPERATOR', [car(1, { broken: true }), car(2, { rented: true })]);

    expect(page.textContent).toContain('Broken');
    expect(page.textContent).toContain('Rented');
  });

  it('lets admins delete after confirming, not operators', () => {
    let page = render('ROLE_OPERATOR', [car(1)]);
    expect(page.querySelector('.btn-outline-danger')).toBeNull();
    expect(page.textContent).not.toContain('Upload CSV');

    page = render('ROLE_ADMIN', [car(1)]);
    const deleted: number[] = [];
    component.deleteEvent.subscribe((id) => deleted.push(id));
    spyOn(window, 'confirm').and.returnValue(true);
    (page.querySelector('.btn-outline-danger') as HTMLButtonElement).click();

    expect(deleted).toEqual([1]);
  });

  it('does not open the detail page when delete is clicked', () => {
    const page = render('ROLE_ADMIN', [car(1)]);
    const opened: number[] = [];
    component.vehicleSelected.subscribe((id) => opened.push(id));
    spyOn(window, 'confirm').and.returnValue(false);

    (page.querySelector('.btn-outline-danger') as HTMLButtonElement).click();
    (page.querySelector('tbody tr') as HTMLElement).click();

    expect(opened).toEqual([1]);
  });

  it('reports the CSV import result and asks the page to reload', () => {
    render('ROLE_ADMIN', []);
    spyOn(TestBed.inject(ParseVehicle), 'uploadCsv').and.returnValue(
      of({ parsedVehicles: [{}, {}], errors: [{ rowNumber: 3, message: 'Model is required.' }] }),
    );
    let reloaded = false;
    component.uploaded.subscribe(() => (reloaded = true));
    component.selectedFile = new File(['x'], 'cars.csv');

    component.uploadFile();
    fixture.detectChanges();

    expect(reloaded).toBeTrue();
    expect(component.uploadStatus).toBe('Imported 2 vehicle(s), 1 row(s) rejected.');
    expect(fixture.nativeElement.textContent).toContain('Row 3: Model is required.');
  });
});
