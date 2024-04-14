import {Injectable} from "@angular/core";
import {BaseService} from "../share/services/base.service";
import {HttpClient} from "@angular/common/http";
import {Environment} from "../share/environment/environment";

@Injectable()
export class TaskService extends BaseService {
  constructor(private http: HttpClient) {
    super(http);
  }

  get SERVICE_URL(): string {
    return `${Environment.baseUrl}/api/tasks`;
  }
}
