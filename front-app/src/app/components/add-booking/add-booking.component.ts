import { BookingService } from 'src/app/services/booking/booking.service';
import { SlotService } from './../../services/slot/slot.service';
import { ResourceService } from 'src/app/services/resource/resource.service';
import { Resource } from './../../models/Resource';
import { UserService } from 'src/app/services/user/user.service';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Observable } from 'rxjs';
import { User } from 'src/app/models/User';
import { map, startWith } from 'rxjs/operators';
import { Slot } from 'src/app/models/Slot';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-add-booking',
  templateUrl: './add-booking.component.html',
  styleUrls: ['./add-booking.component.css']
})
export class AddBookingComponent implements OnInit, OnDestroy {

  form: any = {};

  modalTitle = 'Information';
  isSuccessful = false;
  information = '\n';

  sub: any;
  slotId: number;

  resource: Resource;
  slot: Slot;

  selectedCustomer: FormControl;
  customersForSelector: string[];
  map: Map<string, number>;
  availableCustomers: User[];
  filteredCustomers: Observable<string[]>;
  slotIsPresent = false;
  resourceIsPresent = false;

  addBookingForm: FormGroup;

  constructor(
    private userService: UserService,
    private resourceService: ResourceService,
    private route: ActivatedRoute,
    private slotService: SlotService,
    private bookingService: BookingService,
    private router: Router
  ) {
    this.selectedCustomer = new FormControl();
    this.addBookingForm = new FormGroup({
      selectedCustomer: this.selectedCustomer
    });
    this.addBookingForm.controls.selectedCustomer.setValidators(Validators.required);
    this.map = new Map<string, number>();
  }

  getSlotIdFromPath(): void {
    this.sub = this.route.params.subscribe(params => {
      this.slotId = +params.id;
    });
  }

  ngOnDestroy(): void {
    this.sub.unsubscribe();
  }

  ngOnInit(): void {
    this.findCustomers();
    this.getSlotIdFromPath();
    this.findSlot();
  }

  findSlot(): void {
    this.slotService.getSlot(this.slotId).subscribe(data => {
      this.slot = data;
      this.prepareSlotData();
      this.slotIsPresent = true;
      this.findResource(this.slot.resourceId);
    },
      error => { }
    );
  }

  prepareSlotData(): void {
    this.slot.startTime = this.slot.startTime.replace('T', ' ');
    this.slot.finishTime = this.slot.finishTime.replace('T', ' ');
  }

  findResource(id: number): void {
    this.resourceService.getResource(id).subscribe(data => {
      this.resource = data;
      this.resourceIsPresent = true;
    },
      error => { }
    );
  }

  findCustomers(): void {
    this.userService.getCustomers().subscribe(data => {
      this.availableCustomers = data;
      this.prepareCustomersViewForSelector(this.availableCustomers);
      this.filteredCustomers = this.selectedCustomer.valueChanges
        .pipe(
          startWith(''),
          map(value => this.filterCustomers(value))
        );
    },
      (error: any) => { }
    );
  }

  private filterCustomers(customer: string): string[] {
    const filterValue = customer.toLowerCase();
    // tslint:disable-next-line: no-shadowed-variable
    return this.customersForSelector.filter(customer => customer.toLowerCase().includes(filterValue));
  }

  private prepareCustomersViewForSelector(customers: User[]): void {
    const customersList: string[] = [];

    customers.forEach(element => {
      const tmp = element.name + ' ' + element.surname + ' ' + element.ssn;
      this.map.set(tmp, element.id);
      customersList.push(tmp);
    });

    this.customersForSelector = customersList;
  }

  onSubmit(): void {
    if (!this.addBookingForm.get('selectedCustomer').valid) {
      this.information = '\nCustomer is required';
    } else {
      if (!this.map.get(this.selectedCustomer.value)) {
        this.information = '\nSelect customer from list';
      } else {
        this.addBooking(this.map.get(this.selectedCustomer.value));
      }
    }
  }

  addBooking(userId: number): void {
    this.bookingService.createBooking(this.slotId, userId).subscribe((data: any) => {
      this.information = '\nBooking was completed successfully';
      this.isSuccessful = true;
    },
      (error) => {
        this.information = '\n' + error.error.message;
        this.isSuccessful = false;
      });
  }

  okClicked(): void {
    if (this.isSuccessful) {
      this.router.navigate(['/bookings']);
    }
  }
}
