import {BaseService} from "../share/services/base.service";
import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Environment} from "../share/environment/environment";
import {Category} from "../models/category.model";

@Injectable({
  providedIn: 'root'
})
export class CategoryService extends BaseService {
  constructor(private http: HttpClient) {
    super(http);
  }

  get SERVICE_URL(): string {
    return `${Environment.baseUrl}/api/categories`;
  }

  getCategories(queryParams: Record<string, any>) {
    return this.getRequest(this.SERVICE_URL, {
      params: this.buildParams(queryParams)
    });
  }

  createCategory(data: Category) {
    return this.postRequest(this.SERVICE_URL, data);
  }

  updateCategory(id: string, data: Category) {
    return this.putRequest(`${this.SERVICE_URL}/${id}`, data);
  }

  deleteCategory(id: string) {
    return this.deleteRequest(`${this.SERVICE_URL}/${id}`);
  }
}
