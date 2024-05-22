import {BaseService} from "../share/services/base.service";
import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Environment} from "../share/environment/environment";
import {Filter} from "../models/filter.model";

@Injectable({
  providedIn: 'root'
})
export class FilterService extends BaseService {
  constructor(private http: HttpClient) {
    super(http);
  }

  get SERVICE_URL(): string {
    return `${Environment.baseUrl}/api/filters`;
  }

  getFilters(projectId: any) {
    return this.getRequest(this.SERVICE_URL, {
      params: this.buildParams({
        projectId
      })
    });
  }

  addFilter(data: Filter) {
    return this.postRequest(this.SERVICE_URL, data);
  }

  updateFilterName(id: string, filterName: any) {
    return this.putRequest(`${this.SERVICE_URL}/${id}`, filterName);
  }

  deleteFilter(id: string) {
    return this.deleteRequest(`${this.SERVICE_URL}/${id}`);
  }
}
