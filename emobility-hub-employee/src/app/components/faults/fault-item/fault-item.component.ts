import { Component, Input, OnInit } from '@angular/core';
import { Fault } from '../../../models/fault.models';

@Component({
  selector: 'app-fault-item',
  templateUrl: './fault-item.component.html',
  styleUrl: './fault-item.component.css'
})
export class FaultItemComponent  implements OnInit {
  @Input() fault!: Fault;
  truncatedDescription: string = '';

  ngOnInit(): void {
    this.truncatedDescription =
      this.fault.description.length > 10
        ? this.fault.description.substring(0, 10) + '...'
        : this.fault.description;
  }
}
