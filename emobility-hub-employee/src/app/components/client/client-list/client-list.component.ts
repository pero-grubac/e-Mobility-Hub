import { Component } from '@angular/core';
import { Client, ClientPage } from '../../../models/client.models';
import { ClientService } from '../../../services/client.service';

@Component({
  selector: 'app-client-list',
  templateUrl: './client-list.component.html',
  styleUrl: './client-list.component.css',
})
export class ClientListComponent {
  clients: Client[] = [];
  currentPage: number = 0;
  totalPages: number = 0;
  displayedPages: (number | string)[] = [];
  searchTerm: string = '';

  constructor(private clientService: ClientService) {}

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
          this.updateDisplayedPages();
        },
        error: (err) => console.error('Error fetching clients:', err),
      });
  }

  onSearch(term: string): void {
    this.searchTerm = term;
    this.currentPage = 0;
    this.loadClients();
  }

  goToPage(page: number): void {
    if (page >= 0 && page < this.totalPages) {
      this.currentPage = page;
      this.loadClients();
    }
  }

  handlePageClick(page: number | string): void {
    if (typeof page === 'number') {
      this.goToPage(page);
    }
  }

  private updateDisplayedPages(): void {
    const delta = 2;
    const range: (number | string)[] = [];
    const start = Math.max(0, this.currentPage - delta);
    const end = Math.min(this.totalPages - 1, this.currentPage + delta);

    if (start > 0) {
      range.push(0);
      if (start > 1) range.push('...');
    }

    for (let i = start; i <= end; i++) range.push(i);

    if (end < this.totalPages - 1) {
      if (end < this.totalPages - 2) range.push('...');
      range.push(this.totalPages - 1);
    }

    this.displayedPages = range;
  }

  isNumber(page: number | string): boolean {
    return typeof page === 'number';
  }

  onBlockUnblockClient(clientId: number): void {
    const client = this.clients.find((c) => c.id === clientId);
    if (client) {
      const newBlockStatus = !client.isBlocked; // Nova vrednost za isBlocked

      this.clientService.block(clientId, newBlockStatus).subscribe({
        next: () => {
          client.isBlocked = newBlockStatus;
          console.log(
            `Client ${clientId} is now ${
              newBlockStatus ? 'blocked' : 'unblocked'
            }.`
          );
        },
        error: (err) => {
          console.error('Error blocking/unblocking client:', err);
        },
      });
    }
  }
}
