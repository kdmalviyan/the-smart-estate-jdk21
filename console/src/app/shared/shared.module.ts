import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { NgxSpinnerModule } from 'ngx-spinner';

import { ValdemortModule } from 'ngx-valdemort';
import { DateService } from '../core/service/date-utils';
import { DateAgoPipe } from './Directives/DateAgoPipe';
import { DateDifference } from './Directives/DateDifference';
import { NumberToWordsPipe } from './Directives/NumberToWord';
import { OnlyNumber } from './Directives/OnlyNumber';
import { OnlyString } from './Directives/OnlyString';
import { FeatherIconsModule } from './feather-icons.module';
import { MaterialModule } from './material.module';

@NgModule({
  declarations: [
    OnlyNumber,
    OnlyString,
    DateAgoPipe,
    NumberToWordsPipe,
    DateDifference,
  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule,
    NgbModule,
    NgxSpinnerModule,
    FeatherIconsModule,
  ],
  exports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule,
    NgbModule,
    NgxSpinnerModule,
    MaterialModule,
    FeatherIconsModule,
    OnlyNumber,
    OnlyString,
    DateAgoPipe,
    DateDifference,
    NumberToWordsPipe,
    ValdemortModule,
  ],
  providers:[DateService]
})
export class SharedModule { }
