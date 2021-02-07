import { AddUserComponent } from './components/add-user/add-user.component';
import { UserListComponent } from './components/user-list/user-list.component';
import { AddBookingComponent } from './components/add-booking/add-booking.component';
import { AddSlotComponent } from './components/add-slot/add-slot.component';
import { AddResourceComponent } from './components/add-resource/add-resource.component';
import { BookingListComponent } from './components/booking-list/booking-list.component';
import { SlotListComponent } from './components/slot-list/slot-list.component';
import { ResourceListComponent } from './components/resource-list/resource-list.component';
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { BookingDetailsComponent } from './components/booking-details/booking-details.component';
import { AuthGuard } from './helpers/auth.guard';

const routes: Routes = [
  { path: 'home', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'resources', component: ResourceListComponent, canActivate: [AuthGuard] },
  { path: 'resources/:id/slots', component: SlotListComponent, canActivate: [AuthGuard] },
  { path: 'bookings', component: BookingListComponent, canActivate: [AuthGuard] },
  { path: 'bookings/:id', component: BookingDetailsComponent, canActivate: [AuthGuard] },
  { path: 'resources/add-resource', component: AddResourceComponent, canActivate: [AuthGuard] },
  { path: 'resources/:id/slots/:id/bookings/add-booking', component: AddBookingComponent, canActivate: [AuthGuard] },
  { path: 'resources/:id/slots/add-slot', component: AddSlotComponent, canActivate: [AuthGuard] },
  { path: 'users', component: UserListComponent, canActivate: [AuthGuard] },
  { path: 'users/add-user', component: AddUserComponent, canActivate: [AuthGuard] },
  { path: '', redirectTo: 'home', pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
