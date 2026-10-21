import { TestBed } from '@angular/core/testing';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { Router } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { Component } from '@angular/core';
import { AppComponent } from './app.component';

@Component({ template: '' })
class BlankComponent {}

describe('AppComponent', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([
          { path: 'login', component: BlankComponent },
          { path: 'cars', component: BlankComponent },
        ]),
      ],
      declarations: [AppComponent, BlankComponent],
      schemas: [NO_ERRORS_SCHEMA],
    }).compileComponents();
  });

  it('creates the app', () => {
    const fixture = TestBed.createComponent(AppComponent);
    expect(fixture.componentInstance.title).toBe('emobility-hub-employee');
  });

  it('hides the header on /login and shows it elsewhere', async () => {
    const fixture = TestBed.createComponent(AppComponent);
    const router = TestBed.inject(Router);

    await router.navigateByUrl('/login');
    fixture.detectChanges();
    expect(fixture.componentInstance.showHeader).toBeFalse();
    expect(fixture.nativeElement.querySelector('app-header')).toBeNull();

    await router.navigateByUrl('/cars');
    fixture.detectChanges();
    expect(fixture.componentInstance.showHeader).toBeTrue();
    expect(fixture.nativeElement.querySelector('app-header')).not.toBeNull();
  });
});
