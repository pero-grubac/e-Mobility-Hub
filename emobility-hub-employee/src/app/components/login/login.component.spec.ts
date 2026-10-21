import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { of, throwError } from 'rxjs';

import { LoginComponent } from './login.component';
import { AuthService } from '../../services/auth.service';
import { SessionService } from '../../services/session.service';
import { fakeSession } from '../../../testing/session';

describe('LoginComponent', () => {
  let fixture: ComponentFixture<LoginComponent>;
  let component: LoginComponent;
  let authService: jasmine.SpyObj<AuthService>;
  let sessionService: jasmine.SpyObj<SessionService>;
  let router: jasmine.SpyObj<Router>;

  beforeEach(async () => {
    authService = jasmine.createSpyObj('AuthService', ['login']);
    sessionService = jasmine.createSpyObj('SessionService', ['set']);
    router = jasmine.createSpyObj('Router', ['navigate']);

    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      imports: [FormsModule],
      providers: [
        { provide: AuthService, useValue: authService },
        { provide: SessionService, useValue: sessionService },
        { provide: Router, useValue: router },
      ],
      schemas: [NO_ERRORS_SCHEMA],
    }).compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('remembers the session and goes home on success', () => {
    const response = { ...fakeSession('ROLE_ADMIN'), token: 'jwt' };
    authService.login.and.returnValue(of(response));
    component.authRequest = { username: 'admin', password: 'admin123' };

    component.onSubmit();

    expect(authService.login).toHaveBeenCalledWith({
      username: 'admin',
      password: 'admin123',
    });
    expect(sessionService.set).toHaveBeenCalledWith(response);
    expect(router.navigate).toHaveBeenCalledWith(['/']);
    expect(component.errorMessage).toBe('');
  });

  it('shows an error and stays on the page on failure', () => {
    spyOn(console, 'error');
    authService.login.and.returnValue(throwError(() => ({ status: 401 })));

    component.onSubmit();
    fixture.detectChanges();

    expect(sessionService.set).not.toHaveBeenCalled();
    expect(router.navigate).not.toHaveBeenCalled();
    const error = fixture.nativeElement.querySelector('.error-message');
    expect(error?.textContent).toContain('Invalid username or password');
  });
});
