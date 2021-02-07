import { Booking } from './../../models/Booking';
import { Component, HostListener, OnInit, ViewChild } from '@angular/core';
import { CurrentUser } from 'src/app/models/CurrentUser';
import { TokenStorageService } from 'src/app/services/token-storage/token-storage.service';
import { Router } from '@angular/router';
import { BookingService } from 'src/app/services/booking/booking.service';
import { MdbTableDirective } from 'angular-bootstrap-md';

@Component({
  selector: 'app-booking-list',
  templateUrl: './booking-list.component.html',
  styleUrls: ['./booking-list.component.css']
})
export class BookingListComponent implements OnInit {

  @ViewChild(MdbTableDirective, { static: true }) mdbTable: MdbTableDirective;

  currentUser: CurrentUser;
  bookings: Booking[] = [];
  headElements = ['slot.startDate', 'slot.startTime', 'slot.finishDate', 'slot.finishTime', 'resource.name', 'resource.type', 'user.ssn'];
  titleElements = ['Start Date', 'Start Time', 'Finish Date', 'Finish Time', 'Resource Name', 'Resource Type', 'Ssn'];
  searchText = '';
  previous: string;
  selectedItem: number;
  sub: any;

  constructor(
    private bookingService: BookingService,
    private tokenStorageService: TokenStorageService,
    private router: Router
  ) { }

  @HostListener('input') oninput(): void {
    this.searchItems();
  }

  ngOnInit(): void {
    this.findBookings();
    this.findCurrentUser();
  }

  findCurrentUser(): void {
    this.currentUser = this.tokenStorageService.getUser();
  }

  findBookings(): void {
    this.bookingService.getBookings().subscribe(
      data => {
        this.bookings = data;
        this.prepareData();
        this.mdbTable.setDataSource(this.bookings);
        this.previous = this.mdbTable.getDataSource();
      },
      err => {
        this.bookings = JSON.parse(err.error).message;
      }
    );
  }

  prepareData(): void {
    for (const booking of this.bookings) {
      booking.slot.startDate = booking.slot.startTime.substr(0, 10);
      booking.slot.startTime = booking.slot.startTime.substr(11, 5);
      booking.slot.finishDate = booking.slot.finishTime.substr(0, 10);
      booking.slot.finishTime = booking.slot.finishTime.substr(11, 5);
    }
  }

  detailsClicked(bookingId: number): void {
    this.router.navigate(['/bookings', bookingId]);
  }

  removeBookClicked(): void {
    this.removeBooking();
  }

  removeBooking(): void {
    this.bookingService.removeBooking(this.selectedItem).subscribe(data => {
      this.reloadPage();
    },
      (error) => { });
  }

  reloadPage(): void {
    window.location.reload();
  }

  searchItems(): void {
    const prev = this.mdbTable.getDataSource();
    if (!this.searchText) {
      this.mdbTable.setDataSource(this.previous);
      this.bookings = this.mdbTable.getDataSource();
    }
    if (this.searchText) {
      this.bookings = this.mdbTable.searchLocalDataBy(this.searchText);
      this.mdbTable.setDataSource(prev);
    }
  }
}
