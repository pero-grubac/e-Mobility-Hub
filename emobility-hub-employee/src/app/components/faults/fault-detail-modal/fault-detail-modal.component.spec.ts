import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FaultDetailModalComponent } from './fault-detail-modal.component';

describe('FaultDetailModalComponent', () => {
  let component: FaultDetailModalComponent;
  let fixture: ComponentFixture<FaultDetailModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [FaultDetailModalComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(FaultDetailModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
