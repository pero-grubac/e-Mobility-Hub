import { TestBed } from '@angular/core/testing';
import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';

import { environment } from '../config/environment';
import { AuthService } from './auth.service';
import { CarService } from './car.service';
import { ParseVehicle } from './parseVehicle.service';
import { RentalService } from './rental.service';
import { StatisticService } from './statistic.service';
import { FaultService } from './fault.service';
import { EmployeeService } from './employee.service';
import { VehicleService } from './vehicle.service';

// Contract tests: each service must call the URL and parameters the
// Spring Boot controllers expect.
describe('API services', () => {
  let httpMock: HttpTestingController;
  const api = environment.baseUrl;

  beforeEach(() => {
    TestBed.configureTestingModule({ imports: [HttpClientTestingModule] });
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => httpMock.verify());

  describe('AuthService', () => {
    it('posts credentials to /auth/login and returns the token', () => {
      let token: string | undefined;
      TestBed.inject(AuthService)
        .login({ username: 'admin', password: 'admin123' })
        .subscribe((res) => (token = res.token));

      const req = httpMock.expectOne(`${api}/auth/login`);
      expect(req.request.method).toBe('POST');
      expect(req.request.body).toEqual({
        username: 'admin',
        password: 'admin123',
      });
      req.flush({ token: 'jwt' });

      expect(token).toBe('jwt');
    });
  });

  describe('CarService', () => {
    let service: CarService;
    beforeEach(() => (service = TestBed.inject(CarService)));

    it('pages and sorts cars, newest first', () => {
      service.getCars(2, 10).subscribe();

      const req = httpMock.expectOne(
        (r) => r.url === `${api}/electric-cars/getByModel`,
      );
      expect(req.request.params.get('page')).toBe('2');
      expect(req.request.params.get('size')).toBe('10');
      expect(req.request.params.get('sort')).toBe('id,desc');
      expect(req.request.params.has('search')).toBeFalse();
      req.flush({});
    });

    it('adds the search term when given', () => {
      service.getCars(0, 5, 'Model').subscribe();

      const req = httpMock.expectOne(
        (r) => r.url === `${api}/electric-cars/getByModel`,
      );
      expect(req.request.params.get('search')).toBe('Model');
      req.flush({});
    });

    it('uses the right verbs for CRUD and fix', () => {
      const form = new FormData();
      service.addCar(form).subscribe();
      service.updateCar(form).subscribe();
      service.deleteCar(7).subscribe();
      service.fix(7).subscribe();
      service.getById(7).subscribe();

      httpMock
        .expectOne({ method: 'POST', url: `${api}/electric-cars` })
        .flush({});
      httpMock
        .expectOne({ method: 'PUT', url: `${api}/electric-cars` })
        .flush({});
      httpMock
        .expectOne({ method: 'DELETE', url: `${api}/electric-cars/7` })
        .flush(null);
      httpMock
        .expectOne({ method: 'PUT', url: `${api}/electric-cars/fix/7` })
        .flush({});
      httpMock
        .expectOne({ method: 'GET', url: `${api}/electric-cars/7` })
        .flush({});
    });
  });

  describe('ParseVehicle', () => {
    it('uploads the CSV as the "csv" multipart field', () => {
      const file = new File(['a,b'], 'cars.csv', { type: 'text/csv' });
      TestBed.inject(ParseVehicle).uploadCsv(file).subscribe();

      const req = httpMock.expectOne(`${api}/parse-vehicle`);
      expect(req.request.method).toBe('POST');
      const body = req.request.body as FormData;
      expect((body.get('csv') as File).name).toBe('cars.csv');
      req.flush({ errors: [], parsedVehicles: [] });
    });
  });

  describe('RentalService', () => {
    let service: RentalService;
    beforeEach(() => (service = TestBed.inject(RentalService)));

    it('searches all rentals by client or vehicle', () => {
      service.getAll(1, 10, 'client2').subscribe();

      const req = httpMock.expectOne((r) => r.url === `${api}/rentals`);
      expect(req.request.params.get('page')).toBe('1');
      expect(req.request.params.get('search')).toBe('client2');
      expect(req.request.params.get('sort')).toBe('id,desc');
      req.flush({});
    });

    it('lists the rentals of one vehicle without a search', () => {
      service.getAllByVehicleId(0, 5, 3).subscribe();

      const req = httpMock.expectOne(
        (r) => r.url === `${api}/rentals/getAllByVehicleId/3`,
      );
      expect(req.request.params.has('search')).toBeFalse();
      req.flush({});
    });
  });

  describe('FaultService', () => {
    it('searches the faults of a vehicle, newest first', () => {
      TestBed.inject(FaultService).getAll(7, 0, 5, 'brake').subscribe();

      const req = httpMock.expectOne(
        (r) => r.url === `${api}/faults/getAllByVehicleId/7`,
      );
      expect(req.request.params.get('search')).toBe('brake');
      expect(req.request.params.get('sort')).toBe('updateDateTime,desc');
      req.flush({});
    });
  });

  describe('EmployeeService', () => {
    it('sends the search as a query parameter', () => {
      TestBed.inject(EmployeeService).getEmployees(0, 10, 'mana').subscribe();

      const req = httpMock.expectOne(
        (r) => r.url === `${api}/users/getAllByUsername`,
      );
      expect(req.request.params.get('search')).toBe('mana');
      expect(req.request.body).toBeNull();
      req.flush({});
    });
  });

  describe('VehicleService', () => {
    let service: VehicleService;
    beforeEach(() => (service = TestBed.inject(VehicleService)));

    it('lists every vehicle type with search', () => {
      service.getAll(0, 10, 'SCO').subscribe();

      const req = httpMock.expectOne((r) => r.url === `${api}/vehicles`);
      expect(req.request.params.get('search')).toBe('SCO');
      req.flush({});
    });

    it('loads the positions for the map', () => {
      service.getLocations().subscribe();
      httpMock.expectOne(`${api}/vehicles/locations`).flush([]);
    });

    it('updates one rent price', () => {
      service.updateRentPrice(4, 7.5).subscribe();

      const req = httpMock.expectOne(`${api}/vehicles/4/rent-price`);
      expect(req.request.method).toBe('PUT');
      expect(req.request.body).toEqual({ rentPrice: 7.5 });
      req.flush({});
    });
  });

  describe('StatisticService', () => {
    it('passes the date range for daily revenue', () => {
      TestBed.inject(StatisticService)
        .getDailyRevenue('2024-03-01T00:00:00', '2024-03-31T23:59:59')
        .subscribe();

      const req = httpMock.expectOne(
        (r) => r.url === `${api}/statistics/getDailyRevenue`,
      );
      expect(req.request.params.get('start')).toBe('2024-03-01T00:00:00');
      expect(req.request.params.get('end')).toBe('2024-03-31T23:59:59');
      req.flush([]);
    });

    it('requests revenue by type and faults per vehicle', () => {
      const service = TestBed.inject(StatisticService);
      service.getRevenueByVehicleType().subscribe();
      service.getFaultsPerVehicle().subscribe();

      httpMock
        .expectOne(`${api}/statistics/getRevenueByVehicleType`)
        .flush([]);
      httpMock.expectOne(`${api}/statistics/getFaultsPerVehicle`).flush([]);
    });
  });
});
