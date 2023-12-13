import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { AuthService } from '../core/service/auth.service';
import { HttpService } from './../core/service/http.service';

@Injectable({
  providedIn: 'root'
})
export class ProjectManagementService {

  editData;

  url = environment.apiUrl + '/'

  constructor(private httpService: HttpService, private http: HttpClient, private authService: AuthService) { }

  createProject(data) {
    return this.httpService.post('project', data);
  }

  getAllProjects() {
    return this.httpService.get('project');
  }

  getProjectById(id) {
    return this.httpService.get(`project/${id}`);
  }

  updateProject(data) {
    return this.httpService.put('project', data);
  }

  getAllProjectsWithMinimalDetails() {
    return this.httpService.get('project/minimalFields');
  }


  setEditData(data) {
    this.editData = data;
  }

  getEditData() {
    return this.editData;
  }
  downloadProjectFile(id) {
    return this.http.get(this.url + `project/file/download/${id}`, {
      responseType: 'blob'
    });
  }

  uploadFiles(id, data) {
    return this.httpService.post(`project/file/upload/${id}`, data);
  }

  deleteFile(projectId, fileId) {
    return this.httpService.get(`project/file/delete/${projectId}/${fileId}`);
  }

  //--------------------------inventory----------------------------------------

  addInventory(data, projectId) {
    return this.httpService.post(`inventory/${projectId}`, data);
  }

  uploadInventoryWithExcel(data: FormData,projectId) {
    const header = new HttpHeaders();
    header.append('Content-Type', 'file');
    const url = environment.apiUrl + `/uploadProjectInventory/${projectId}`;
    return this.http.post(url, data, { headers: header });
  }


}
