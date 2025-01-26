import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddScooterModalComponent } from './add-scooter-modal.component';

describe('AddScooterModalComponent', () => {
  let component: AddScooterModalComponent;
  let fixture: ComponentFixture<AddScooterModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AddScooterModalComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AddScooterModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
