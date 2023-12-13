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
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatSortModule } from '@angular/material/sort';
import { MatTableModule } from '@angular/material/table';
import { MatToolbarModule } from '@angular/material/toolbar';
import { NgxDatatableModule } from '@swimlane/ngx-datatable';
import { AgGridModule } from 'ag-grid-angular';
import { NgxDaterangepickerMd } from 'ngx-daterangepicker-material';
import { MessageService } from '../core/service/message.service';
import { AddTargetComponent } from './add-target/add-target.component';
import { TargetsRoutingModule } from './targets-routing.module';
import { TargetsComponent } from './targets.component';
import { TargetsService } from './targets.service';
import{DeleteTargetDialogComponent} from './delete/delete.component'
import { NgSelectModule } from '@ng-select/ng-select';


@NgModule({
  declarations: [
    TargetsComponent,
    AddTargetComponent,
    DeleteTargetDialogComponent
  ],
  imports: [
    TargetsRoutingModule,
    CommonModule,
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
    MatSlideToggleModule,
    MatExpansionModule,
    NgSelectModule,
    NgxDaterangepickerMd.forRoot(),
    AgGridModule
  ],
  providers: [TargetsService, MessageService]
})
export class TargetsModule { }
