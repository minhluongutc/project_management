import {Injectable} from "@angular/core";
import { BehaviorSubject } from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class ProjectStoreService {
  private readonly _id = new BehaviorSubject<string>('');
  readonly id$ = this._id.asObservable();

  private readonly _role = new BehaviorSubject<number>(0);
  readonly role$ = this._role.asObservable();

  get id(): string {
    return this._id.getValue();
  }

  set id(val: string) {
    this._id.next(val);
  }

  get role(): number {
    return this._role.getValue();
  }

  set role(val: number) {
    this._role.next(val);
  }

  resetStore() {
    this.id = '';
    this.role = 0;
  }
}
