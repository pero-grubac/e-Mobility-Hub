import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RentalItemComponent } from './rental-item.component';

describe('RentalItemComponent', () => {
  let component: RentalItemComponent;
  let fixture: ComponentFixture<RentalItemComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RentalItemComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(RentalItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
