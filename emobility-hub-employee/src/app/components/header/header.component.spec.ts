import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { Router } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';

import { HeaderComponent } from './header.component';
import { SessionService } from '../../services/session.service';
import { fakeSession } from '../../../testing/session';

describe('HeaderComponent', () => {
  let fixture: ComponentFixture<HeaderComponent>;

  const render = (role: string) => {
    TestBed.inject(SessionService).set(fakeSession(role));
    fixture = TestBed.createComponent(HeaderComponent);
    fixture.detectChanges();
    const buttons = fixture.nativeElement.querySelectorAll(
      'button',
    ) as NodeListOf<HTMLButtonElement>;
    return Array.from(buttons).map((b) => b.textContent!.trim());
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [HeaderComponent],
      imports: [RouterTestingModule, HttpClientTestingModule],
      schemas: [NO_ERRORS_SCHEMA],
    }).compileComponents();
  });

  it('shows statistics only to a manager', () => {
    expect(render('ROLE_MANAGER')).toContain('Statistic');
    expect(render('ROLE_ADMIN')).not.toContain('Statistic');
  });

  it('hides manufacturers and employees from an operator', () => {
    const menu = render('ROLE_OPERATOR');
    expect(menu).toContain('Car');
    expect(menu).toContain('Rentals');
    expect(menu).not.toContain('Manufacturer');
    expect(menu).not.toContain('Employee');
  });

  it('shows the map to operators and managers, pricing only to managers', () => {
    expect(render('ROLE_OPERATOR')).toContain('Map');
    expect(render('ROLE_OPERATOR')).not.toContain('Pricing');
    expect(render('ROLE_MANAGER')).toContain('Pricing');
    expect(render('ROLE_ADMIN')).not.toContain('Map');
  });

  it('hides rentals from an admin', () => {
    const menu = render('ROLE_ADMIN');
    expect(menu).toContain('Employee');
    expect(menu).not.toContain('Rentals');
  });

  it('logs out on the backend and opens /login', () => {
    render('ROLE_ADMIN');
    const navigate = spyOn(TestBed.inject(Router), 'navigate');

    fixture.componentInstance.onLogout();
    TestBed.inject(HttpTestingController)
      .expectOne('/api/auth/logout')
      .flush(null);

    expect(TestBed.inject(SessionService).isActive()).toBeFalse();
    expect(navigate).toHaveBeenCalledWith(['/login']);
  });
});
