import {Injectable} from "@angular/core";
import {BaseService} from "../share/services/base.service";
import {HttpClient} from "@angular/common/http";
import {Environment} from "../share/environment/environment";

@Injectable({
  providedIn: 'root'
})
export class NotificationService extends BaseService{
  constructor(private http: HttpClient) {
    super(http);
  }

  get SERVICE_URL(): string {
    return `${Environment.baseUrl}/api/notifications`;
  }

  getNotifications(queryParams: Record<string, any>) {
    return this.getRequest(this.SERVICE_URL, {
      params: this.buildParams(queryParams)
    });
  }

  readNotification(id: string) {
    return this.putRequest(`${this.SERVICE_URL}/${id}`, {});
  }
}
