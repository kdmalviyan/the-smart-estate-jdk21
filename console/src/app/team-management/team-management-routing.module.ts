import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AddEditTeamComponent } from './add-edit-team/add-edit-team.component';
import { TeamManagementComponent } from './team-management/team-management.component';

const routes: Routes = [
    {
        path: '',
        component: TeamManagementComponent
    },
    {
        path: 'add-team',
        component: AddEditTeamComponent
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class TeamManagementRoutingModule { }