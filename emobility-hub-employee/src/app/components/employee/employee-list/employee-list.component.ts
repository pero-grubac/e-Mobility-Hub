import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { EmployeeService } from '../../../services/employee.service';
import { User } from '../../../models/user.model';

@Component({
  selector: 'app-employee-list',
  templateUrl: './employee-list.component.html',
  styleUrl: './employee-list.component.css',
})
export class EmployeeListComponent implements OnInit {
  employees: User[] = [];
  currentPage: number = 0;
  totalPages: number = 0;
  pageSize: number = 12;
  searchTerm: string = '';
  displayedPages: number[] = [];

  constructor(
    private employeeService: EmployeeService,
    private router: Router,
    private dialog: MatDialog
  ) {}

  ngOnInit(): void {
    this.loadEmployees();
  }

  loadEmployees(searchTerm: string = ''): void {
    this.employeeService
      .getEmployees(this.currentPage, this.pageSize, searchTerm)
      .subscribe((data) => {
        this.employees = data.content;
        this.totalPages = data.page.totalPages;
        this.currentPage = data.page.number;
        this.updateDisplayedPages();
      });
  }

  handleSearch(term: string): void {
    this.searchTerm = term;
    this.currentPage = 0;
    this.loadEmployees(term);
  }

  onAdd(): void {
    /* const dialogRef = this.dialog.open(AddEmployeeModalComponent, {
      width: '600px',
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.loadEmployees();
      }
    });*/
  }

  handleEmployeeSelected(employeeId: number): void {
    this.router.navigate(['/employee', employeeId]);
  }

  handlePageChange(page: number): void {
    if (page >= 0 && page < this.totalPages) {
      this.currentPage = page;
      this.loadEmployees();
    }
  }

  private updateDisplayedPages(): void {
    const delta = 2;
    const range: number[] = [];
    for (
      let i = Math.max(0, this.currentPage - delta);
      i <= Math.min(this.totalPages - 1, this.currentPage + delta);
      i++
    ) {
      range.push(i);
    }
    this.displayedPages = range;
  }
  goToPage(page: number): void {
    if (page >= 0 && page < this.totalPages) {
      this.currentPage = page;
      this.loadEmployees();
    }
  }
  onSearch(term: string): void {
    this.searchTerm = term;
    this.currentPage = 0;
    this.loadEmployees();
  }
  isNumber(page: number | string): boolean {
    return typeof page === 'number';
  }
}
