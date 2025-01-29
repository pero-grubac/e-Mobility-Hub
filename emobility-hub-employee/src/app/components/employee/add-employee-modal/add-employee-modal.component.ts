import { Component, EventEmitter, Output } from '@angular/core';
import { DetailedUserRequest } from '../../../models/user.model';
import { EmployeeService } from '../../../services/employee.service';
import { MatDialogRef } from '@angular/material/dialog';
import { RoleEnum } from '../../../models/role.enum';

@Component({
  selector: 'app-add-employee-modal',
  templateUrl: './add-employee-modal.component.html',
  styleUrl: './add-employee-modal.component.css',
})
export class AddEmployeeModalComponent {
  employee: DetailedUserRequest = {
    username: '',
    firstName: '',
    lastName: '',
    password: '',
    id: null,
    role: '',
  };

  roles = Object.values(RoleEnum);

  constructor(
    private dialogRef: MatDialogRef<AddEmployeeModalComponent>,
    private employeeService: EmployeeService
  ) {}

  onSave(): void {
    if (
      this.employee.username &&
      this.employee.firstName &&
      this.employee.lastName &&
      this.employee.password &&
      this.employee.role
    ) {
      this.employeeService.register(this.employee).subscribe({
        next: () => {
          alert('Employee created successfully!');
          this.dialogRef.close(true);
        },
        error: (err) => {
          console.error('Error creating employee:', err);
        },
      });
    } else {
      alert('Please fill in all fields.');
    }
  }

  onCancel(): void {
    this.dialogRef.close(false);
  }
}
