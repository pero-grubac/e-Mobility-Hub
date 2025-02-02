import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
})
export class AppComponent {
  title = 'emobility-hub-employee';
  isHomePage: boolean = false;
  showHeader: boolean = true;

  constructor(private router: Router) {
    this.router.events.subscribe(() => {
      this.isHomePage = this.router.url === '/';
      this.showHeader = this.router.url !== '/login';
    });
  }
}
