import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from './../../../environments/environment';
import { catchError, map, Observable, throwError } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class HttpService {

  errorMessage: string = 'Server Error';

  constructor(private httpClient: HttpClient) { }

  get(endpoint: string): Observable<any> {
    const url: string = this.createApiUrl(endpoint);
    return this.httpClient.get(url)
      .pipe(
        map((res: any) => res), catchError((err: any) => {
          return throwError(() => {
            return { error: err['error'] || this.errorMessage }
          });
        })); // errors if any;
  }

  getFile(endpoint: string): Observable<any> {
    const url: string = this.createApiUrl(endpoint);
    return this.httpClient.get(url, { responseType: 'blob' })
      .pipe(
        map((res: any) => <Blob>res.blob()), catchError((err: any) => {
          return throwError(() => {
            return { error: err['error'] || this.errorMessage }
          });
        })); // errors if any;
  }

  post(endpoint: string, obj: any): Observable<any> {
    const url: string = this.createApiUrl(endpoint);
    return this.httpClient.post(url, obj)
      .pipe(
        map((res: any) => res), catchError((err: any) => {
          return throwError(() => {
            return { error: err['error'] || this.errorMessage }
          });
        })); // errors if any;
  }

  put(endpoint: string, obj: any): Observable<any> {
    const url: string = this.createApiUrl(endpoint);
    return this.httpClient.put(url, obj)
      .pipe(
        map((res: any) => res), catchError((err: any) => {
          return throwError(() => {
            return { error: err['error'] || this.errorMessage }
          });
        })); // errors if any;
  }

  delete(endpoint: string): Observable<any> {
    const url: string = this.createApiUrl(endpoint);
    return this.httpClient.delete(url)
      .pipe(
        map((res: any) => res), catchError((err: any) => {
          return throwError(() => {
            return { error: err['error'] || this.errorMessage }
          });
        })); // errors if any;
  }

  private createApiUrl(apiPath: string) {
    return environment.apiUrl + '/' + apiPath;
  }
}
