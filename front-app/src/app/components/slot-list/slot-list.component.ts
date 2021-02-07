import { BookingService } from 'src/app/services/booking/booking.service';
import { Resource } from './../../models/Resource';
import { Slot } from './../../models/Slot';
import { SlotService } from './../../services/slot/slot.service';
import { Component, HostListener, ViewChild, OnInit, OnDestroy } from '@angular/core';
import { MdbTableDirective } from 'angular-bootstrap-md';
import { ResourceService } from 'src/app/services/resource/resource.service';
import { CurrentUser } from 'src/app/models/CurrentUser';
import { TokenStorageService } from 'src/app/services/token-storage/token-storage.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-slot-list',
  templateUrl: './slot-list.component.html',
  styleUrls: ['./slot-list.component.css']
})
export class SlotListComponent implements OnInit, OnDestroy {

  @ViewChild(MdbTableDirective, { static: true }) mdbTable: MdbTableDirective;

  currentUser: CurrentUser;
  slots: Slot[] = [];
  headElements = ['startDate', 'startTime', 'finishDate', 'finishTime', 'isBooked'];
  titleElements = ['Start Date', 'Start Time', 'Finish Date', 'Finish Time', 'Booked'];
  searchText = '';
  previous: string;
  resourceId: number;
  sub: any;
  resource: Resource;
  information = '';
  isSuccessful = false;
  selectedItem: number;

  constructor(
    private resourceService: ResourceService,
    private slotService: SlotService,
    private tokenStorageService: TokenStorageService,
    private router: Router,
    private route: ActivatedRoute,
    private bookingService: BookingService
  ) { }

  ngOnDestroy(): void {
    this.sub.unsubscribe();
  }

  @HostListener('input') oninput(): void {
    this.searchItems();
  }

  ngOnInit(): void {
    this.getResourceIdFromPath();
    this.findResource();
    this.findSlots();
    this.findCurrentUser();
  }

  findCurrentUser(): void {
    this.currentUser = this.tokenStorageService.getUser();
  }

  findSlots(): void {
    this.slotService.getSlotsByResourceId(this.resourceId).subscribe(
      data => {
        this.slots = data;
        this.prepareData();
        this.mdbTable.setDataSource(this.slots);
        this.previous = this.mdbTable.getDataSource();
      },
      err => {
        this.slots = JSON.parse(err.error).message;
      }
    );
  }

  findResource(): void {
    this.resourceService.getResource(this.resourceId).subscribe(
      data => {
        this.resource = data;
      }
    );
  }

  getResourceIdFromPath(): void {
    this.sub = this.route.params.subscribe(params => {
      this.resourceId = +params.id;
    });
  }

  bookClicked(slotId: number): void {
    if (!this.currentUser.authorities.includes('ROLE_CUSTOMER')) {
      this.router.navigate(['/resources/' + this.resourceId + '/slots/' + slotId + '/bookings/add-booking']);
      return;
    }

    if (this.currentUser.authorities.includes('ROLE_CUSTOMER')) {
      this.addBooking(slotId);
    }
  }

  addBooking(slotId: number): void {
    this.bookingService.createBooking(slotId).subscribe((data: any) => {
      this.information = 'Your booking was completed successfully';
      this.isSuccessful = true;
    },
      (error) => {
        this.information = error.error.message;
        this.isSuccessful = false;
      });
  }

  addSlotClicked(): void {
    this.router.navigate(['/resources/' + this.resourceId + '/slots/add-slot']);
  }

  removeSlotClicked(): void {
    this.removeSlot();
  }

  removeSlot(): void {
    this.slotService.removeSlot(this.selectedItem).subscribe((data: any) => {
      this.reloadPage();
    },
      (error) => { });
  }

  reloadPage(): void {
    window.location.reload();
  }

  redirect(): void {
    if (this.isSuccessful) {
      this.router.navigate(['/bookings']);
    }
  }

  prepareData(): void {
    for (const slot of this.slots) {
      slot.startDate = slot.startTime.substr(0, 10);
      slot.startTime = slot.startTime.substr(11, 5);
      slot.finishDate = slot.finishTime.substr(0, 10);
      slot.finishTime = slot.finishTime.substr(11, 5);

      if (slot.isBooked.toString() === 'false') {
        slot.isBooked = 'No';
      }
      if (slot.isBooked.toString() === 'true') {
        slot.isBooked = 'Yes';
      }
    }
  }

  searchItems(): void {
    const prev = this.mdbTable.getDataSource();
    if (!this.searchText) {
      this.mdbTable.setDataSource(this.previous);
      this.slots = this.mdbTable.getDataSource();
    }
    if (this.searchText) {
      this.slots = this.mdbTable.searchLocalDataBy(this.searchText);
      this.mdbTable.setDataSource(prev);
    }
  }
}
