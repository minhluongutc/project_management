import {BaseService} from "../share/services/base.service";
import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Environment} from "../share/environment/environment";
import {Category} from "../models/category.model";

@Injectable({
  providedIn: 'root'
})
export class UpdateHistoryTaskService extends BaseService {
  constructor(private http: HttpClient) {
    super(http);
  }

  get SERVICE_URL(): string {
    return `${Environment.baseUrl}/api/update-history-task`;
  }

  getUpdateHistoryTask(taskId: string) {
    let queryParams: Record<string, any> = {
      taskId: taskId
    }
    return this.getRequest(this.SERVICE_URL, {
      params: this.buildParams(queryParams)
    });
  }
}
