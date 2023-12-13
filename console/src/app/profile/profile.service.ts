import { Injectable } from '@angular/core';
import { HttpService } from '../core/service/http.service';

@Injectable({
  providedIn: 'root'
})
export class ProfileService {

  constructor(private httpService: HttpService) { }

  changePassword(data) {
    return this.httpService.put('user/password/change', data);
  }

  getUser(userName) {
    return this.httpService.get(`user/userName?userName=${userName}`);
  }

  uploadPhoto(data, userId) {
    return this.httpService.post(`user/uploadImage/${userId}`, data);
  }

}
