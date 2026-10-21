import { TestBed } from '@angular/core/testing';
import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';
import { SessionService } from './session.service';
import { Session } from '../models/auth.models';
import { fakeSession } from '../../testing/session';

describe('SessionService', () => {
  let service: SessionService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({ imports: [HttpClientTestingModule] });
    service = TestBed.inject(SessionService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => httpMock.verify());

  it('has no role before anyone signs in', () => {
    expect(service.isActive()).toBeFalse();
    expect(service.getRole()).toBeNull();
    expect(service.hasRole(['ROLE_ADMIN'])).toBeFalse();
  });

  it('keeps only the session fields, never the token', () => {
    service.set({ ...fakeSession('ROLE_ADMIN'), token: 'secret' } as Session);

    expect(JSON.stringify(service)).not.toContain('secret');
    expect(service.getRole()).toBe('ROLE_ADMIN');
  });

  it('checks the role against a list', () => {
    service.set(fakeSession('ROLE_OPERATOR'));

    expect(service.hasRole(['ROLE_OPERATOR', 'ROLE_MANAGER'])).toBeTrue();
    expect(service.hasRole(['ROLE_MANAGER'])).toBeFalse();
  });

  it('treats an expired session as signed out', () => {
    service.set(fakeSession('ROLE_ADMIN', -1));

    expect(service.isActive()).toBeFalse();
    expect(service.getRole()).toBeNull();
  });

  it('loads the session from /auth/me once and then caches it', () => {
    let first: Session | null | undefined;
    service.ensureLoaded().subscribe((s) => (first = s));
    httpMock.expectOne('/api/auth/me').flush(fakeSession('ROLE_MANAGER'));

    expect(first?.role).toBe('ROLE_MANAGER');

    service.ensureLoaded().subscribe();
    httpMock.expectNone('/api/auth/me');
  });

  it('resolves to null when /auth/me says 401', () => {
    let result: Session | null | undefined;
    service.ensureLoaded().subscribe((s) => (result = s));
    httpMock
      .expectOne('/api/auth/me')
      .flush(null, { status: 401, statusText: 'Unauthorized' });

    expect(result).toBeNull();
  });

  it('asks the backend again once the cached session has expired', () => {
    service.set(fakeSession('ROLE_ADMIN', -1));

    service.ensureLoaded().subscribe();
    httpMock.expectOne('/api/auth/me').flush(fakeSession('ROLE_ADMIN'));
  });

  it('logs out on the backend and forgets the session', () => {
    service.set(fakeSession('ROLE_ADMIN'));

    service.logout().subscribe();

    const req = httpMock.expectOne('/api/auth/logout');
    expect(req.request.method).toBe('POST');
    req.flush(null);
    expect(service.isActive()).toBeFalse();
  });
});
