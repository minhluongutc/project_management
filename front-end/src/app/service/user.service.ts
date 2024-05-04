import {Injectable} from "@angular/core";
import {BaseService} from "../share/services/base.service";
import {HttpClient} from "@angular/common/http";
import {Environment} from "../share/environment/environment";

@Injectable(
  {
    providedIn: 'root'
  }
)
export class UserService extends BaseService {
  constructor(private http: HttpClient) {
    super(http);
  }

  get SERVICE_URL(): string {
    return `${Environment.baseUrl}/api/users`;
  }

  checkExistUsernames(username: string) {
    return this.getRequest(`${this.SERVICE_URL}/exist-username`, {
      params: this.buildParams({username})
    });
  }

  checkExistEmails(email: string) {
    return this.getRequest(`${this.SERVICE_URL}/exist-email`, {
      params: this.buildParams({email})
    });
  }

  checkExistEmailsInProject(projectId: string, email: string) {
    return this.getRequest(`${this.SERVICE_URL}/exist-email/${projectId}`, {
      params: this.buildParams({email})
    });
  }

  getUserById(id: string) {
    return this.get(id);
  }
}
