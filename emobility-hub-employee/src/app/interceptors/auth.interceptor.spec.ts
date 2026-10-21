import { TestBed } from '@angular/core/testing';
import { HTTP_INTERCEPTORS, HttpClient } from '@angular/common/http';
import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';
import { Router } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { AuthInterceptor } from './auth.interceptor';
import { SessionService } from '../services/session.service';
import { fakeSession } from '../../testing/session';

describe('AuthInterceptor', () => {
  let http: HttpClient;
  let httpMock: HttpTestingController;
  let session: SessionService;
  let navigate: jasmine.Spy;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule],
      providers: [
        { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true },
      ],
    });
    http = TestBed.inject(HttpClient);
    httpMock = TestBed.inject(HttpTestingController);
    session = TestBed.inject(SessionService);
    navigate = spyOn(TestBed.inject(Router), 'navigate');
    session.set(fakeSession('ROLE_ADMIN'));
  });

  afterEach(() => httpMock.verify());

  it('never puts a token in a header', () => {
    http.get('/electric-cars').subscribe();

    const req = httpMock.expectOne('/electric-cars');
    expect(req.request.headers.has('Authorization')).toBeFalse();
    req.flush({});
  });

  it('forgets the session and opens /login on a 401', () => {
    http.get('/electric-cars').subscribe({ error: () => {} });

    httpMock
      .expectOne('/electric-cars')
      .flush(null, { status: 401, statusText: 'Unauthorized' });

    expect(session.isActive()).toBeFalse();
    expect(navigate).toHaveBeenCalledWith(['/login']);
  });

  it('leaves a 403 to the page that made the call', () => {
    http.delete('/users/1').subscribe({ error: () => {} });

    httpMock
      .expectOne('/users/1')
      .flush(null, { status: 403, statusText: 'Forbidden' });

    expect(session.isActive()).toBeTrue();
    expect(navigate).not.toHaveBeenCalled();
  });

  it('does not redirect for a failed login', () => {
    http.post('/auth/login', {}).subscribe({ error: () => {} });

    httpMock
      .expectOne('/auth/login')
      .flush(null, { status: 401, statusText: 'Unauthorized' });

    expect(navigate).not.toHaveBeenCalled();
  });
});
