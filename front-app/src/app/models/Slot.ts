import { NumberValueAccessor } from '@angular/forms';

export class Slot {
  id: number;
  startDate?: string;
  startTime: string;
  finishDate?: string;
  finishTime: string;
  divisor?: number;
  isBooked: string;
  resourceId: number;
}
