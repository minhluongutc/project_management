import {BaseService} from "../share/services/base.service";
import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Environment} from "../share/environment/environment";
import {Filter} from "../models/filter.model";

@Injectable({
  providedIn: 'root'
})
export class UserCalendarService extends BaseService {
  constructor(private http: HttpClient) {
    super(http);
  }

  get SERVICE_URL(): string {
    return `${Environment.baseUrl}/api/user-calender`;
  }

  getUserCalendar(buildParams: Record<string, any>) {
    return this.getRequest(this.SERVICE_URL, {
      params: this.buildParams(buildParams)
    });
  }

  getUserCalendarById(id: string) {
    return this.getRequest(`${this.SERVICE_URL}/${id}`);
  }

  addUserCalendar(data: any) {
    return this.postRequest(this.SERVICE_URL, data);
  }

  updateUserCalendar(id: string, data: any) {
    return this.putRequest(`${this.SERVICE_URL}/${id}`, data);
  }

  deleteUserCalendar(id: string) {
    return this.deleteRequest(`${this.SERVICE_URL}/${id}`);
  }
}
