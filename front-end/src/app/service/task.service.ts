import {Injectable} from "@angular/core";
import {BaseService} from "../share/services/base.service";
import {HttpClient} from "@angular/common/http";
import {Environment} from "../share/environment/environment";

@Injectable(
  {
    providedIn: 'root'
  }
)
export class TaskService extends BaseService {
  constructor(private http: HttpClient) {
    super(http);
  }

  get SERVICE_URL(): string {
    return `${Environment.baseUrl}/api/tasks`;
  }

  getTaskAccordingLevel(queryParams: Record<string, any>) {
    return this.getRequest(`${this.SERVICE_URL}/level`, {
      params: this.buildParams(queryParams)
    });
  }

  insertTask(data: any) {
    return this.postRequest(this.SERVICE_URL, data);
  }
}
