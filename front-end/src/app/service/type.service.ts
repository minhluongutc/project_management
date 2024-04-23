import {BaseService} from "../share/services/base.service";
import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Environment} from "../share/environment/environment";

@Injectable({
    providedIn: 'root'
})
export class TypeService extends BaseService {
  constructor(private http: HttpClient) {
    super(http);
  }

  get SERVICE_URL(): string {
    return `${Environment.baseUrl}/api/types`;
  }

  getTypes(queryParams: Record<string, any>) {
    return this.getRequest(this.SERVICE_URL, {
      params: this.buildParams(queryParams)
    });
  }

  createType(data: any) {
    return this.postRequest(this.SERVICE_URL, data);
  }

  updateType(id: string, data: any) {
    return this.putRequest(`${this.SERVICE_URL}/${id}`, data);
  }

  deleteType(id: string) {
    return this.deleteRequest(`${this.SERVICE_URL}/${id}`);
  }
}
