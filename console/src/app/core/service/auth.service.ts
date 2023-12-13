import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, of, Subject } from 'rxjs';
import { User } from '../models/user';
import { HttpService } from './http.service';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private currentUserSubject: BehaviorSubject<User>;
  public currentUser: Observable<User>;
  private loggedInUser = new Subject();

  constructor(private httpService: HttpService) {
    this.currentUserSubject = new BehaviorSubject<User>(
      JSON.parse(localStorage.getItem('currentUser'))
    );
    this.currentUser = this.currentUserSubject.asObservable();
  }

   getcurrentUserValue(): User {
    return this.currentUserSubject.value;
  }

  setcurrentUserValue(user: any) {
    this.currentUserSubject.next(user);
  }

  
  setUser(user: any) {
    this.loggedInUser.next(user);
  }

  getUser() {
    return this.loggedInUser.asObservable();
  }

  login(userData) {
    return this.httpService.post('auth/token', userData);
  }
  refreshToken(body: any) {
    return this.httpService.post('auth/refreshtoken', body);
  }
  logout() {
    localStorage.clear();
    //this.currentUserSubject.next(null);
    return of({ success: false });
  }
}
