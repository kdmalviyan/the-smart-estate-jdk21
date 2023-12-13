import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpService } from '../core/service/http.service';

@Injectable({
  providedIn: 'root'
})
export class FollowUpService {


  constructor(private httpService: HttpService, private http: HttpClient) { }

  
  addFollowup(id, data) {
    return this.httpService.post(`followup/${id}`, data);
  }

  getAllFollowup() {
    return this.httpService.get('followup');
  }

  updateFollowup(data,id) {
    return this.httpService.put(`mobile/followup/${id}`, data);
  }

  getPageableFollowups(pageParams) {
    const url = environment.apiUrl + "/followup/pageable";
    return this.http.post(url, pageParams);
  }
}
