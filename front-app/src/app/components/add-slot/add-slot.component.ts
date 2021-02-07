import { SlotService } from './../../services/slot/slot.service';
import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-add-slot',
  templateUrl: './add-slot.component.html',
  styleUrls: ['./add-slot.component.css']
})
export class AddSlotComponent implements OnInit, OnDestroy {

  form: any = {};
  resourceId: number;
  sub: any;

  modalTitle: string;
  collisionSlots: any;
  isSuccessful = false;
  information = '\n';

  startDate: any;

  displayValue = '0 min';
  pitchValue = 0;

  minDate: Date;

  startTime: Date;
  finishTime: Date;
  divisor: string;

  @ViewChild('pickerStartDate', { static: true }) startDateTimePicker: any;
  @ViewChild('pickerFinishDate', { static: true }) finishDateTimePicker: any;

  constructor(private slotService: SlotService, private router: Router, private route: ActivatedRoute) {
    this.minDate = new Date();
    this.minDate.setHours(this.minDate.getHours() + 1);
  }

  ngOnInit(): void {
    this.getResourceIdFromPath();
  }

  pitch(event: any): void {
    this.pitchValue = event.value;
    this.buildDisplayValue(event);
  }

  buildDisplayValue(event: any): void {
    let seconds = event.value * 60;
    seconds = Number(seconds);

    const h = Math.floor(seconds / 3600);
    const m = Math.floor(seconds % 3600 / 60);

    const hDisplay = h > 0 ? h + (h === 1 ? ' h ' : ' h ') : '';
    const mDisplay = m > 0 ? m + (m === 1 ? ' min ' : ' min ') : '';
    this.displayValue = hDisplay + mDisplay;

    if (event.value === 0) {
      this.displayValue = '0 min';
    }
  }

  openPickerStartDate(): void {
    this.startDateTimePicker.open();
  }

  getResourceIdFromPath(): void {
    this.sub = this.route.params.subscribe(params => {
      this.resourceId = +params.id;
    });
  }

  ngOnDestroy(): void {
    this.sub.unsubscribe();
  }

  openPickerFinishDate(): void {
    this.finishDateTimePicker.open();
  }

  prepareDateFormat(): void {
    this.startTime = this.form.startTime.toDate();
    this.finishTime = this.form.finishTime.toDate();
    this.finishTime.setMilliseconds(0);
    this.finishTime.setSeconds(0);
    this.startTime.setMilliseconds(0);
    this.startTime.setSeconds(0);

    if (this.pitchValue === 0) {
      const dif = this.startTime.getTime() - this.finishTime.getTime();
      this.divisor = Math.abs(dif / 1000).toString();
    } else {
      this.divisor = (this.pitchValue * 60).toString();
    }
  }

  addSlot(): void {
    this.slotService.addSlot(
      this.startTime.toISOString().substr(0, 16),
      this.finishTime.toISOString().substr(0, 16),
      this.divisor, this.resourceId).subscribe((data: any) => {
        this.isSuccessful = true;
        this.modalTitle = 'Information';
        this.information = '\nSlots added successfully!';
      },
        (error: any) => {
          this.information = '\n';
          this.isSuccessful = false;
          this.matchException(error);
        });
  }

  matchException(error: any): void {
    this.modalTitle = 'Information';
    if (error.error.slots) {
      this.modalTitle = error.error.slot;
      this.collisionSlots = error.error.slots;
      this.prepareData();
    } else if (error.error.startTime) {
      this.information = '\nStart time and finish time cannot be the same';
    } else if (error.error.finishTime) {
      this.information = '\nFinish time cannot be less than start time';
    } else {
      this.information = '\nThe time period cannot be divided into equal parts';
    }
  }

  prepareData(): void {
    // tslint:disable-next-line: prefer-for-of
    for (let i = 0; i < this.collisionSlots[0].length; i++) {
      const startTime = this.collisionSlots[0][i].startTime.replace('T', ' ');
      const finishTime = this.collisionSlots[0][i].endTime.replace('T', ' ');
      this.information += startTime + '\n';
      this.information += finishTime + '\n\n';
    }
  }

  setDefaultInformation(): void {
    this.information = '\nPlease fill the form correctly';
  }

  onSubmit(): void {
    this.prepareDateFormat();
    this.addSlot();
  }

  okClicked(): void {
    this.information = '\n';
    if (this.isSuccessful === true) {
      this.router.navigate(['/resources/' + this.resourceId + '/slots']);
    }
  }
}
