import { Component, OnInit, Input } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { storage_Avaliable } from 'src/app/classes/storage_checker';
import { Position, Marker } from 'src/app/user/company/company.component';
import { SearchService } from 'src/app/services/search.service';
import { positionElements } from '@ng-bootstrap/ng-bootstrap/util/positioning';
import { BranchService } from 'src/app/services/branch.service';
import { Branch } from 'src/app/classes/Company';
import { Router } from '@angular/router';

export interface NearbyMarker extends Marker {
  branchId?:number,
  companyId?:number
}
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
  public markersArray:NearbyMarker[];
  public branches:Branch[];
  constructor(public activeModal: NgbActiveModal,private sDataService:SearchService,private bDataService:BranchService,private router:Router) { }

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
    this.getNearbyBranches();
  }

  public getNearbyBranches() {
    this.bDataService.getBranches().subscribe(response=>{
      this.branches = <Branch[]>response.body['content'];
      console.log(this.branches)
      this.getMarkerData();    
    },error=>{
      console.log(error);
    })
  }
  public goToBranchProfile(branchId:number,companyId:number) {
    console.log(branchId);
    console.log(companyId)
    this.router.navigate(['/home']).then(()=>{
      this.router.navigate(['/branchProfile',companyId,branchId]);
    });
  }

  public getMarkerData() {
    this.branches.forEach(branch=>{
      if(Number(branch.geoX) > this.actualPosition.latitude - 0.05 && Number(branch.geoX) < this.actualPosition.latitude + 0.05) {
        if(Number(branch.geoY) > this.actualPosition.longitude - 0.05 && Number(branch.geoY) < this.actualPosition.longitude + 0.05) {
          
          let marker:NearbyMarker = {
            latitude:Number(branch.geoX),
            longitude:Number(branch.geoY),
            label:branch.name,
            branchId:branch.branchId,
            companyId:branch.companyId,
          }
          if(!this.markersArray) {
            this.markersArray = [];
          }
          this.markersArray.push(marker);
        }
      }
    });
  }
}
