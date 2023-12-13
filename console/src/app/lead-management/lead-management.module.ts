import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { LeadManagementRoutingModule } from './lead-management-routing.module';
import { LeadManagementComponent } from './lead-management.component';
import { AddEditLeadComponent } from './dialogs/add-edit-lead/add-edit-lead.component';
import { DeleteDialogComponent } from './dialogs/delete/delete.component';

import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatButtonModule } from '@angular/material/button';
import { MatRadioModule } from '@angular/material/radio';
import { MatIconModule } from '@angular/material/icon';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatSelectModule } from '@angular/material/select';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatCardModule } from '@angular/material/card';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatDialogModule } from '@angular/material/dialog';
import { MatSortModule } from '@angular/material/sort';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatMenuModule } from '@angular/material/menu';
import { LeadManagementService } from './lead-management.service';
import { NgxDatatableModule } from '@swimlane/ngx-datatable';
import { ProjectManagementService } from '../project-management/project-management.service';
import { MessageService } from '../core/service/message.service';
import { ViewLeadComponent } from './view-lead/view-lead.component';
import { SharedModule } from '../shared/shared.module';
import { MatTabsModule } from '@angular/material/tabs';
import { NgxMatFileInputModule } from '@angular-material-components/file-input';
import { NgxSpinnerModule } from 'ngx-spinner';
import { NgSelectModule } from '@ng-select/ng-select';
import { NgxDaterangepickerMd } from 'ngx-daterangepicker-material';
import { DeactivateLeadComponent } from './dialogs/deactivate-lead/deactivate-lead.component';
import { FollowupCommentComponent } from './dialogs/followup-comment/followup-comment.component';
import { ProjectChangeComponent } from './dialogs/project-change/project-change.component';
import { UserManagementService } from '../user-management/user-management.service';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatChipsModule } from '@angular/material/chips';
import { FollowupCreateComponent } from './dialogs/followup-create/followup-create.component';
import { AdvanceFilterComponent } from './dialogs/advance-filter/advance-filter.component';
import { DownloadReportComponent } from './dialogs/download-report/download-report.component';

@NgModule({
  declarations: [
    LeadManagementComponent,
    AddEditLeadComponent,
    DeleteDialogComponent,
    ViewLeadComponent,
    DeactivateLeadComponent,
    FollowupCommentComponent,
    ProjectChangeComponent,
    FollowupCreateComponent,
    AdvanceFilterComponent,
    DownloadReportComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    LeadManagementRoutingModule,
    MatTableModule,
    MatPaginatorModule,
    MatFormFieldModule,
    MatInputModule,
    MatSnackBarModule,
    MatButtonModule,
    MatIconModule,
    MatRadioModule,
    MatSelectModule,
    MatCheckboxModule,
    MatCardModule,
    MatDatepickerModule,
    MatDialogModule,
    MatSortModule,
    MatToolbarModule,
    MatMenuModule,
    MatTableModule,
    MatTabsModule,
    MatProgressSpinnerModule,
    NgxDatatableModule,
    SharedModule,
    NgxMatFileInputModule,
    NgxSpinnerModule,
    NgSelectModule,
    MatExpansionModule,
    MatChipsModule,

    NgxDaterangepickerMd.forRoot({
      separator: ' to ',
      clearLabel: ''
    })
  ],
  providers: [LeadManagementService, ProjectManagementService, MessageService, UserManagementService]
})
export class LeadManagementModule { }
