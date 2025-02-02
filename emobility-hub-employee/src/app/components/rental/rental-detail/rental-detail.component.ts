import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { RentalService } from '../../../services/rental.service';
import { DetailedRental } from '../../../models/rental.model';

@Component({
  selector: 'app-rental-detail',
  templateUrl: './rental-detail.component.html',
  styleUrl: './rental-detail.component.css',
})
export class RentalDetailComponent implements OnInit {
  rental!: DetailedRental;

  constructor(
    private route: ActivatedRoute,
    private rentalService: RentalService,
    private router: Router
  ) {}

  ngOnInit(): void {
    const rentalId = Number(this.route.snapshot.paramMap.get('id'));
    if (rentalId) {
      this.loadRentalDetails(rentalId);
    }
  }

  loadRentalDetails(rentalId: number): void {
    this.rentalService.getById(rentalId).subscribe({
      next: (data) => {
        this.rental = data;
      },
      error: (err) => {
        console.error('Error loading rental details:', err);
      },
    });
  }
  onClientClick(clientId: number): void {
    this.router.navigate(['/client', clientId]);
  }
  onVehicleSelected(vehicleId: number): void {}
}
