import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class CommunicateService {
  private requestSource = new Subject<any>();
  private respondSource = new Subject<any>();

  // Observable string streams
  request$ = this.requestSource.asObservable();
  respond$ = this.respondSource.asObservable();

  // Service message commands
  request(data: any) {
    this.requestSource.next(data);
  }

  respond(data: any) {
    this.respondSource.next(data);
  }
}
