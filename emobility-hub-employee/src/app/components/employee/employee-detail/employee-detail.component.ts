import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { User } from '../../../models/user.model';
import { EmployeeService } from '../../../services/employee.service';
import { RoleEnum } from '../../../models/role.enum';

@Component({
  selector: 'app-employee-detail',
  templateUrl: './employee-detail.component.html',
  styleUrl: './employee-detail.component.css',
})
export class EmployeeDetailComponent implements OnInit {
  employee: User | null = null;
  roles: string[] = Object.values(RoleEnum);
  
  constructor(
    private employeeService: EmployeeService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    const employeeId = this.route.snapshot.paramMap.get('id');
    if (employeeId) {
      this.loadEmployee(Number(employeeId));
    }
  }

  loadEmployee(id: number): void {
    this.employeeService.getById(id).subscribe({
      next: (employee) => {
        this.employee = employee;
      },
      error: (err) => {
        console.error('Error fetching employee:', err);
      },
    });
  }

  onUpdate(): void {
    /* if (this.employee) {
      this.employeeService.updateEmployee(this.employee.id, this.employee).subscribe({
        next: () => {
          alert('Employee updated successfully!');
          this.router.navigate(['/employees']);
        },
        error: (err) => {
          console.error('Error updating employee:', err);
        },
      });
    }*/
  }

  onDelete(): void {
    /* if (this.employee) {
      const confirmed = confirm('Are you sure you want to delete this employee?');
      if (confirmed) {
        this.employeeService.deleteEmployee(this.employee.id).subscribe({
          next: () => {
            alert('Employee deleted successfully!');
            this.router.navigate(['/employees']);
          },
          error: (err) => {
            console.error('Error deleting employee:', err);
          },
        });
      }
    }*/
  }
}