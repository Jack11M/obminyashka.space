import { Routes, RouterModule } from '@angular/router';

import { HomepageComponent } from './homepage/homepage';
import { LoginComponent } from './login/login.component';
import { AddproductComponent} from '@app/addproduct/addproduct/addproduct.component';
import { AuthGuard } from './_guards';
import {ProductComponent} from '@app/product/product/product.component';
import {SearchResultsComponent} from '@app/searchresults/searchresults/searchresults.component';
import { ProfileComponent } from './profile/profile/profile.component';

const appRoutes: Routes = [
    { path: '', component: HomepageComponent, },
    { path: 'login', component: LoginComponent },
    { path: 'addproduct', component: AddproductComponent, canActivate: [AuthGuard] },
    { path: 'product', component: ProductComponent, canActivate: [AuthGuard] },
    { path: 'search', component: SearchResultsComponent},
    { path: 'profile', component: ProfileComponent, canActivate: [AuthGuard]},
    // otherwise redirect to home
    { path: '**', redirectTo: '' }
];

export const routing = RouterModule.forRoot(appRoutes);
