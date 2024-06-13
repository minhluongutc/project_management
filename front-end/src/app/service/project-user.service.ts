import {Injectable} from "@angular/core";
import {BaseService} from "../share/services/base.service";
import {HttpClient} from "@angular/common/http";
import {Environment} from "../share/environment/environment";
import {User} from "../share/auth/user.model";

@Injectable({
  providedIn: 'root'
})
export class ProjectUserService extends BaseService {
  constructor(private http: HttpClient) {
    super(http);
  }

  get SERVICE_URL(): string {
    return `${Environment.baseUrl}/api/project-users`;
  }

  getUserByProject(queryParams: Record<string, any>) {
    return this.getRequest(this.SERVICE_URL, {
      params: this.buildParams(queryParams)
    });
  }

  addUserToProject(data: User[]) {
    return this.postRequest(this.SERVICE_URL, data);
  }

  createAndAddUserToProject(data: User[]) {
    return this.postRequest(`${this.SERVICE_URL}/add`, data);
  }

  changeProfessionalLevelAndPermissionProject(userId: string, projectId: string, professionalLevel: number, permission: number) {
    const data = {
      projectId: projectId,
      professionalLevel: professionalLevel,
      permission: permission
    }
    return this.putRequest(`${this.SERVICE_URL}/${userId}`, data);
  }

  getRoleInProject(projectId: string, userId: string) {
    return this.getRequest(`${this.SERVICE_URL}/role`,
      {
        params: this.buildParams({
          projectId,
          userId
        })
      }
    )
      ;
  }
}
