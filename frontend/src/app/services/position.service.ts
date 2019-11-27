import { Injectable } from '@angular/core';
import { Subject, BehaviorSubject } from 'rxjs';
import { Position } from '../user/company/company.component';
import { storage_Avaliable } from '../classes/storage_checker';

@Injectable({
  providedIn: 'root'
})
export class PositionService {
  constructor() { }

  public getActualPosition() {
    let subject = new BehaviorSubject<Position>(null);
    let navigatorObject = window.navigator;
    
    if (storage_Avaliable('localStorage')) {
      if (!localStorage.getItem('actualPosition')) {
        let actualPosition = {};
        navigatorObject.geolocation.getCurrentPosition(
          position => {
            actualPosition = {
              latitude: position.coords.latitude,
              longitude: position.coords.longitude
            };
            localStorage.setItem(
              'actualPosition',
              JSON.stringify(actualPosition)
            );

             actualPosition = JSON.parse(localStorage.getItem('actualPosition'));
             subject.next(actualPosition);
          },
          error => {
            actualPosition ={
              latitude: 51.246452,
              longitude: 22.568445
            };
            subject.next(actualPosition);
          }
        );
      } else {
        let position = JSON.parse(localStorage.getItem('actualPosition'));
        subject.next(position);
      }
    } else {
      let actualPosition = {
        latitude: 51.246452,
        longitude: 22.568445
      };
      subject.next(actualPosition);
    }

    return subject;
  }

  public isInside(circle_x:number,circle_y:number,rad:number,x:number,y:number) 
  { 
      // Compare radius of circle with distance  
      // of its center from given point 
      if ((x - circle_x) * (x - circle_x) + 
      (y - circle_y) * (y - circle_y) <= rad * rad) 
      return true; 
      else
      return false; 
  } 

  public setCircleRadius(lat1, lon1, lat2, lon2) {
    var R = 6378.137; // Radius of earth in KM
    var dLat = lat2 * Math.PI / 180 - lat1 * Math.PI / 180;
    var dLon = lon2 * Math.PI / 180 - lon1 * Math.PI / 180;
    var a = Math.sin(dLat/2) * Math.sin(dLat/2) +
    Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180) *
    Math.sin(dLon/2) * Math.sin(dLon/2);
    var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
    var d = R * c;
    return d * 1000; // meters
}
}
