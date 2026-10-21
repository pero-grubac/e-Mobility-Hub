import { ComponentFixture, TestBed } from '@angular/core/testing';
import { PaginationComponent } from './pagination.component';

describe('PaginationComponent', () => {
  let fixture: ComponentFixture<PaginationComponent>;
  let component: PaginationComponent;

  const labels = () =>
    Array.from(
      fixture.nativeElement.querySelectorAll('button') as NodeListOf<HTMLButtonElement>,
    ).map((b) => b.textContent!.trim());

  const show = (currentPage: number, totalPages: number) => {
    component.currentPage = currentPage;
    component.totalPages = totalPages;
    component.ngOnChanges();
    fixture.detectChanges();
  };

  beforeEach(() => {
    TestBed.configureTestingModule({ declarations: [PaginationComponent] });
    fixture = TestBed.createComponent(PaginationComponent);
    component = fixture.componentInstance;
  });

  it('is hidden when there is only one page', () => {
    show(0, 1);
    expect(labels()).toEqual([]);
  });

  it('numbers pages from 1 and collapses long ranges', () => {
    show(5, 12);
    expect(labels()).toEqual(['Previous', '1', '…', '4', '5', '6', '7', '8', '…', '12', 'Next']);
  });

  it('emits the chosen page and ignores the current one', () => {
    show(0, 3);
    const emitted: number[] = [];
    component.pageChange.subscribe((p) => emitted.push(p));

    component.go(2);
    component.go(0);
    component.go(null);
    component.go(3);

    expect(emitted).toEqual([2]);
  });
});
