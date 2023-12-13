import { Injectable } from '@angular/core';
import {
  Router,
  CanActivate,
  ActivatedRouteSnapshot,
  RouterStateSnapshot
} from '@angular/router';
import { url } from 'inspector';

import { AuthService } from '../service/auth.service';
import { MessageService } from '../service/message.service';
import { StorageService } from '../service/storage.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {
  metadata: any;

  constructor(private authService: AuthService, private router: Router,
    private storage: StorageService,
    private message: MessageService) {
    this.metadata = this.storage.getStorage();

  }


  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    const roles = this.metadata.roles;
    if (roles == undefined) {
      return true;
    }
    let currentRole = this.metadata.loggedInUser.roles[0].name;
    let currentUrl = state.url;
    const userPermissions = roles.filter(e => e.name == currentRole)[0];
    const urlAccess = userPermissions.uiPermissions.filter(e => currentUrl.includes(e.path))[0];

    if (urlAccess != null || urlAccess != undefined) {
      return true;
    }
    this.router.navigate(['/authentication/signin']);
    this.message.showError("Not Authorized")
    return false;
  }
}
