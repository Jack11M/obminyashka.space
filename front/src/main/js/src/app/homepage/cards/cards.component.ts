import { CardsService } from './../../_services/cards.service';
import { Component, OnInit} from '@angular/core';
import { ICard } from '@app/_models/card';
import { Observable } from 'rxjs';
import { Router } from '@angular/router';
@Component({
  selector: 'app-cards',
  templateUrl: './cards.component.html',
  styleUrls: ['./cards.component.scss']
})

export class CardsComponent implements OnInit {

  listCards: Observable<Array<ICard>>;

  constructor(private cardsService: CardsService, private router: Router) { }

  ngOnInit(){
    this.listCards = this.cardsService.cards(8, 5000);
  }

  addToFavouritesAction(card: ICard): void {
    this.cardsService.addToFavourites(card, !card.isFavourite).subscribe(x => {});
  }

}
