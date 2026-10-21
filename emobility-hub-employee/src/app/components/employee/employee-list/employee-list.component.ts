import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { EmployeeService } from '../../../services/employee.service';
import { User } from '../../../models/user.model';
import { AddEmployeeModalComponent } from '../add-employee-modal/add-employee-modal.component';

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

  constructor(
    private employeeService: EmployeeService,
    private router: Router,
    private dialog: MatDialog,
  ) {}

  ngOnInit(): void {
    this.loadEmployees();
  }

  loadEmployees(): void {
    this.employeeService
      .getEmployees(this.currentPage, this.pageSize, this.searchTerm)
      .subscribe((data) => {
        this.employees = data.content;
        this.totalPages = data.page.totalPages;
        this.currentPage = data.page.number;
      });
  }


  onAdd(): void {
    const dialogRef = this.dialog.open(AddEmployeeModalComponent, {
      width: '600px',
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.loadEmployees();
      }
    });
  }

  handleEmployeeSelected(employeeId: number): void {
    this.router.navigate(['/employee', employeeId]);
  }



  onSearch(term: string): void {
    this.searchTerm = term;
    this.currentPage = 0;
    this.loadEmployees();
  }

  onPageChange(page: number): void {
    this.currentPage = page;
    this.loadEmployees();
  }
}
