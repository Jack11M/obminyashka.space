import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { ICard } from '@app/_models/card';
import { CardsService } from '@app/_services';
import { AdvertisementService } from '@app/_services/advertisement.service';

@Component({
  selector: 'app-card-item',
  templateUrl: './card-item.component.html',
  styleUrls: ['./card-item.component.scss']
})
export class CardItemComponent implements OnInit {

  @Output() addToFavouritesAction = new EventEmitter<ICard>();

  //card: ICard;
  @Input() card: ICard;

  constructor(private cardsService: CardsService, private advertisementService: AdvertisementService) { }

  ngOnInit() {
  }

  addToFavourites(card: any) {
    this.addToFavouritesAction.emit(card as ICard);
  }


}
