import { Injectable } from '@angular/core';
import { IAdvertisement } from '@app/_models/advertisement';

import { AngularFireDatabase } from '@angular/fire/database';
import { Observable, Subject } from 'rxjs';
import { ICard } from '@app/_models/card';
import { IFilter } from '@app/_models/filter';
import { CardsService } from './cards.service';
import { AddToFavouritesService } from './add-to-favourites.service';

@Injectable({
  providedIn: 'root'
})
export class AdvertisementService {

  private cardsCache: [IAdvertisement];
  private basePath = 'cards';

  constructor(private db: AngularFireDatabase, private addToFavouritesService: AddToFavouritesService) { }

  getAdvertisements(currentFilter: IFilter): Observable<Array<ICard>> {
    return new Observable(observer => {
      this.requestCards().subscribe(data =>
        observer.next(this.getAdvertisementsWithFilter(data, currentFilter)));
    });
  }

  private getAdvertisementsWithFilter(data, currentFilter: IFilter) {
    this.cardsCache = data;
    //apply filter
    let currentData = this.applyFilter(this.cardsCache, currentFilter);

    //sort
    if (currentFilter.sortByCreated) {
      currentData.sort(this.compareCreated);
    }
    //pagging
    if ((currentFilter.pagging.elementsOnPage > 0) || (currentFilter.pagging.page > 0)) {
      let currentElementsOnPage = currentFilter.pagging.elementsOnPage;
      if (currentElementsOnPage === 0) {
        currentElementsOnPage = 50;
      }
      let beginIndex = 0;
      if (currentFilter.pagging.page > 1) {
        beginIndex = (currentFilter.pagging.page - 1) * currentElementsOnPage;
      }
      currentData = currentData.splice(beginIndex, currentElementsOnPage);
    }

    //cast
    const result = [];
    for (let i = 0; i < currentData.length; i++){
      result.push(currentData[i] as ICard);
    }
    return result;
  }

  private requestCards(): Observable<Array<any>> {
    if (!this.cardsCache) {
      return this.db.list(this.basePath).valueChanges();
    } else {
      return new Observable(data =>
        data.next(this.cardsCache));
    }
  }

  private applyFilter(arr: [IAdvertisement], currentFilter: IFilter): IAdvertisement[] {
    const result: Array<IAdvertisement> = [];
    for (let i = 0; i < arr.length; i++) {
      if (this.matchesFilter(arr[i], currentFilter)) {
        result.push(arr[i]);
      }
    }
    return result;
  }

  private matchesFilter(elem: IAdvertisement, currentFilter: IFilter): boolean {
    if (!this.matchesCurrentFilter(elem, currentFilter, 'filterGender', 'gender' )) {
      return false;
    }
    if (!this.matchesCurrentFilter(elem, currentFilter, 'filterSeason', 'season' )) {
      return false;
    }

    if ((currentFilter.filterLocation) && (elem.location !== currentFilter.filterLocation)) {
      return false;
    }

    if (!this.matchesCurrentFilter(elem, currentFilter, 'filterAge', 'age' )) {
      return false;
    }

    if (!this.matchesCurrentFilter(elem, currentFilter, 'filterSize', 'size' )) {
      return false;
    }

    if (!(Number.isNaN(currentFilter.category_id)) && (elem.category !== currentFilter.category_id)) {
      return false;
    }

    if ((currentFilter.name) && (!(elem.title.toLowerCase().includes(currentFilter.name.toLowerCase())))) {
      return false;
    }

    return true;
  }

  matchesCurrentFilter(elem: IAdvertisement, currentFilter: IFilter, filterFieldName: string, elementFieldName: string ): boolean {
    if (currentFilter[filterFieldName].length === 0) {
      return true;
    }
    return (currentFilter[filterFieldName].some(element => element === elem[elementFieldName]));
  }

  compareCreated( a: IAdvertisement, b: IAdvertisement ) {
    if ( a.created < b.created ) {
      return -1;
    }
    if ( a.created > b.created ) {
      return 1;
    }
    return 0;
  }

  updateCardsCache(card: ICard, isFavourite: boolean){
    if (!this.cardsCache) {
      return;
    }
    const cardCacheElem = this.cardsCache.find(x => x.id === card.id);
    // if (cardCacheElem) {
      cardCacheElem.isFavourite = isFavourite;
    // }
  }

  addToFavourites(card: ICard, isFavourite: boolean): Subject<ICard> {
    const subject = new Subject<ICard>();
    this.addToFavouritesService.addToFavourites(card, isFavourite).subscribe(res => {
        //refresh in cardsCache
        const cardCacheElem = this.cardsCache.find(x => x.id === card.id);
        cardCacheElem.isFavourite = isFavourite;
        subject.next(card);
      }
      );
    return subject;
  }
}
