import {Injectable} from "@angular/core";
import { BehaviorSubject } from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class ProjectStoreService {
  private readonly _id = new BehaviorSubject<string>('');
  readonly id$ = this._id.asObservable();

  get id(): string {
    return this._id.getValue();
  }

  set id(val: string) {
    this._id.next(val);
  }

  resetStore() {
    this.id = ''
  }
}
