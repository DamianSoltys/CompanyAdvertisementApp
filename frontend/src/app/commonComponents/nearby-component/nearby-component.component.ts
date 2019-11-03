import { Component, OnInit, Input } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { storage_Avaliable } from 'src/app/classes/storage_checker';
import { Position, Marker } from 'src/app/user/company/company.component';
import { SearchService } from 'src/app/services/search.service';
import { positionElements } from '@ng-bootstrap/ng-bootstrap/util/positioning';

@Component({
  selector: 'app-nearby-component',
  templateUrl: './nearby-component.component.html',
  styleUrls: ['./nearby-component.component.scss']
})
export class NearbyComponent implements OnInit {
  @Input() name;
  public actualPosition: Position = {
    latitude: 51.246452,
    longitude: 22.568445
  };
  public markersArray:Marker[];
  constructor(public activeModal: NgbActiveModal,private sDataService:SearchService) { }

  ngOnInit() {
    this.getActualPosition();
  }

  private getActualPosition() {
    let navigatorObject = window.navigator;

    if (storage_Avaliable('localStorage')) {
      if (!localStorage.getItem('actualPosition') && !this.actualPosition) {
        navigatorObject.geolocation.getCurrentPosition(
          position => {
            this.actualPosition = {
              latitude: position.coords.latitude,
              longitude: position.coords.longitude
            };
            localStorage.setItem(
              'actualPosition',
              JSON.stringify(this.actualPosition)
            );
          },
          error => {}
        );
      } else {
        let position = JSON.parse(localStorage.getItem('actualPosition'));

        if (position) {
          this.actualPosition = {
            latitude: position.latitude,
            longitude: position.longitude
          };
        }
      }
    }
  }

  public getNearbyBranches() {
  }
}
