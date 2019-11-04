import { CardsService } from './../cards.service';
import { Component, OnInit} from '@angular/core';

@Component({
  selector: 'app-cards',
  templateUrl: './cards.component.html',
  styleUrls: ['./cards.component.scss']
})

export class CardsComponent implements OnInit {
  isFavorite = false;
  public cards = [];

  constructor(private cardsService: CardsService) { }

  public sampleMethodCall() {
    let that = this;
    setInterval(() => {
      that.getData(); 
    }, 5000); 
 }

  ngOnInit(){
    this.sampleMethodCall();
}

private getData(){
  this.cardsService.loadCardsRandom()
      .subscribe(data => this.cards = data.sort(function(){
        return Math.random()-0.5;
      }).slice(0,8));
}

  // ngOnInit(): any {
  //   this.cardsService.loadCardsRandom()
  //     .subscribe(data => this.cards = data.sort(function(){
  //       return Math.random()-0.5;
  //     }).slice(0,8));
  // }


  // addFavorite(): void {
  //   this.cardsService.addToFavorites(card.id).
  //   .subscribe(data => this.cards = data);

  // };

}
