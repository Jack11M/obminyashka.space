import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { ICard } from './card';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CardsService {
  private url = 'http://localhost:3000/cards';

  constructor(private http: HttpClient) { }

  loadCardsRandom(): Observable<ICard[]> {
    return this.http.get<ICard[]>(this.url);
  }

  addToFavorites(card: ICard): Observable<any> {
    const httpOptions = {
      headers: new HttpHeaders({'Content-Type': 'application/json'})
    };
    return this.http.put(`${this.url}/${card.id}`, card.isFavorite = !card.isFavorite, httpOptions);
  }
}
