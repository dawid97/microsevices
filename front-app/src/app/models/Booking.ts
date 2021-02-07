import { User } from 'src/app/models/User';
import { Slot } from './Slot';
import { Resource } from './Resource';

export class Booking {
  id: number;
  userId: number;
  slotId: number;
  slot?: Slot;
  resource?: Resource;
  user?: User;
}
