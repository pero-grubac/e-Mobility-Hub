import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { JWTService } from '../../services/jwt.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrl: './header.component.css',
})
export class HeaderComponent implements OnInit {
  userRole: string | null = null;

  constructor(private router: Router, private jwtService: JWTService) {}

  ngOnInit(): void {
    this.userRole = this.jwtService.getRole();
  }

  onLogout(): void {
    this.jwtService.removeJWT();
    this.router.navigate(['/login']);
  }
  hasRole(requiredRoles: string[]): boolean {
    return this.jwtService.hasRole(requiredRoles);
  }
}
