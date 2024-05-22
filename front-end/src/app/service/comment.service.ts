import {BaseService} from "../share/services/base.service";
import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Environment} from "../share/environment/environment";

@Injectable({
    providedIn: 'root'
})
export class CommentService extends BaseService {
  constructor(private http: HttpClient) {
    super(http);
  }

  get SERVICE_URL(): string {
    return `${Environment.baseUrl}/api`;
  }

  getComment(objectId: any) {
    return this.getRequest(`${this.SERVICE_URL}/${objectId}/comments`);
  }

  createComment(objectId: any, data: any) {
    return this.postRequest(`${this.SERVICE_URL}/${objectId}/comments`, data);
  }

  updateComment(objectId: any, commentId: any, data: any) {
    return this.putRequest(`${this.SERVICE_URL}/${objectId}/comments/${commentId}`, data);
  }

  deleteComment(objectId: any, commentId: any) {
    return this.delete(`${this.SERVICE_URL}/${objectId}/comments/${commentId}`);
  }
}
