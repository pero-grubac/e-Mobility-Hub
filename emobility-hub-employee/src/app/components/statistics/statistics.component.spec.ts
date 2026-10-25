import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { MatDatepicker } from '@angular/material/datepicker';
import { of } from 'rxjs';

import { StatisticsComponent } from './statistics.component';
import { StatisticService } from '../../services/statistic.service';

describe('StatisticsComponent', () => {
  let fixture: ComponentFixture<StatisticsComponent>;
  let component: StatisticsComponent;
  let service: jasmine.SpyObj<StatisticService>;

  beforeEach(async () => {
    service = jasmine.createSpyObj('StatisticService', [
      'getDailyRevenue',
      'getRevenueByVehicleType',
      'getFaultsPerVehicle',
    ]);
    service.getDailyRevenue.and.returnValue(of([]));
    service.getRevenueByVehicleType.and.returnValue(of([]));
    service.getFaultsPerVehicle.and.returnValue(of([]));

    await TestBed.configureTestingModule({
      declarations: [StatisticsComponent],
      providers: [{ provide: StatisticService, useValue: service }],
      schemas: [NO_ERRORS_SCHEMA],
    }).compileComponents();

    fixture = TestBed.createComponent(StatisticsComponent);
    component = fixture.componentInstance;
    component.currentDate = new Date(2026, 9, 25, 14, 30);
  });

  it('asks for the current month up to the first day of the next month', () => {
    fixture.detectChanges();

    expect(service.getDailyRevenue).toHaveBeenCalledWith(
      '2026-10-01T00:00:00',
      '2026-11-01T00:00:00',
    );
  });

  it('ends a chosen December at the first of January', () => {
    fixture.detectChanges();
    const picker = jasmine.createSpyObj<MatDatepicker<Date>>('MatDatepicker', ['close']);

    component.chooseMonth(new Date(2026, 11, 1), picker);

    expect(service.getDailyRevenue).toHaveBeenCalledWith(
      '2026-12-01T00:00:00',
      '2027-01-01T00:00:00',
    );
    expect(picker.close).toHaveBeenCalled();
  });

  it('charts a day without a revenue value as 0', () => {
    service.getDailyRevenue.and.returnValue(
      of([
        { rentalDay: '2026-10-01', totalRevenue: 75 },
        { rentalDay: '2026-10-25', totalRevenue: null },
      ]),
    );

    fixture.detectChanges();

    expect(component.dailyRevenueData).toEqual([
      { name: '2026-10-01', value: 75 },
      { name: '2026-10-25', value: 0 },
    ]);
  });
});
