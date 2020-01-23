import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';

import { AppComponent } from './app.component';
import { routing } from './app.routing';

import { AlertComponent } from './_components';
import { JwtInterceptor, ErrorInterceptor } from './_helpers';
import { LoginComponent } from './login/login.component';

import { HeaderComponent } from './header/header.component';
import { FooterComponent } from './footer/footer.component';
import {HomepageModule} from '@app/homepage/homepage.module';
import {AddProductModule} from '@app/addproduct/addproduct.module';
import {CardsService} from '@app/_services';
import {LocationService} from '@app/_services/location.service';
import {ProductModule} from '@app/product/product.module';
import {SearchResultsModule} from '@app/searchresults/searchresults.module';
import { SharedModule } from './shared/shared.module';

import { AngularFireModule } from '@angular/fire';
import { AngularFireDatabaseModule } from '@angular/fire/database';
import { environment } from '../environments/environment';
import { AuthGuard } from './_guards';
import { AngularFireAuth } from '@angular/fire/auth';
import { AddToFavouritesService } from './_services/add-to-favourites.service';
import { ProfileModule } from './profile/profile.module';

@NgModule({
    imports: [
        BrowserModule,
        BrowserAnimationsModule,
        FormsModule,
        ReactiveFormsModule,
        HttpClientModule,
        routing,
        HomepageModule,
        AddProductModule,
        ProductModule,
        SearchResultsModule,
        SharedModule,
        ProductModule,
        ProfileModule,
        AngularFireModule.initializeApp(environment.firebase),
        AngularFireDatabaseModule
    ],
    declarations: [
        AppComponent,
        AlertComponent,
        LoginComponent,
        HeaderComponent,
        FooterComponent
    ],
    providers: [
        { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true },
        { provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true },

        AuthGuard,
        // Custom
        CardsService,
        AddToFavouritesService,
        // TODO delete
        AngularFireAuth
    ],
    bootstrap: [AppComponent]
})

export class AppModule { }
