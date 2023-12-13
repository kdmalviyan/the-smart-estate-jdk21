import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { LeadManagementComponent } from './lead-management.component';
import { ViewLeadComponent } from './view-lead/view-lead.component';

const routes: Routes = [
  {
    path: '',
    component: LeadManagementComponent
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
export class LeadManagementRoutingModule { }
