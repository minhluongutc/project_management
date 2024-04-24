import {BaseService} from "../share/services/base.service";
import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Environment} from "../share/environment/environment";
import {Category} from "../models/category.model";
import {StatusIssue} from "../models/status-issue.model";

@Injectable({
  providedIn: 'root'
})
export class StatusIssueService extends BaseService {
  constructor(private http: HttpClient) {
    super(http);
  }

  get SERVICE_URL(): string {
    return `${Environment.baseUrl}/api/status-issue`;
  }

  getStatusIssue(queryParams: Record<string, any>) {
    return this.getRequest(this.SERVICE_URL, {
      params: this.buildParams(queryParams)
    });
  }

  createStatusIssue(data: StatusIssue) {
    return this.postRequest(this.SERVICE_URL, data);
  }

  updateStatusIssue(id: string, data: StatusIssue) {
    return this.putRequest(`${this.SERVICE_URL}/${id}`, data);
  }

  deleteStatusIssue(id: string) {
    return this.deleteRequest(`${this.SERVICE_URL}/${id}`);
  }
}
