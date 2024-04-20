import {BaseService} from "../share/services/base.service";
import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Environment} from "../share/environment/environment";

@Injectable({
  providedIn: 'root'
})
export class DocumentService extends BaseService {
  constructor(private http: HttpClient) {
    super(http);
  }

  get SERVICE_URL(): string {
    return `${Environment.baseUrl}/api/attachments`;
  }

  getAttachmentsByObjectId(objectId: string) {
    return this.getRequest(this.SERVICE_URL, {
      params: {"objectId": objectId}
    });
  }
}
