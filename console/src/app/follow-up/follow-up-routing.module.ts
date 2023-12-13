import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ViewLeadComponent } from '../lead-management/view-lead/view-lead.component';
import { FollowUpComponent } from './follow-up.component';

const routes: Routes = [
    {
        path: '',
        component: FollowUpComponent
    },
    {
        path: 'view',
        component: ViewLeadComponent
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class FollowupRoutingModule { }