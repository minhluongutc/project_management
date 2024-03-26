import {HttpClient} from "@angular/common/http";
import {BaseService} from "../share/services/base.service";
import {Environment} from "../share/environment/environment";
import {Injectable} from "@angular/core";

@Injectable()
export class ProjectService extends BaseService {
  constructor(private http: HttpClient) {
    super(http);
  }

  get SERVICE_URL(): string {
    return `${Environment.baseUrl}/api`;
  }

  getProjects(userId: number, queryParams: Record<string, any>) {
    return this.getRequest(`${this.SERVICE_URL}/${userId}/projects`, {
      params: this.buildParams(queryParams)
    });
  }


}
