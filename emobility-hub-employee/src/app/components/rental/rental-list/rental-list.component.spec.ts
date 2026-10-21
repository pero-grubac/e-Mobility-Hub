import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { RentalListComponent } from './rental-list.component';
import { RentalService } from '../../../services/rental.service';

describe('RentalListComponent', () => {
  let fixture: ComponentFixture<RentalListComponent>;
  let component: RentalListComponent;
  let rentals: jasmine.SpyObj<RentalService>;

  const page = (totalPages: number) =>
    of({
      content: [
        {
          id: 9,
          clientUsername: 'client1',
          vehicleUniqueIdentifier: 'SCO-001',
          rentalStart: '2026-10-21T10:00:00',
          rentalEnd: null,
          duration: null,
          distance: null,
          price: null,
        },
      ],
      page: { size: 10, number: 0, totalElements: 1, totalPages },
    } as never);

  beforeEach(async () => {
    rentals = jasmine.createSpyObj('RentalService', ['getAll', 'getAllByVehicleId']);
    rentals.getAll.and.returnValue(page(3));
    rentals.getAllByVehicleId.and.returnValue(page(1));

    await TestBed.configureTestingModule({
      declarations: [RentalListComponent],
      imports: [RouterTestingModule],
      providers: [{ provide: RentalService, useValue: rentals }],
      schemas: [NO_ERRORS_SCHEMA],
    }).compileComponents();
    fixture = TestBed.createComponent(RentalListComponent);
    component = fixture.componentInstance;
  });

  it('on the Rentals page pages and searches all rentals', () => {
    fixture.detectChanges();
    expect(rentals.getAll).toHaveBeenCalledWith(0, 10, '');

    component.onPageChange(2);
    expect(rentals.getAll).toHaveBeenCalledWith(2, 10, '');
    expect(rentals.getAllByVehicleId).not.toHaveBeenCalled();

    component.onSearch('client1');
    expect(rentals.getAll).toHaveBeenCalledWith(0, 10, 'client1');
  });

  it('on a detail page shows only that vehicle', () => {
    component.vehicleId = 6;
    component.ngOnChanges();
    fixture.detectChanges();

    expect(rentals.getAllByVehicleId).toHaveBeenCalledWith(0, 5, 6, '');
    expect(rentals.getAll).not.toHaveBeenCalled();
    expect(fixture.nativeElement.textContent).not.toContain('SCO-001');
  });

  it('marks a ride that is still going', () => {
    fixture.detectChanges();
    expect(fixture.nativeElement.textContent).toContain('In progress');
  });
});
