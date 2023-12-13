import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { RawLeadManagementComponent } from './raw-lead-management.component';

const routes: Routes = [
  {
    path: '',
    component: RawLeadManagementComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class RawLeadManagementRoutingModule { }
