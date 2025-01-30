import { Component, Input, OnInit } from '@angular/core';
import { Fault } from '../../../models/fault.models';

@Component({
  selector: 'app-fault-detail',
  templateUrl: './fault-detail.component.html',
  styleUrl: './fault-detail.component.css',
})
export class FaultDetailComponent implements OnInit {
  @Input() fault!: Fault;
  truncatedDescription: string = '';

  ngOnInit(): void {
    this.truncatedDescription =
      this.fault.description.length > 10
        ? this.fault.description.substring(0, 10) + '...'
        : this.fault.description;
  }
}