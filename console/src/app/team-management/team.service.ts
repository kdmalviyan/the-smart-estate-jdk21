import { Injectable } from '@angular/core';
import { HttpService } from '../core/service/http.service';
import { ApiConfig } from '../config/api.config';
import { forkJoin, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TeamService {

  constructor(private httpService: HttpService) { }

  editData;

  createTeam(data) {
    return this.httpService.post(ApiConfig.CREATE_TEAM, data);
  }

  getAllTeams() {
    return this.httpService.get(ApiConfig.GET_ALL_TEAMS);
  }

  updateTeam(data) {
    return this.httpService.put(ApiConfig.UPDATE_TEAM, data);
  }

  addTeamLead(teamId, leadId) {
    return this.httpService.put(ApiConfig.ASSIGN_TEAM_LEAD + teamId + '/' + leadId, null);
  }

  changeTeamLead(teamId, oldLeadId, newLeadId) {
    return this.httpService.put(ApiConfig.CHANGE_TEAM_LEAD + teamId + `/${oldLeadId}/${newLeadId}`, null);
  }

  getTeamById(id) {
    return this.httpService.get(ApiConfig.GET_TEAM_BY_ID + id);
  }

  addTeamMember(teamId, userId) {
    return this.httpService.put(ApiConfig.ADD_TEAM_MEMBER + `${teamId}/${userId}`, null);
  }

  removeTeamMember(teamId, userId) {
    return this.httpService.put(ApiConfig.REMOVE_TEAM_MEMBER + `${teamId}/${userId}`, null);
  }

  updateOnlyProject(teamId, oldProjectId, newProjectId) {
    return this.httpService.put(`team/${teamId}/project/${oldProjectId}/${newProjectId}`, null);
  }

  setEditData(data) {

    this.editData = data;
  }

  getEditData() {
    return this.editData;
  }

  getAllProjectsWithMinimalDetails() {
    return this.httpService.get('project/minimalFields');
  }

  getUsersByProject(projectId) {
    return this.httpService.get(ApiConfig.GET_USERS_BY_PROJECT_ID + projectId);
  }

  public requestDataFromMultipleSources(teamId, projectId): Observable<any[]> {
    let teamData = this.getTeamById(teamId);
    let projectData = this.getAllProjectsWithMinimalDetails();
    let users = this.getUsersByProject(projectId);
    return forkJoin([teamData, projectData, users]);
  }

  deactivateTeam(id) {
    return this.httpService.get(`team/deactivate/${id}`);
  }
}
