import { TestBed } from '@angular/core/testing';
import {
  ActivatedRouteSnapshot,
  CanActivateFn,
  Router,
  RouterStateSnapshot,
  UrlTree,
} from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { Observable, firstValueFrom, of } from 'rxjs';

import { authGuard } from './auth.guard';
import { SessionService } from '../services/session.service';
import { Session } from '../models/auth.models';
import { fakeSession } from '../../testing/session';

describe('authGuard', () => {
  let session: Session | null;
  let router: Router;

  const run = (roles?: string[]) => {
    const route = {
      data: roles ? { roles } : {},
    } as unknown as ActivatedRouteSnapshot;
    const result = TestBed.runInInjectionContext(() =>
      authGuard(route, {} as RouterStateSnapshot),
    ) as Observable<boolean | UrlTree>;
    return firstValueFrom(result);
  };

  const redirectsTo = (result: boolean | UrlTree, url: string) =>
    result instanceof UrlTree && router.serializeUrl(result) === url;

  beforeEach(() => {
    session = null;
    TestBed.configureTestingModule({
      imports: [RouterTestingModule],
      providers: [
        {
          provide: SessionService,
          useValue: { ensureLoaded: () => of(session) },
        },
      ],
    });
    router = TestBed.inject(Router);
  });

  it('sends a visitor without a session to /login', async () => {
    expect(redirectsTo(await run(['ROLE_ADMIN']), '/login')).toBeTrue();
  });

  it('allows any signed-in user on a route without roles', async () => {
    session = fakeSession('ROLE_OPERATOR');
    expect(await run([])).toBeTrue();
  });

  it('allows a user whose role is listed', async () => {
    session = fakeSession('ROLE_MANAGER');
    expect(await run(['ROLE_ADMIN', 'ROLE_MANAGER'])).toBeTrue();
  });

  it('sends a signed-in user without the role to the start page', async () => {
    session = fakeSession('ROLE_OPERATOR');
    expect(
      redirectsTo(await run(['ROLE_ADMIN', 'ROLE_MANAGER']), '/'),
    ).toBeTrue();
  });
});
