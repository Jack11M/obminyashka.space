import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {MatMenuModule} from '@angular/material/menu';
import {MatTabsModule} from '@angular/material/tabs';
import {MatSelectModule} from '@angular/material/select';
import { CardItemComponent } from '@app/shared/card-item/card-item.component';

import { routing } from '../app.routing';

@NgModule({
  imports: [
    CommonModule,
    routing
  ],
  declarations: [
    CardItemComponent
  ],
  exports: [
    MatMenuModule,
    MatTabsModule,
    MatSelectModule,
    CardItemComponent
  ]
})
export class SharedModule { }
