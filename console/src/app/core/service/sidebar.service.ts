import { Injectable } from '@angular/core';
import { BehaviorSubject, Subject } from 'rxjs';

@Injectable()
export class SidebarService {

  private sidebarSubject = new Subject<boolean>();
  public sideBarObserv = this.sidebarSubject.asObservable();

  constructor() { }

  setSidebarState = (value: boolean) => {
    this.sidebarSubject.next(value);
  };
}
