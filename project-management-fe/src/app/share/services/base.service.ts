import {
  HttpClient,
  HttpHeaders,
  HttpParameterCodec,
  HttpParams,
} from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

export abstract class BaseService {
  protected constructor(private httpClient: HttpClient) {}

  abstract get SERVICE_URL(): string;

  public list(): Observable<any> {
    return this.getRequest(this.SERVICE_URL);
  }

  public get(id: string): Observable<any> {
    return this.getRequest(`${this.SERVICE_URL}/${id}`);
  }

  public saveOrUpdate(object: any): Observable<any> {
    if (object.id) {
      return this.postRequest(
        `${this.SERVICE_URL}/${object.id}`,
        JSON.stringify({ data: object })
      );
    } else {
      return this.putRequest(
        `${this.SERVICE_URL}`,
        JSON.stringify({ data: object })
      );
    }
  }

  public delete(id: string): Observable<any> {
    return this.httpClient.delete(`${this.SERVICE_URL}/${id}`);
  }

  public getRequest(url: string, options?: any): Observable<any> {
    return this.httpClient
      .get(url, options)
      .pipe(catchError((error) => this.handleError(error)));
  }

  /**
   * make post request
   */
  public postRequest(url: string, data?: any, options?: any): Observable<any> {
    return this.httpClient
      .post(url, data, options)
      .pipe(catchError((error) => this.handleError(error)));
  }

  /**
   * make put request
   */
  public putRequest(url: string, data?: any): Observable<any> {
    return this.httpClient
      .put(url, data)
      .pipe(catchError((error) => this.handleError(error)));
  }

  /**
   * make get request for file
   */
  public getRequestFile(url: string): Observable<any> {
    return this.httpClient
      .get(url, {
        responseType: 'blob',
        observe: 'response',
      })
      .pipe(catchError((error) => this.handleError(error)));
  }

  /**
   * make post request for file
   */
  public postRequestFile(url: string, data?: any): Observable<any> {
    return this.httpClient
      .post(url, data, { responseType: 'blob' })
      .pipe(catchError((error) => this.handleError(error)));
  }

  /**
   * make post request for file
   */
  public postRequestFile2(url: string, data?: any): Observable<any> {
    return this.httpClient
      .post(url, data, {
        responseType: 'blob',
        observe: 'response',
      })
      .pipe(catchError((error) => this.handleError(error)));
  }

  /**
   * make get request
   */
  public deleteRequest(url: string, data?: any): Observable<any> {
    const httpOptions = {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
      body: data,
    };
    return this.httpClient
      .delete(url, httpOptions)
      .pipe(catchError((error) => this.handleError(error)));
  }

  /**
   * make patch request
   */
  public patchRequest(url: string, data?: any): Observable<any> {
    return this.httpClient
      .patch(url, data)
      .pipe(catchError((error) => this.handleError(error)));
  }

  /**
   * handleError
   */
  protected handleError(error: any) {
    return throwError(error.error);
  }

  private noNotifyError: string[] = [''];

  private checkUrl(url: string): boolean {
    for (let bypassUrl of this.noNotifyError) {
      let byPass = url.indexOf(bypassUrl);
      if (byPass >= 0) return true;
    }
    return false;
  }

  private noNotifyErrorCode: number[] = [];

  private checkErrorCode(errorCode: number): boolean {
    return this.noNotifyErrorCode.indexOf(errorCode) >= 0;
  }

  buildHttpParams(
    params: HttpParams,
    paramName: string,
    paramValues: any
  ): HttpParams {
    if (!paramValues && paramValues !== false) {
      return params;
    }
    if (paramValues instanceof Array) {
      for (const item of paramValues) {
        params = params.append(paramName, item);
      }
    } else if (paramValues instanceof Object) {
      for (const key in paramValues) {
        let childParamValues = paramValues[key];
        if (childParamValues instanceof Object) {
          for (let childKey in childParamValues) {
            params = params.append(
              `${key}[${childKey}]`,
              childParamValues[childKey] ?? ''
            );
          }
        } else {
          params = params.append(key, paramValues[key] ?? '');
        }
      }
    } else {
      params = params.append(paramName, paramValues + '');
    }
    return params;
  }

  public buildParams(obj: any): HttpParams {
    return Object.entries(obj || {}).reduce((params, [key, value]) => {
      if (value === null) {
        return params.set(key, String(''));
      } else if (typeof value === typeof {}) {
        return params.set(key, JSON.stringify(value));
      } else {
        return params.set(key, String(value));
      }
    }, new HttpParams({ encoder: new CustomEncoder() }));
  }

  toQueryString(query?: Record<string, any>) {
    let result = '';

    if (query) {
      for (const key in query) {
        result += `&${key}=${query[key]}`;
      }

      if (result !== '') {
        return '?' + result.slice(1); // remove first '&'
      }
    }

    return result;
  }

  /**
   * convertData
   */
  public convertData(data: any): any {
    if (typeof data === typeof {}) {
      return this.convertDataObject(data);
    } else if (typeof data === typeof []) {
      return this.convertDataArray(data);
    } else if (typeof data === typeof true) {
      return this.convertBoolean(data);
    }
    return data;
  }

  /**
   * convertDataObject
   * param data
   */
  public convertDataObject(data: any): Object {
    if (data) {
      for (const key in data) {
        if (data[key] instanceof File) {
        } else {
          data[key] = this.convertData(data[key]);
        }
      }
    }
    return data;
  }

  public convertDataArray(data: Array<any>): Array<any> {
    if (data && data.length > 0) {
      for (const i in data) {
        data[i] = this.convertData(data[i]);
      }
    }
    return data;
  }

  public convertBoolean(value: Boolean): number {
    return value ? 1 : 0;
  }
}

class CustomEncoder implements HttpParameterCodec {
  encodeKey(key: string): string {
    return encodeURIComponent(key);
  }

  encodeValue(value: string): string {
    return encodeURIComponent(value);
  }

  decodeKey(key: string): string {
    return decodeURIComponent(key);
  }

  decodeValue(value: string): string {
    return decodeURIComponent(value);
  }
}
