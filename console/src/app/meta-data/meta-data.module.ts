import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatDialogModule } from '@angular/material/dialog';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatMenuModule } from '@angular/material/menu';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatRadioModule } from '@angular/material/radio';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatSortModule } from '@angular/material/sort';
import { MatTableModule } from '@angular/material/table';
import { MatToolbarModule } from '@angular/material/toolbar';
import { NgxDatatableModule } from '@swimlane/ngx-datatable';
import { MessageService } from './../core/service/message.service';
import { LeadSourceComponent } from './lead-source/lead-source.component';
import { MetaDataRoutingModule } from './meta-data-routing.module';
import { MetaDataService } from './meta-data.service';

import { AddTabComponent } from './ui-tabs/add-tab/add-tab.component';
import { UiTabsComponent } from './ui-tabs/ui-tabs.component';
import { UIPermissionsComponent } from './uipermissions/uipermissions.component';
import { LeadStatusComponent } from './lead-status/lead-status.component';
import { LeadTypeComponent } from './lead-type/lead-type.component';
import { LeadInventoryComponent } from './lead-inventory/lead-inventory.component';
import { DeactivateReasonComponent } from './deactivate-reason/deactivate-reason.component';
import { ThirdPartyIntegrationComponent } from './third-party-integration/third-party-integration.component';
import { IntegrationFormDialogComponent } from './third-party-integration/integration-form-dialog/integration-form-dialog.component';
import { VacationTypeComponent } from './vacation-type/vacation-type.component';
import { VacationComponent } from './vacation/vacation.component';

@NgModule({
  declarations: [
    LeadSourceComponent,
    UIPermissionsComponent,
    UiTabsComponent,
    AddTabComponent,
    LeadStatusComponent,
    LeadTypeComponent,
    LeadInventoryComponent,
    DeactivateReasonComponent,
    ThirdPartyIntegrationComponent,
    IntegrationFormDialogComponent,
    VacationTypeComponent,
    VacationComponent
  ],
  imports: [
    CommonModule,
    MetaDataRoutingModule,
    MatTableModule,
    FormsModule,
    ReactiveFormsModule,
    NgxDatatableModule,
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
    MatExpansionModule,
  ],
  providers: [MetaDataService, MessageService]
})
export class MetaDataModule { }
