import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { AddEditProjectComponent } from './add-edit-project/add-edit-project.component';

import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { MatButtonModule } from '@angular/material/button';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatRadioModule } from '@angular/material/radio';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatTableModule } from '@angular/material/table';
import { NgxDatatableModule } from '@swimlane/ngx-datatable';


import { NgxMatFileInputModule } from '@angular-material-components/file-input';
import { MatCardModule } from '@angular/material/card';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatDialogModule } from '@angular/material/dialog';
import { MatMenuModule } from '@angular/material/menu';
import { MatSortModule } from '@angular/material/sort';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatTooltipModule } from '@angular/material/tooltip';
import { NgxMaskModule } from 'ngx-mask';
import { SharedModule } from '../shared/shared.module';
import { ProjectFilesComponent } from './project-files/project-files.component';
import { ProjectManagementRoutingModule } from './project-management-routing.module';
import { ProjectManagementComponent } from './project-management.component';
import { ViewProjectComponent } from './view-project/view-project.component';
import { AddEditInventoryComponent } from './add-edit-inventory/add-edit-inventory.component';
import { AgGridModule } from 'ag-grid-angular';
import { UploadInventoryComponent } from './upload-inventory/upload-inventory.component';


@NgModule({
  declarations: [
    ProjectManagementComponent,
    AddEditProjectComponent,
    ViewProjectComponent,
    ProjectFilesComponent,
    AddEditInventoryComponent,
    UploadInventoryComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    NgxDatatableModule,
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
    MatTooltipModule,
    MatExpansionModule,
    NgxMatFileInputModule,
    ProjectManagementRoutingModule,
    SharedModule,
    NgxMaskModule.forRoot(),
    AgGridModule
  ]
})
export class ProjectManagementModule { }
