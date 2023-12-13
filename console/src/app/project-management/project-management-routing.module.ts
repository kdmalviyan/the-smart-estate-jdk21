import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AddEditProjectComponent } from './add-edit-project/add-edit-project.component';
import { ProjectManagementComponent } from './project-management.component';

const routes: Routes = [
    {
        path: '',
        component: ProjectManagementComponent
    },
    {
        path: 'edit-project',
        component: AddEditProjectComponent
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class ProjectManagementRoutingModule { }