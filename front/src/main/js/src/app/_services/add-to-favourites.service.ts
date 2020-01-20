import { Injectable } from '@angular/core';
import { ICard } from '@app/_models/card';
import { from, Observable } from 'rxjs';
import { AngularFireDatabase } from '@angular/fire/database';

@Injectable({
  providedIn: 'root'
})
export class AddToFavouritesService {
  private basePath = 'cards';

  constructor(private db: AngularFireDatabase) { }

  addToFavourites(card: ICard, isFavourite: boolean): Observable<ICard> {
    const path = this.basePath + '/' + card.id.toString();
    return from(this.db.database.ref(path).update({isFavourite: isFavourite}));
  }
}
