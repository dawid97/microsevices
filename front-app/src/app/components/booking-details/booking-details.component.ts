import { Booking } from './../../models/Booking';
import { BookingService } from 'src/app/services/booking/booking.service';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-booking-details',
  templateUrl: './booking-details.component.html',
  styleUrls: ['./booking-details.component.css']
})
export class BookingDetailsComponent implements OnInit, OnDestroy {

  constructor(
    private bookingService: BookingService,
    private route: ActivatedRoute,
  ) { }

  booking: Booking;
  sub: any;
  bookingId: number;

  ngOnInit(): void {
    this.getBookingIdFromPath();
    this.findBooking();
  }

  findBooking(): void {
    this.bookingService.getBooking(this.bookingId).subscribe(data => {
      this.booking = data;
      this.prepareData();
    });
  }

  ngOnDestroy(): void {
    this.sub.unsubscribe();
  }

  prepareData(): void {
    this.booking.slot.startTime = this.booking.slot.startTime.replace('T', ' ');
    this.booking.slot.finishTime = this.booking.slot.finishTime.replace('T', ' ');
    this.booking.slot.isBooked = this.booking.slot.isBooked ? 'Yes' : 'No';
  }

  getBookingIdFromPath(): void {
    this.sub = this.route.params.subscribe(params => {
      this.bookingId = +params.id;
    });
  }
}

