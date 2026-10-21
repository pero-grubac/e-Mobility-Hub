import {
  AfterViewInit,
  Component,
  ElementRef,
  OnDestroy,
  OnInit,
  ViewChild,
} from '@angular/core';
import { Router } from '@angular/router';
import * as L from 'leaflet';
import {
  VEHICLE_ROUTES,
  VehicleSummary,
  VehicleType,
} from '../../models/vehicle.model';
import { VehicleService } from '../../services/vehicle.service';

type Status = 'available' | 'rented' | 'broken';

const STATUS_COLORS: Record<Status, string> = {
  available: '#1b7a36',
  rented: '#1f4fb8',
  broken: '#b42318',
};

// Banja Luka, where the demo fleet is parked.
const DEFAULT_CENTER: L.LatLngTuple = [44.7722, 17.191];

@Component({
  selector: 'app-vehicle-map',
  templateUrl: './vehicle-map.component.html',
  styleUrl: './vehicle-map.component.css',
})
export class VehicleMapComponent implements OnInit, AfterViewInit, OnDestroy {
  @ViewChild('map') mapElement!: ElementRef<HTMLDivElement>;

  readonly types: { value: VehicleType; label: string }[] = [
    { value: 'CAR', label: 'Cars' },
    { value: 'BICYCLE', label: 'Bicycles' },
    { value: 'SCOOTER', label: 'Scooters' },
  ];
  readonly statuses: { value: Status; label: string }[] = [
    { value: 'available', label: 'Available' },
    { value: 'rented', label: 'Rented' },
    { value: 'broken', label: 'Broken' },
  ];
  readonly colors = STATUS_COLORS;

  shownTypes = new Set<VehicleType>(['CAR', 'BICYCLE', 'SCOOTER']);
  shownStatuses = new Set<Status>(['available', 'rented', 'broken']);
  vehicles: VehicleSummary[] = [];

  private map?: L.Map;
  private markers = L.layerGroup();

  constructor(
    private vehicleService: VehicleService,
    private router: Router,
  ) {}

  ngOnInit(): void {
    this.vehicleService.getLocations().subscribe({
      next: (vehicles) => {
        this.vehicles = vehicles;
        this.render();
        this.fitToVehicles();
      },
      error: (err) => console.error('Error fetching vehicle locations:', err),
    });
  }

  ngAfterViewInit(): void {
    this.map = L.map(this.mapElement.nativeElement).setView(DEFAULT_CENTER, 14);
    L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
      maxZoom: 19,
      attribution:
        '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors',
    }).addTo(this.map);
    this.markers.addTo(this.map);
    // Popups link to the detail pages; route the click inside the SPA.
    this.map.on('popupopen', (event) => {
      const link = event.popup.getElement()?.querySelector('a[data-route]');
      link?.addEventListener('click', (click) => {
        click.preventDefault();
        this.router.navigateByUrl(link.getAttribute('data-route')!);
      });
    });
    // The vehicles may already be loaded.
    this.render();
    this.fitToVehicles();
  }

  ngOnDestroy(): void {
    this.map?.remove();
  }

  statusOf(vehicle: VehicleSummary): Status {
    if (vehicle.broken) return 'broken';
    return vehicle.rented ? 'rented' : 'available';
  }

  toggleType(type: VehicleType): void {
    this.toggle(this.shownTypes, type);
  }

  toggleStatus(status: Status): void {
    this.toggle(this.shownStatuses, status);
  }

  get visible(): VehicleSummary[] {
    return this.vehicles.filter(
      (v) => this.shownTypes.has(v.type) && this.shownStatuses.has(this.statusOf(v)),
    );
  }

  private toggle<T>(set: Set<T>, value: T): void {
    if (set.has(value)) {
      set.delete(value);
    } else {
      set.add(value);
    }
    this.render();
  }

  private render(): void {
    if (!this.map) {
      return;
    }
    this.markers.clearLayers();
    for (const vehicle of this.visible) {
      const status = this.statusOf(vehicle);
      L.circleMarker([vehicle.latitude!, vehicle.longitude!], {
        radius: vehicle.type === 'CAR' ? 10 : 8,
        color: '#fff',
        weight: 2,
        fillColor: STATUS_COLORS[status],
        fillOpacity: 0.9,
      })
        .bindPopup(this.popup(vehicle, status))
        .bindTooltip(vehicle.uniqueIdentifier)
        .addTo(this.markers);
    }
  }

  private popup(vehicle: VehicleSummary, status: Status): string {
    const route = `${VEHICLE_ROUTES[vehicle.type]}/${vehicle.id}`;
    const escape = (text: string) =>
      text.replace(/[&<>"]/g, (c) => `&#${c.charCodeAt(0)};`);
    return `<strong>${escape(vehicle.uniqueIdentifier)}</strong><br>
      ${escape(vehicle.manufacturer)} ${escape(vehicle.model)}<br>
      Status: ${status}<br>
      ${vehicle.rentPrice.toFixed(2)} / hour<br>
      <a href="${route}" data-route="${route}">Details</a>`;
  }

  private fitToVehicles(): void {
    if (this.map && this.vehicles.length > 0) {
      const bounds = L.latLngBounds(
        this.vehicles.map((v) => [v.latitude!, v.longitude!] as L.LatLngTuple),
      );
      this.map.fitBounds(bounds, { padding: [40, 40], maxZoom: 15 });
    }
  }
}
