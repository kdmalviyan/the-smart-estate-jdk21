export class ApiConfig {
    //Team Api's
    public static CREATE_TEAM: string = 'team';
    public static GET_ALL_TEAMS: string = 'team';
    public static GET_TEAM_BY_ID: string = 'team/';
    public static UPDATE_TEAM: string = 'team';
    public static GET_ALL_TEAMS_BY_PROJECT: string = 'team/project/';
    public static FIND_TEAM_BY_MEMBER: string = 'team/member/';
    public static ADD_TEAM_MEMBER: string = 'team/members/add/';
    public static REMOVE_TEAM_MEMBER: string = 'team/members/remove/';
    public static ASSIGN_TEAM_LEAD: string = 'team/members/lead/assign/';
    public static CHANGE_TEAM_LEAD: string = 'team/members/lead/change/';
    public static GET_USERS_BY_PROJECT_ID: string = 'team/users/'
}