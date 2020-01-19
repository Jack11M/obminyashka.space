import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SearchResultsComponent } from './searchresults/searchresults.component';
import { SharedModule } from '@app/shared/shared.module';
import { routing } from '../app.routing';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import {MatAutocompleteModule} from '@angular/material/autocomplete';
import { MatFormFieldModule } from '@angular/material/form-field';
import {MatInputModule} from '@angular/material';
import { RouterModule } from '@angular/router';

@NgModule({
  declarations: [SearchResultsComponent],
  imports: [
    CommonModule,
    SharedModule,
    FormsModule,
    ReactiveFormsModule,
    MatAutocompleteModule,
    MatFormFieldModule,
    MatInputModule,
    RouterModule,
    routing
  ]
})
export class SearchResultsModule { }
