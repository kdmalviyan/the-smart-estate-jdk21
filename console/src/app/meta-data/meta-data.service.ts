import { Injectable } from '@angular/core';
import { HttpService } from '../core/service/http.service';

@Injectable({
  providedIn: 'root'
})
export class MetaDataService {

  constructor(private http: HttpService) { }
  permission: any;
  loadAllMetadata() {
    return this.http.get('metadata');
  }

  // Lead Source 
  createLeadSource(data: any) {
    return this.http.post('leadsource', data);
  }
  getAllLeadSource() {
    return this.http.get('leadsource')
  }
  getLeadSourceById(id) {
    return this.http.get('leadsource/${id}')
  }
  updateLeadSource(data) {
    return this.http.put('leadsource', data);
  }



  setPermission(permission) {
    this.permission = JSON.stringify(permission);
  }

  getPermission() {
    return this.permission;
  }

  // All Metadata Collection Of Lead
  getAllLead(collectionName: any) {
    return this.http.get(`${collectionName}`);
  }
  createLead(collectionName: string, body: any) {
    return this.http.post(`${collectionName}`, body);
  }
  updateLead(collectionName: string, body: any) {
    return this.http.put(`${collectionName}`, body);
  }


  // master fucntions for the metadata

  deactive(collectionName: string, id: number) {
    return this.http.delete(`${collectionName}?id=${id}`);
  }

  /*------------------------------------- UI Tabs------------------------------------- */

  getAllMenus() {
    return this.http.get('permissionTabs');
  }

  createMenu(body, parentModule) {
    return this.http.post(`permissionTabs?parentModule=${parentModule}`, body);
  }

  /*------------------------- Intergration ----------------------------------------*/

  createIntegration(data) {
    return this.http.post(`thirdparty/endpoint`, data);
  }

  getAllIntegrations() {
    return this.http.get('thirdparty/endpoint');
  }

  // All Metadata Collection of Vacation Type
  getVacation(collectionName: string) {
    return this.http.get(`${collectionName}`);
}
  createVacation(collectionName: string, body: any) {
    return this.http.post(`${collectionName}`, body);
  }
  updateVacation(collectionName: string, body: any) {
    return this.http.put(`${collectionName}`, body);
  }
  deleteVacation(collectionName:any,id:any) {
    return this.http.delete(`${collectionName}/${id}`)
  }
}
