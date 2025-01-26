import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddBicycleModalComponent } from './add-bicycle-modal.component';

describe('AddBicycleModalComponent', () => {
  let component: AddBicycleModalComponent;
  let fixture: ComponentFixture<AddBicycleModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AddBicycleModalComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AddBicycleModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
