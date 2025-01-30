import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FaultItemComponent } from './fault-item.component';

describe('FaultItemComponent', () => {
  let component: FaultItemComponent;
  let fixture: ComponentFixture<FaultItemComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [FaultItemComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(FaultItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
