import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { FaultListComponent } from './fault-list.component';
import { FaultService } from '../../../services/fault.service';
import { SessionService } from '../../../services/session.service';
import { fakeSession } from '../../../../testing/session';

describe('FaultListComponent', () => {
  let fixture: ComponentFixture<FaultListComponent>;
  let component: FaultListComponent;
  let faults: jasmine.SpyObj<FaultService>;
  let dialog: jasmine.SpyObj<MatDialog>;

  const page = {
    content: [
      { id: 1, description: 'Brake issue', creationDateTime: '2024-02-15T14:30:00', updateDateTime: '2024-02-15T14:30:00' },
    ],
    page: { size: 5, number: 0, totalElements: 1, totalPages: 1 },
  };

  const render = (role: string) => {
    TestBed.inject(SessionService).set(fakeSession(role));
    fixture = TestBed.createComponent(FaultListComponent);
    component = fixture.componentInstance;
    component.vehicleId = 4;
    component.ngOnChanges();
    fixture.detectChanges();
    return fixture.nativeElement as HTMLElement;
  };

  beforeEach(async () => {
    faults = jasmine.createSpyObj('FaultService', ['getAll', 'delete']);
    faults.getAll.and.returnValue(of(page));
    faults.delete.and.returnValue(of({}));
    dialog = jasmine.createSpyObj('MatDialog', ['open']);
    dialog.open.and.returnValue({ afterClosed: () => of(true) } as never);

    await TestBed.configureTestingModule({
      declarations: [FaultListComponent],
      imports: [HttpClientTestingModule],
      providers: [
        { provide: FaultService, useValue: faults },
        { provide: MatDialog, useValue: dialog },
      ],
      schemas: [NO_ERRORS_SCHEMA],
    }).compileComponents();
  });

  it('loads the faults of its vehicle and searches them', () => {
    render('ROLE_OPERATOR');
    expect(faults.getAll).toHaveBeenCalledWith(4, 0, 5, '');

    component.onSearch('brake');
    expect(faults.getAll).toHaveBeenCalledWith(4, 0, 5, 'brake');
  });

  it('lets every employee report a fault, which updates the vehicle', () => {
    render('ROLE_ADMIN');
    let changed = false;
    component.changed.subscribe(() => (changed = true));

    component.addFault();

    expect(dialog.open).toHaveBeenCalled();
    expect(changed).toBeTrue();
  });

  it('shows delete only to admins and managers', () => {
    expect(render('ROLE_OPERATOR').textContent).not.toContain('Delete');
    expect(render('ROLE_MANAGER').textContent).toContain('Delete');
  });

  it('deletes after confirming', () => {
    const view = render('ROLE_ADMIN');
    spyOn(window, 'confirm').and.returnValue(true);

    (view.querySelector('.btn-outline-danger') as HTMLButtonElement).click();

    expect(faults.delete).toHaveBeenCalledWith(1);
  });
});
