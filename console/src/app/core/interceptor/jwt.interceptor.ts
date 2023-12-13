import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HttpHeaders,
  HttpErrorResponse,
  HttpResponse,
} from '@angular/common/http';
import { BehaviorSubject, Observable, throwError } from 'rxjs';
import { retry, catchError, tap, map, switchMap, filter, take } from 'rxjs/operators';
import { AuthService } from '../service/auth.service';
import { MessageService } from '../service/message.service';
import { Router } from '@angular/router';
import { LoaderService } from '../service/loader.service';
import { StorageService } from '../service/storage.service';

@Injectable()
export class JwtInterceptor implements HttpInterceptor {
  private isRefreshing = false;
  private refreshTokenSubject: BehaviorSubject<any> = new BehaviorSubject<any>(null);
  constructor(

    private authenticationService: AuthService,
    private messageService: MessageService,
    private storage: StorageService,

    private router: Router,
    private loaderService: LoaderService,

  ) { }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

    let currentUser = this.authenticationService.getcurrentUserValue();
    let authReq = request
    let tenantId =localStorage.getItem('tenant');
    let token = localStorage.getItem('token');
    if (!authReq.url.substring(authReq.url.indexOf("8080") + 4, authReq.url.length).startsWith("/team/users")) {
      this.loaderService.isLoading.next(true);
    }
    if (token && currentUser.tenantId) {
      authReq = request.clone({
        headers: new HttpHeaders({
          'Authorization': `Bearer ${token}`,
          'X-Tenant': `${currentUser.tenantId}`
        })
      })
    }
    else if (currentUser && currentUser.tenantId) {
      authReq = request.clone({
        headers: new HttpHeaders({
          'X-Tenant': `${currentUser.tenantId}`
        })
      });

    }
    else {
      authReq = request.clone({
        headers: new HttpHeaders({
          'Content-Type': 'application/json',
          'X-Tenant': tenantId
        })
      });
    }

    return next.handle(authReq).pipe(map(event => {
      if (event instanceof HttpResponse) {
        this.loaderService.isLoading.next(false);
      }
      return event;
    }), catchError((error: HttpErrorResponse) => {
      this.loaderService.isLoading.next(false);
      let errorMessage = '';
      if (error.error instanceof ErrorEvent) {
        // client-side error
        errorMessage = `Error: ${error.error.message}`;
      } else {
        // server-side error
        errorMessage = `Error Code: ${error.status}\nMessage: ${error.message}`;
      }
      if (error.status === 401 || error.status === 403) {
        return this.handle401Error(authReq, next);
      }
      
      else if (error.status === 0) {
        this.storage.clearStorage();
        this.router.navigate(['/authentication/signin', { status: error.status }]);
        this.messageService.showError('Please check if you backend Service is running')
      } else {
        if (error.error.message == null) {
          this.messageService.showError(error.error)
        } else {
          this.messageService.showError(error.error.message)
        }
      }
      return throwError(() => new Error(error.error.message));
    })
    );

  };
  /* Refresh Token Code */
  private handle401Error(request: HttpRequest<any>, next: HttpHandler) {
    if (!this.isRefreshing) {
      this.isRefreshing = true;
      this.refreshTokenSubject.next(null);
      const newTokenRequestPayload = { username: localStorage.getItem('loggedInUserName'), refreshToken: JSON.parse(localStorage.getItem('refreshToken')) };
      let currentUser = this.authenticationService.getcurrentUserValue();
      if (newTokenRequestPayload.refreshToken) {
        return this.authenticationService.refreshToken(newTokenRequestPayload).pipe(
          switchMap((refreshTokenResponse: any) => {
            this.isRefreshing = false;
            localStorage.removeItem('token');
            localStorage.setItem('token', refreshTokenResponse.token);
            localStorage.removeItem('refreshToken');
            localStorage.setItem('refreshToken', JSON.stringify(refreshTokenResponse.refreshToken));
            this.refreshTokenSubject.next(refreshTokenResponse.token);

            return next.handle((request.clone({
              headers: new HttpHeaders({
                'Authorization': `Bearer ${localStorage.getItem('token')}`,
                'X-Tenant': `${currentUser.tenantId}`
              })
            })));

          }),
          catchError((err) => {
            
            this.messageService.showError('Session Expired!')
            this.isRefreshing = false;
            this.storage.clearStorage();
            this.router.navigate(['/authentication/signin']);
           
            return throwError(() => new Error(err.error.message));
          })
        )
      }
      else {
        this.storage.clearStorage();
        this.router.navigate(['/authentication/signin']);
      }
    }
   
  }

}
