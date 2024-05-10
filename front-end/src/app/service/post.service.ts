import {BaseService} from "../share/services/base.service";
import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Environment} from "../share/environment/environment";

@Injectable({
    providedIn: 'root'
})
export class PostService extends BaseService {
  constructor(private http: HttpClient) {
    super(http);
  }

  get SERVICE_URL(): string {
    return `${Environment.baseUrl}/api/posts`;
  }

  getPosts(projectId: string | null) {
    const queryParams: Record<string, any> = {
      projectId: projectId
    }
    return this.getRequest(this.SERVICE_URL, {
      params: this.buildParams({})
    });
  }

  createPost(data: any) {
    return this.postRequest(this.SERVICE_URL, data);
  }
}
