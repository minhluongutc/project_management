import {HttpClient} from "@angular/common/http";
import {BaseService} from "../share/services/base.service";
import {Environment} from "../share/environment/environment";
import {Injectable} from "@angular/core";
import {Project} from "../models/project.model";

@Injectable()
export class ProjectService extends BaseService {
  constructor(private http: HttpClient) {
    super(http);
  }

  get SERVICE_URL(): string {
    return `${Environment.baseUrl}/api`;
  }

  getProjects(userId: string, queryParams: Record<string, any>) {
    return this.getRequest(`${this.SERVICE_URL}/${userId}/projects`, {
      params: this.buildParams(queryParams)
    });
  }

  getProject(projectId: string) {
    return this.getRequest(`${this.SERVICE_URL}/projects/${projectId}`);
  }

  createProject(data: Project) {
    return this.postRequest(`${this.SERVICE_URL}/projects`, data);
  }

  updateProject(projectId: string, data: Project) {
    return this.putRequest(`${this.SERVICE_URL}/projects/${projectId}`, data);
  }


}
