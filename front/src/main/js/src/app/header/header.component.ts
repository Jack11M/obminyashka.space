import { Component, OnInit, OnDestroy } from '@angular/core';
import {FormControl} from '@angular/forms';
import { ICategory } from '@app/_models/category';
import {CategoryService, AuthService, UserService} from '@app/_services'
import { Router } from '@angular/router';
import {NgModel} from '@angular/forms';
import { Observable, Subscription } from 'rxjs';
import { User } from '@app/_models';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit, OnDestroy {

  currentUser: User;
  currentUserSubscription: Subscription;
  currentUserfirstName: string;
  searchForName = '';
  listCategory: Observable<Array<ICategory>>;
  matMenuTriggerData: ICategory;

  constructor(private authenticationService: AuthService,
              private userService: UserService,
              private categoryService: CategoryService,
              private router: Router) {
                this.currentUserSubscription = this.authenticationService.currentUser.subscribe(user => {
                  this.currentUser = user;
                  this.currentUserfirstName = user ? user.firstName : '';
              });
  }

  ngOnInit() {
    this.getCategories();
  }

  getCategories(): void {
    this.listCategory = this.categoryService.getCategories();
    // this.categoryService.getCategories()
    //     .subscribe(categories => this.ListCategory = categories);
  }

  onSubmitSearch(): void {
    this.openSearchResults({ queryParams: { name: this.searchForName}});
  }

  searchInCategory(item: ICategory): void{
    this.openSearchResults({ queryParams: { category: item.id }});
  }

  openSearchResults(params): void{
    this.router.navigate(['/search'], params);
  }

  ngOnDestroy() {
    // unsubscribe to ensure no memory leaks
    this.currentUserSubscription.unsubscribe();
  }

  logout() {
    this.authenticationService.logout().subscribe(x => {
        this.router.navigate(['/login']);
      }
    );
  }
}
