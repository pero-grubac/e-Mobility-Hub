import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ManufacturerItemComponent } from './manufacturer-item.component';

describe('ManufacturerItemComponent', () => {
  let component: ManufacturerItemComponent;
  let fixture: ComponentFixture<ManufacturerItemComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ManufacturerItemComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ManufacturerItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
