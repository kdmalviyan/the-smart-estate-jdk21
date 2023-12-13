import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { HttpService } from '../core/service/http.service';

@Injectable()
export class UserManagementService {
  constructor(private httpService: HttpService) {
  }

  dialogData: any;

  /** CRUD METHODS */

  getAllUsers() {
    return this.httpService.get("user");
  }

  createUser(userData: any) {
    return this.httpService.post("user", userData);
  }

  update(userData: any) {
    return this.httpService.put("user", userData);
  }

  getDialogData() {
    return this.dialogData;
  }
  
  getUsersByProject(projectId) {
    return this.httpService.get('team/users/'+ projectId);
  }
  
  changePassword(data) {
    return this.httpService.put('user/password/change', data);
  }

}
