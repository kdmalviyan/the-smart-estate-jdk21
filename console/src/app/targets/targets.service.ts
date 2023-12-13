import { Injectable } from '@angular/core';
import { HttpService } from '../core/service/http.service';

@Injectable({
  providedIn: 'root'
})
export class TargetsService {

  constructor(private http: HttpService) { }

  create(target) {
    return this.http.post('targets', target);
  }

  getAllTargets(groupBy) {
    return this.http.get(`targets/${groupBy}`)
  }

  deleteTarget(id) {
    return this.http.delete(`targets?id=${id}`)
  }


}
