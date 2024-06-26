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
    return this.getRequest(this.SERVICE_URL, {
      params: this.buildParams({projectId})
    });
  }

  createPost(data: any) {
    return this.postRequest(this.SERVICE_URL, data);
  }

  updatePost(data: any) {
    return this.putRequest(this.SERVICE_URL, data);
  }

  deletePost(id: string) {
    return this.deleteRequest(this.SERVICE_URL, id);
  }

  getPost(id: any) {
    return this.get(id);
  }
}
