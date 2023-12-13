import { NgxMatFileInputModule } from '@angular-material-components/file-input';
import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatDialogModule } from '@angular/material/dialog';
import { NgxDatatableModule } from '@swimlane/ngx-datatable';
import { MessageService } from '../core/service/message.service';
import { LeadManagementService } from '../lead-management/lead-management.service';
import { ProjectManagementService } from '../project-management/project-management.service';
import { SharedModule } from '../shared/shared.module';
import { UserManagementService } from '../user-management/user-management.service';
import { AddRawLeadComponent } from './add-raw-lead/add-raw-lead.component';
import { RawLeadManagementRoutingModule } from './raw-lead-management-routing.module';
import { RawLeadManagementComponent } from './raw-lead-management.component';
import { NgxDaterangepickerMd } from 'ngx-daterangepicker-material';


@NgModule({
  declarations: [
    RawLeadManagementComponent,
    AddRawLeadComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    NgxDatatableModule,
    RawLeadManagementRoutingModule,
    SharedModule,
    MatDialogModule,
    NgxMatFileInputModule,
    NgxDaterangepickerMd.forRoot({
      separator: ' to ',
      clearLabel: ''
    })
  ],
  providers: [LeadManagementService, ProjectManagementService, MessageService, UserManagementService]

})
export class RawLeadManagementModule { }
