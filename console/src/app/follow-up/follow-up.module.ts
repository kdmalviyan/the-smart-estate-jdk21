import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgxMaskModule } from 'ngx-mask';
import { SharedModule } from '../shared/shared.module';
import { FollowupRoutingModule } from './follow-up-routing.module';
import { FollowUpComponent } from './follow-up.component';
import { FollowUpService } from './follow-up.service';
import { NgxMatFileInputModule } from '@angular-material-components/file-input';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatMenuModule } from '@angular/material/menu';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatRadioModule } from '@angular/material/radio';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatSortModule } from '@angular/material/sort';
import { MatTableModule } from '@angular/material/table';
import { MatTabsModule } from '@angular/material/tabs';
import { MatToolbarModule } from '@angular/material/toolbar';
import { NgSelectModule } from '@ng-select/ng-select';
import { NgxDatatableModule } from '@swimlane/ngx-datatable';
import { NgxSpinnerModule } from 'ngx-spinner';
import { MessageService } from '../core/service/message.service';
import { ViewFollowupComponent } from './view-followup/view-followup.component';
import { LeadManagementService } from '../lead-management/lead-management.service';
import { LocaleService } from 'ngx-daterangepicker-material';
import { NgxDaterangepickerMd } from 'ngx-daterangepicker-material';

@NgModule({
  declarations: [
    FollowUpComponent,
    ViewFollowupComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    FollowupRoutingModule,
    SharedModule,
    NgxMaskModule.forRoot(),
    CommonModule,
    FormsModule,

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
    NgxDaterangepickerMd.forRoot({
      separator: ' to ',
      clearLabel: '', // detault is 'Clear'
    })
  ],
  providers: [FollowUpService, MessageService, LeadManagementService, LocaleService]
})
export class FollowUpModule { }
