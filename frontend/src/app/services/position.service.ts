import { Injectable } from '@angular/core';
import { Subject, BehaviorSubject } from 'rxjs';
import { Position } from '../mainComponents/user/company/company.component';
import { storage_Avaliable } from '../interfaces/storage_checker';

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
            actualPosition = {
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

  public isInside(circle_x: number, circle_y: number, rad: number, x: number, y: number) {
    // Compare radius of circle with distance  
    // of its center from given point 
    if ((x - circle_x) * (x - circle_x) +
      (y - circle_y) * (y - circle_y) <= rad * rad)
      return true;
    else
      return false;
  }

  public calculatePointDistance(lat1, lon1, lat2, lon2) {
    var p = 0.017453292519943295;    // Math.PI / 180
    var c = Math.cos;
    var a = 0.5 - c((lat2 - lat1) * p) / 2 +
      c(lat1 * p) * c(lat2 * p) *
      (1 - c((lon2 - lon1) * p)) / 2;

    return (12742 * Math.asin(Math.sqrt(a))) * 1000; // 2 * R; R = 6371 km    
  }

  public checkIfPointInsideCircle(circleRad, circleLat, circleLon, pointLat, pointLon) {
    let pointDistance = this.calculatePointDistance(circleLat, circleLon, pointLat, pointLon);

    if (pointDistance <= circleRad) {
      return true;
    } else {
      return false;
    }
  }
}
