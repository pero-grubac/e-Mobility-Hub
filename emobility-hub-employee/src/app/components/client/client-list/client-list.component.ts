import { Component, OnInit } from '@angular/core';
import { Client, ClientPage } from '../../../models/client.models';
import { ClientService } from '../../../services/client.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-client-list',
  templateUrl: './client-list.component.html',
  styleUrl: './client-list.component.css',
})
export class ClientListComponent implements OnInit {
  clients: Client[] = [];
  currentPage: number = 0;
  totalPages: number = 0;
  searchTerm: string = '';

  constructor(
    private clientService: ClientService,
    private router: Router,
  ) {}

  ngOnInit(): void {
    this.loadClients();
  }

  loadClients(): void {
    this.clientService
      .getClients(this.currentPage, 10, this.searchTerm)
      .subscribe({
        next: (data: ClientPage) => {
          this.clients = data.content;
          this.totalPages = data.page.totalPages;
        },
        error: (err) => console.error('Error fetching clients:', err),
      });
  }

  onSearch(term: string): void {
    this.searchTerm = term;
    this.currentPage = 0;
    this.loadClients();
  }




  onBlockUnblockClient(clientId: number): void {
    const client = this.clients.find((c) => c.id === clientId);
    if (client) {
      const newBlockStatus = !client.isBlocked;

      this.clientService.block(clientId, newBlockStatus).subscribe({
        next: () => {
          client.isBlocked = newBlockStatus;
        },
        error: (err) => {
          console.error('Error blocking/unblocking client:', err);
        },
      });
    }
  }

  onClientClick(clientId: number): void {
    this.router.navigate(['/client', clientId]);
  }

  onPageChange(page: number): void {
    this.currentPage = page;
    this.loadClients();
  }
}
