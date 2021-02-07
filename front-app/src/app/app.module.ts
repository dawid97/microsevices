import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { HomeComponent } from './components/home/home.component';
import { FormsModule } from '@angular/forms';
import { authInterceptorProviders } from './helpers/auth.interceptor';
import { ResourceListComponent } from './components/resource-list/resource-list.component';
import { MDBBootstrapModule } from 'angular-bootstrap-md';
import { SlotListComponent } from './components/slot-list/slot-list.component';
import { BookingListComponent } from './components/booking-list/booking-list.component';
import { BookingDetailsComponent } from './components/booking-details/booking-details.component';
import { AddResourceComponent } from './components/add-resource/add-resource.component';
import { AddSlotComponent } from './components/add-slot/add-slot.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { NgxMatDateAdapter, NgxMatDateFormats, NgxMatDatetimePickerModule, NgxMatTimepickerModule, NGX_MAT_DATE_FORMATS } from '@angular-material-components/datetime-picker';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { ReactiveFormsModule } from '@angular/forms';
import { MatNativeDateModule } from '@angular/material/core';
import { NgxMatMomentAdapter, NGX_MAT_MOMENT_DATE_ADAPTER_OPTIONS, } from '@angular-material-components/moment-adapter';
import { MatSliderModule } from '@angular/material/slider';
import { AddBookingComponent } from './components/add-booking/add-booking.component';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { UserListComponent } from './components/user-list/user-list.component';
import { AddUserComponent } from './components/add-user/add-user.component';

const CUSTOM_DATE_FORMATS: NgxMatDateFormats = {
  parse: {
    dateInput: 'l, LTS'
  },
  display: {
    dateInput: 'DD-MM-YYYY HH:mm',
    monthYearLabel: 'MMM YYYY',
    dateA11yLabel: 'LL',
    monthYearA11yLabel: 'MMMM YYYY'
  }
};

@NgModule({
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    BrowserAnimationsModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatInputModule,
    NgxMatTimepickerModule,
    ReactiveFormsModule,
    MatButtonModule,
    HttpClientModule,
    NgxMatDatetimePickerModule,
    MatInputModule,
    MatAutocompleteModule,
    MatSliderModule,
    MDBBootstrapModule.forRoot(),
  ],
  declarations: [
    AppComponent,
    LoginComponent,
    RegisterComponent,
    HomeComponent,
    ResourceListComponent,
    SlotListComponent,
    BookingListComponent,
    BookingDetailsComponent,
    AddResourceComponent,
    AddSlotComponent,
    AddBookingComponent,
    UserListComponent,
    AddUserComponent
  ],
  providers: [
    authInterceptorProviders,
    { provide: NGX_MAT_MOMENT_DATE_ADAPTER_OPTIONS, useValue: { useUtc: true } },
    { provide: NGX_MAT_DATE_FORMATS, useValue: CUSTOM_DATE_FORMATS },
    { provide: NgxMatDateAdapter, useClass: NgxMatMomentAdapter },
  ],
  bootstrap: [AppComponent],
})
export class AppModule { }
