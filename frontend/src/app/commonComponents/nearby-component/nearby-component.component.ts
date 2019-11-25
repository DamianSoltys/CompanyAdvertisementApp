import { Component, OnInit, Input, ViewChild, AfterViewInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { storage_Avaliable } from 'src/app/classes/storage_checker';
import { Position, Marker } from 'src/app/user/company/company.component';
import { SearchService } from 'src/app/services/search.service';
import { BranchService } from 'src/app/services/branch.service';
import { Branch } from 'src/app/classes/Company';
import { Router } from '@angular/router';
import { AgmMap } from '@agm/core';

export interface NearbyMarker extends Marker {
  branchId?:number,
  companyId?:number
}
@Component({
  selector: 'app-nearby-component',
  templateUrl: './nearby-component.component.html',
  styleUrls: ['./nearby-component.component.scss']
})
export class NearbyComponent implements OnInit,AfterViewInit {
  @Input() name;
  public zoom = 12;
  public draggable = false;
  public actualPosition: Position;
  public radius:number;
  public markersArray:NearbyMarker[];
  public branches:Branch[];
  @ViewChild('googleMap') googleMap:AgmMap;

  constructor(public activeModal: NgbActiveModal,private sDataService:SearchService,private bDataService:BranchService,private router:Router) { }

  ngOnInit() {
    this.getActualPosition();
  }

  ngAfterViewInit() {
    this.googleMap.mapReady.subscribe(map=>{
      this.radius = this.setCircleRadius(this.actualPosition.latitude,this.actualPosition.longitude-0.05,this.actualPosition.latitude,this.actualPosition.longitude+0.05)/2;
    });
  }

  private getActualPosition() {
    let navigatorObject = window.navigator;
    if (storage_Avaliable('localStorage')) {
      if (!localStorage.getItem('actualPosition') && !this.actualPosition) {
        this.actualPosition = {};
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
          error => {
            this.actualPosition ={
              latitude: 51.246452,
              longitude: 22.568445
            };
          }
        );
      } else {
        let position = JSON.parse(localStorage.getItem('actualPosition'));

        if (position) {
          this.actualPosition = {
            latitude: position.latitude,
            longitude: position.longitude
          };
        } else {
          this.actualPosition = {
            latitude: 51.246452,
            longitude: 22.568445
          };
        }
      }
    }
    this.getNearbyBranches();
  }

  public getNearbyBranches() {
    this.bDataService.getBranches().subscribe(response=>{
      this.branches = <Branch[]>response.body['content'];
      this.getMarkerData();    
    },error=>{
      console.log(error);
    })
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

  public goToBranchProfile(branchId:number,companyId:number) {
    console.log(branchId);
    console.log(companyId)
    this.router.navigate(['/home']).then(()=>{
      this.router.navigate(['/branchProfile',companyId,branchId]);
    });
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

  public getMarkerData() {
    this.branches.forEach(branch=>{
      if(this.isInside(this.actualPosition.latitude,this.actualPosition.longitude,0.025,Number(branch.geoX),Number(branch.geoY))) {
          
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
      });
  }
}
