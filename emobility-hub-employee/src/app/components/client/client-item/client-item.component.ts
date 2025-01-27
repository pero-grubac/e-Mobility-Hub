import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Client } from '../../../models/client.models';

@Component({
  selector: 'app-client-item',
  templateUrl: './client-item.component.html',
  styleUrl: './client-item.component.css',
})
export class ClientItemComponent {
  @Input() client!: Client;
  @Output() blockUnblock = new EventEmitter<number>();
  @Output() clientClick = new EventEmitter<number>();

  toggleBlock(clientId: number, event: MouseEvent): void {
    event.stopPropagation();
    this.blockUnblock.emit(clientId);
  }
  onClientClick(): void {
    this.clientClick.emit(this.client.id);
  }
}
