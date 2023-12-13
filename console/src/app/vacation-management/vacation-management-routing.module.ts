import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { VacationManagementComponent } from './vacation-management.component';

const routes: Routes = [
  {
    path: '',
    component:VacationManagementComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class VacationManagementRoutingModule { 

}
