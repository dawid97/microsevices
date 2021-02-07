import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Slot } from 'src/app/models/Slot';

@Injectable({
  providedIn: 'root'
})
export class SlotService {

  BASE_PATH = 'http://localhost:8765/api/resource-service';

  constructor(private http: HttpClient) { }

  getSlotsByResourceId(resourceId: number): Observable<Slot[]> {
    return this.http.get<Slot[]>(this.BASE_PATH + '/resources/' + resourceId + '/slots');
  }

  removeSlot(id: number): any {
    return this.http.delete(this.BASE_PATH + '/slots/' + id);
  }

  addSlot(startTime: string, finishTime: string, divisor: string, resourceId: number): any {
    return this.http.post(this.BASE_PATH + '/slots', { startTime, finishTime, divisor, resourceId });
  }

  getSlot(id: number): Observable<Slot> {
    return this.http.get<Slot>(this.BASE_PATH + '/slots/' + id);
  }
}
