import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, Observer } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LocationService {
  FILE_NAME = '../../assets/districts.csv';

  private locationData = {};

  constructor(private http: HttpClient) {}

  getLocationData() {
    return this.http.get(this.FILE_NAME, {responseType: 'text'});
  }

  fillLocationData(data: string) {
    const arrStrings = data.split('\n');
    const arrRegions = [];
    arrStrings.forEach( elem => {
      const arrCurrElem = elem.split(';');
      if (arrCurrElem.length === 0) {
        return;
      }
      if ((arrCurrElem[0].length > 1) && (!(arrCurrElem[0] in this.locationData))) {
        this.locationData[arrCurrElem[0]] = [];
      }
      if ((arrCurrElem.length === 2) && (arrCurrElem[1].length > 1)) {
          this.locationData[arrCurrElem[0]].push(arrCurrElem[1]);
      }
    });
    return this.locationData;
  }

  getLocations() {
    return new Observable(observer => {
      this.getLocationData().subscribe(data =>
        observer.next(this.fillLocationData(data)));
    });
  }

  getLocations2(region1) {
    return new Observable(observer => {
      if (Object.keys(this.locationData).length === 0) {
        this.getLocationData().subscribe(data =>
          observer.next(this.getRegions2(this.fillLocationData(data), region1)));
        } else {
          observer.next(this.getRegions2(this.locationData, region1));
        }
    });
  }

  getRegions2(data, region1) {
    if (region1 in data) {
      return data[region1];
    } else {
      return [];
    }
  }
}
