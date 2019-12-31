import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HomepageComponent } from './homepage/homepage.component';
import { StepsComponent } from './steps/steps.component';
import {CharityComponent} from '@app/homepage/charity/charity.component';
import {CardsComponent} from '@app/homepage/cards/cards.component';
import {BannersComponent} from '@app/homepage/banners/banners.component';
import { SharedModule } from '@app/shared/shared.module';
import { NguCarouselModule } from '@ngu/carousel';

@NgModule({
  declarations: [HomepageComponent, StepsComponent,
    CharityComponent, CardsComponent, BannersComponent],
  imports: [
    CommonModule,
    SharedModule,
    NguCarouselModule
  ],
  exports: [
    HomepageComponent
  ]
})
export class HomepageModule { }
