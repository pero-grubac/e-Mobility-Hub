import { Component, EventEmitter, Input, Output } from '@angular/core';
import { User } from '../../../models/user.model';

@Component({
  selector: 'app-employee-item',
  templateUrl: './employee-item.component.html',
  styleUrl: './employee-item.component.css',
})
export class EmployeeItemComponent {
  @Input() employee!: User;
  @Output() employeeClick = new EventEmitter<number>();

  onEmployeeClick(): void {
    this.employeeClick.emit(this.employee.id);
  }

  getRoleImage(role: string): string {
    switch (role) {
      case 'ROLE_ADMIN':
        return 'assets/images/admin.png';
      case 'ROLE_OPERATOR':
        return 'assets/images/operator.png';
      case 'ROLE_MANAGER':
        return 'assets/images/manager.png';
      default:
        return 'assets/images/default-avatar.png'; 
    }
  }

  formatRole(role: string): string {
    switch (role) {
      case 'ROLE_ADMIN':
        return 'Administrator';
      case 'ROLE_OPERATOR':
        return 'Operator';
      case 'ROLE_MANAGER':
        return 'Manager';
      default:
        return 'Unknown Role';
    }
  }
}
