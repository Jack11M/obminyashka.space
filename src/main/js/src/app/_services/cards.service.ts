import { Injectable, OnInit } from '@angular/core';
import { ICard } from '../_models/card';
import { Observable, BehaviorSubject, from, Subject, Subscription } from 'rxjs';

import { AngularFireDatabase } from '@angular/fire/database';

import { timer } from 'rxjs/observable/timer';
import { AddToFavouritesService } from './add-to-favourites.service';

@Injectable({
  providedIn: 'root'
})
export class CardsService {
  private NUMBER_RANDOM_ELEMENTS: number;
  private REFRESH_INTERVAL: number;

  private basePath = 'cards';

  private cardsCache: [ICard];
  private randomCards$: BehaviorSubject<ICard[]> = new BehaviorSubject<ICard[]>([]);
  private randomCardsObservable$: Observable<ICard[]> = this.randomCards$.asObservable();

  private stopRefresh: boolean;
  private timer: Subscription;

  constructor(private db: AngularFireDatabase, private addToFavouritesService: AddToFavouritesService) { }


  cards(numberRandomElements: number, refreshInterval: number): Observable<Array<ICard>> {
    if (!this.cardsCache) {
      this.NUMBER_RANDOM_ELEMENTS = numberRandomElements;
      this.REFRESH_INTERVAL = refreshInterval;
      this.requestCards().subscribe(data => this.startRefreshRandomCards(data));
    }
    return this.randomCardsObservable$;
  }

  private startRefreshRandomCards(data) {
    if (!this.cardsCache) {
      this.cardsCache = data;
      this.timer = timer(0, this.REFRESH_INTERVAL).subscribe(_ => this.updateRandomCards());
    }
  }

  updateRandomCards() {
    if (this.stopRefresh) {
      return;
    }
    this.randomCards$.next(this.shuffle(this.cardsCache, this.NUMBER_RANDOM_ELEMENTS));
  }

  shuffle(a, numberElem) {
    for (let i = a.length - 1; i > 0; i--) {
        const j = Math.floor(Math.random() * (i + 1));
        [a[i], a[j]] = [a[j], a[i]];
    }
    return a.slice(0, numberElem);
  }

  private requestCards(): Observable<Array<any>> {
    return this.db.list(this.basePath).valueChanges();
  }

  addToFavourites(card: ICard, isFavourite: boolean): Observable<ICard> {
    this.stopRefresh = true;
    const subject = new Subject<ICard>();
    this.addToFavouritesService.addToFavourites(card, isFavourite).subscribe(res => {

        //refresh in cardsCache
        const cardCacheElem = this.cardsCache.find(x => x.id === card.id);
        cardCacheElem.isFavourite = isFavourite;

        setTimeout(x => {this.stopRefresh = false; }, 5000);

        subject.next(card);
      }
      );
    return subject;
  }
}
