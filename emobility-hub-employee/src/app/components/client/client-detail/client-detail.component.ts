import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Client } from '../../../models/client.models';
import { ClientService } from '../../../services/client.service';

@Component({
  selector: 'app-client-detail',
  templateUrl: './client-detail.component.html',
  styleUrl: './client-detail.component.css',
})
export class ClientDetailComponent {
  client!: Client;

  constructor(
    private clientService: ClientService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    const clientId = this.route.snapshot.params['id']; 
    this.loadClient(clientId);
  }

  loadClient(id: number): void {
    this.clientService.getById(id).subscribe({
      next: (data: Client) => {
        this.client = data;
      },
      error: (err) => {
        console.error('Error fetching client details:', err);
      },
    });
  }
}
