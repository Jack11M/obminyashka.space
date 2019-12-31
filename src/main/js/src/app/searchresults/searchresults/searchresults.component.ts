import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import {NgModel, FormGroup} from '@angular/forms';
import { Subscription, Observable } from 'rxjs';
import { ICard } from '@app/_models/card';
import { LocationService } from '@app/_services/location.service';

import {FormControl} from '@angular/forms';
import {map, startWith} from 'rxjs/operators';
import {MatInputModule} from '@angular/material/input';
import {MatAutocompleteModule} from '@angular/material/autocomplete';
import {MatFormFieldModule} from '@angular/material/form-field';
import { AdvertisementService } from '@app/_services/advertisement.service';
import { IFilter } from '@app/_models/filter';
import { CategoryService } from '@app/_services';

@Component({
  selector: 'app-searchresults',
  templateUrl: './searchresults.component.html',
  styleUrls: ['./searchresults.component.scss']
})
export class SearchResultsComponent implements OnInit, OnDestroy {

  filterFormGroupControl = new FormGroup({
    // region1Control : new FormControl(),
    // region2Control : new FormControl()
  });
  region1Control = new FormControl();
  region2Control = new FormControl();

  showPaggingControl = new FormControl('showPagging15');
  showSortControl = new FormControl('showSortNone');
  genderControlW = new FormControl();
  genderControlM = new FormControl();
  genderControlU = new FormControl();

  seasonControlU = new FormControl();
  seasonControlW = new FormControl();
  seasonControlS = new FormControl();

  ageControl1 = new FormControl();
  ageControl2 = new FormControl();
  ageControl3 = new FormControl();
  ageControl4 = new FormControl();
  ageControl5 = new FormControl();
  ageControl6 = new FormControl();

  sizeControl1 = new FormControl();
  sizeControl2 = new FormControl();
  sizeControl3 = new FormControl();
  sizeControl4 = new FormControl();
  sizeControl5 = new FormControl();
  sizeControl6 = new FormControl();

  name: string;
  category_id: number;
  filterDescription: string;
  paramsSubscription: Subscription;

  listCards: Observable<Array<ICard>>;

  filteredRegion1: Observable<string[]>;
  filteredRegion2: Observable<string[]>;

  constructor(private activatedRoute: ActivatedRoute,
    private router: Router,
    private locationService: LocationService,
    private advertisementService: AdvertisementService,
    private categoryService: CategoryService) { }

  ngOnInit() {
    this.paramsSubscription = this.activatedRoute.queryParams.subscribe(params => {
      this.name = params['name'];
      this.category_id = Number.parseInt(params['category'], 10);
      if (this.name) {
        this.filterDescription = this.name;
      } else {
        const currentCategory = this.categoryService.getCategoryById(this.category_id);
        if (currentCategory) {
          this.filterDescription = currentCategory.name;
        }
      }
      this.listCards = this.advertisementService.getAdvertisements(this.getCurrentFilter());
    });
    this.initLocationSelectors();
  }

  ngOnDestroy() {
    this.paramsSubscription.unsubscribe();
  }

  private _filter(value: string, list: string[]): string[] {
    const filterValue = value.toLowerCase();
    return list.filter(region => region.toLowerCase().indexOf(filterValue) === 0);
  }

  initLocationSelectors() {
    this.locationService.getLocations().subscribe( data => {
      const listRegions1 = [];
      Object.keys(data).forEach(function(key) {
        listRegions1.push(key);
      });
      this.initRegionControls(listRegions1);
    });
  }

  initRegionControls(listRegions1) {
    this.filteredRegion1 = this.region1Control.valueChanges.pipe(
      startWith(''),
      map(value => this._filter(value, listRegions1))
    );
  }

  checkFilter2(event) {
    this.region2Control.setValue('');
    this.locationService.getLocations2(event).subscribe( data => {
      this.initRegion2Controls(data);
    });
    this.updateFilter(event);
  }

  resetFilter1(){
    this.initLocationSelectors();
  }

  initRegion2Controls(listRegions2) {
    this.filteredRegion2 = this.region2Control.valueChanges.pipe(
      startWith(''),
      map(value => this._filter(value, listRegions2))
    );
  }

  updateFilter(event) {
    this.listCards = this.advertisementService.getAdvertisements(this.getCurrentFilter());
  }

  getCurrentFilter(): IFilter {
    return {
      category_id: this.category_id,
      name: this.name,
      filterGender : this.getFilterGender(),
      filterSeason : this.getFilterSeason(),
      filterLocation: this.region1Control.value,
      filterAge: this.getFilterAges(),
      filterSize: this.getFilterSizes(),
      sortByCreated: (this.showSortControl.value === 'showSortByDate'),
      pagging: this.getPagging()
    };
  }

  getFilterGender(): string[] {
    const result = [];
    if (this.genderControlW.value) {
      result.push('w');
    }
    if (this.genderControlM.value) {
      result.push('m');
    }
    if (this.genderControlU.value) {
      result.push('u');
    }
    return result;
  }
  getFilterSeason(): string[] {
    const result = [];
    if (this.seasonControlU.value) {
      result.push('u');
    }
    if (this.seasonControlS.value) {
      result.push('s');
    }
    if (this.seasonControlW.value) {
      result.push('w');
    }
    return result;
  }

  getFilterAges(): string[] {
    const result = [];
    if (this.ageControl1.value) {
      result.push('0');
    }
    if (this.ageControl2.value) {
      result.push('1-2');
    }
    if (this.ageControl3.value) {
      result.push('3-4');
    }
    if (this.ageControl4.value) {
      result.push('5-7');
    }
    if (this.ageControl5.value) {
      result.push('8-11');
    }
    if (this.ageControl6.value) {
      result.push('11-14');
    }
    return result;
  }

  getFilterSizes(): string[] {
    const result = [];
    if (this.sizeControl1.value) {
      result.push('50-80');
    }
    if (this.sizeControl2.value) {
      result.push('80-92');
    }
    if (this.sizeControl3.value) {
      result.push('92-104');
    }
    if (this.sizeControl4.value) {
      result.push('104-122');
    }
    if (this.sizeControl5.value) {
      result.push('122-146');
    }
    if (this.sizeControl6.value) {
      result.push('146-164');
    }
    return result;
  }

  getPagging() {
    let currentElementsOnPage = 0;
    const showPagging = this.showPaggingControl.value;
    if (showPagging === 'showPagging5') {
      currentElementsOnPage = 5;
    } else if (showPagging === 'showPagging10') {
      currentElementsOnPage = 10;
    } else if (showPagging === 'showPagging15') {
      currentElementsOnPage = 15;
    }
    return ({page: 0,
            elementsOnPage: currentElementsOnPage});
  }

  addToFavouritesAction(card: ICard) {
    this.advertisementService.addToFavourites(card, !card.isFavourite).subscribe(card => this.updateFilter(''));
  }
}
