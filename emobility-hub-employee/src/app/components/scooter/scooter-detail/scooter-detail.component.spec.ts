import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ScooterDetailComponent } from './scooter-detail.component';

describe('ScooterDetailComponent', () => {
  let component: ScooterDetailComponent;
  let fixture: ComponentFixture<ScooterDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ScooterDetailComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ScooterDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
