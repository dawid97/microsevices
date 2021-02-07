import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Booking } from 'src/app/models/Booking';

@Injectable({
  providedIn: 'root'
})
export class BookingService {

  BASE_PATH = 'http://localhost:8765/api/booking-service/bookings';

  constructor(private http: HttpClient) { }

  getBookings(): Observable<Booking[]> {
    return this.http.get<Booking[]>(this.BASE_PATH);
  }

  createBooking(slotId: number, userId?: number): any {
    return this.http.post(this.BASE_PATH, { slotId, userId });
  }

  removeBooking(bookingId: number): any {
    return this.http.delete(this.BASE_PATH + '/' + bookingId);
  }

  getBooking(bookingId: number): any {
    return this.http.get(this.BASE_PATH + '/' + bookingId);
  }
}
