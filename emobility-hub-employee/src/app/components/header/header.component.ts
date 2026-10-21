import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { SessionService } from '../../services/session.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrl: './header.component.css',
})
export class HeaderComponent implements OnInit {
  userRole: string | null = null;

  constructor(
    private router: Router,
    private sessionService: SessionService,
  ) {}

  ngOnInit(): void {
    this.userRole = this.sessionService.getRole();
  }

  onLogout(): void {
    this.sessionService.logout().subscribe({
      complete: () => this.router.navigate(['/login']),
      error: () => this.router.navigate(['/login']),
    });
  }

  hasRole(requiredRoles: string[]): boolean {
    return this.sessionService.hasRole(requiredRoles);
  }
}
