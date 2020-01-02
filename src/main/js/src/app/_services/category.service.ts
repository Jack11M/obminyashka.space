import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';

import { ICategory } from '@app/_models/category';
import { AngularFireDatabase } from 'angularfire2/database';

@Injectable({
  providedIn: 'root'
})
export class CategoryService {
  cacheCategory: ICategory[];
  private basePath = 'category';
  constructor(private db: AngularFireDatabase) { }

  public getCategories(): Observable<ICategory[]> {
    return this.requestCategory();
  }

  public getCategoryById(id: number): ICategory {
    if (this.cacheCategory){
      return this.cacheCategory.find(elem => elem.id === id);
    }
  }

  private requestCategory(): Observable<Array<any>> {
    if (!this.cacheCategory) {
      const result = this.db.list(this.basePath).valueChanges();
      result.subscribe(data => this.cacheCategory = data as [ICategory]);
      return result;
    } else {
      return new Observable(data =>
        data.next(this.cacheCategory));
    }
  }
}
