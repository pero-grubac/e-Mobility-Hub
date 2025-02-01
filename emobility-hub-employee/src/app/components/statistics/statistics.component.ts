import { Component, OnInit } from '@angular/core';
import { StatisticService } from '../../services/statistic.service';
import { MatDatepicker } from '@angular/material/datepicker';
import { DailyRevenue } from '../../models/statistic.models';

@Component({
  selector: 'app-statistics',
  templateUrl: './statistics.component.html',
  styleUrl: './statistics.component.css',
})
export class StatisticsComponent implements OnInit {
  dailyRevenueData: any[] = [];
  revenueByVehicleTypeData: any[] = [];
  faultsPerVehicleData: any[] = [];
  currentDate = new Date();

  view: [number, number] = [700, 400]; // Chart Options
  showLegend = true;
  showLabels = true;
  gradient = false;
  animations = true;
  maxFaultValue = 0;
  constructor(private statisticService: StatisticService) {}

  ngOnInit(): void {
    const start = this.formatDate(
      new Date(this.currentDate.getFullYear(), this.currentDate.getMonth(), 1)
    );
    const end = this.formatDate(
      new Date(
        this.currentDate.getFullYear(),
        this.currentDate.getMonth() + 1,
        0
      )
    );
    this.fetchDailyRevenue(start, end);

    this.fetchRevenueByVehicleType();
    this.fetchFaultsPerVehicle();
  }

  fetchDailyRevenue(start: string, end: string): void {
    this.statisticService.getDailyRevenue(start, end).subscribe(
      (data: DailyRevenue[]) => {
        if (data && Array.isArray(data)) {
          this.dailyRevenueData = data.map((entry) => ({
            name: entry.rentalDay, // Ensure name is a valid date string
            value: entry.totalRevenue, // Ensure value is a number
          }));
        } else {
          console.error('Invalid data received for daily revenue:', data);
          this.dailyRevenueData = []; // Empty data fallback
        }
      },
      (error) => {
        console.error('Error fetching daily revenue:', error);
        this.dailyRevenueData = []; // Fallback in case of an error
      }
    );
  }

  chooseMonth(normalizedMonth: Date, datepicker: MatDatepicker<Date>): void {
    const start = this.formatDate(
      new Date(normalizedMonth.getFullYear(), normalizedMonth.getMonth(), 1)
    );
    const end = this.formatDate(
      new Date(normalizedMonth.getFullYear(), normalizedMonth.getMonth() + 1, 0)
    );

    this.fetchDailyRevenue(start, end);
    datepicker.close();
  }

  // Format date to yyyy-MM-ddThh:mm:ss
  private formatDate(date: Date): string {
    const year = date.getFullYear();
    const month = this.pad(date.getMonth() + 1); // Months are 0-indexed
    const day = this.pad(date.getDate());
    const hours = this.pad(date.getHours());
    const minutes = this.pad(date.getMinutes());
    const seconds = this.pad(date.getSeconds());
    return `${year}-${month}-${day}T${hours}:${minutes}:${seconds}`;
  }

  // Helper to pad single-digit months/days/hours/minutes/seconds with a leading zero
  private pad(value: number): string {
    return value < 10 ? `0${value}` : `${value}`;
  }

  fetchRevenueByVehicleType(): void {
    this.statisticService.getRevenueByVehicleType().subscribe((data) => {
      this.revenueByVehicleTypeData = data.map((entry) => ({
        name: entry.vehicleType,
        value: entry.totalRevenue,
      }));
    });
  }

  fetchFaultsPerVehicle(): void {
    this.statisticService.getFaultsPerVehicle().subscribe((data) => {
      this.faultsPerVehicleData = data.map((entry) => ({
        name: entry.model,
        value: Math.round(entry.faultCount),
      }));
    });
    this.maxFaultValue = Math.ceil(
      Math.max(...this.faultsPerVehicleData.map((d) => d.value))
    );
  
  }
  
}
