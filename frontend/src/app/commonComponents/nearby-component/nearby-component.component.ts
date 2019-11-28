import { Component, OnInit, Input, ViewChild, AfterViewInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { storage_Avaliable } from 'src/app/classes/storage_checker';
import { Position, Marker } from 'src/app/user/company/company.component';
import { SearchService } from 'src/app/services/search.service';
import { BranchService } from 'src/app/services/branch.service';
import { Branch } from 'src/app/classes/Company';
import { Router } from '@angular/router';
import { AgmMap } from '@agm/core';
import { Subject } from 'rxjs';
import { PositionService } from 'src/app/services/position.service';

export interface NearbyMarker extends Marker {
  branchId?:number,
  companyId?:number
}
@Component({
  selector: 'app-nearby-component',
  templateUrl: './nearby-component.component.html',
  styleUrls: ['./nearby-component.component.scss']
})
export class NearbyComponent implements OnInit{
  @Input() name;
  public zoom = 10;
  public draggable = false;
  public actualPosition: Position;
  public radius:number;
  public biggerRadius:number;
  public markersArray:NearbyMarker[];
  public branches:Branch[];
  @ViewChild('googleMap') googleMap:AgmMap;

  constructor(public activeModal: NgbActiveModal,private sDataService:SearchService,private bDataService:BranchService,private router:Router,private pDataService:PositionService) { }

  ngOnInit() {
    this.pDataService.getActualPosition().subscribe(position=>{
      if(position) {
        this.actualPosition = position;
        this.radius = this.pDataService.calculatePointDistance(this.actualPosition.latitude,this.actualPosition.longitude-0.3,this.actualPosition.latitude,this.actualPosition.longitude+0.3)/2;
        this.biggerRadius = this.pDataService.calculatePointDistance(this.actualPosition.latitude,this.actualPosition.longitude-0.6,this.actualPosition.latitude,this.actualPosition.longitude+0.6)/2;
        this.getNearbyBranches();
      }
    });
    
  }

  public getNearbyBranches() {
    this.bDataService.getBranches().subscribe(response=>{
      this.branches = <Branch[]>response.body['content'];
      this.getMarkerData();
    },error=>{
      console.log(error);
    })
  }


  public goToBranchProfile(branchId:number,companyId:number) {
    this.router.navigate(['/home']).then(()=>{
      this.router.navigate(['/branchProfile',companyId,branchId]);
    });
  }



  public getMarkerData() {
    this.branches.forEach(branch=>{
      if(this.pDataService.checkIfPointInsideCircle(this.biggerRadius,this.actualPosition.latitude,this.actualPosition.longitude,Number(branch.geoX),Number(branch.geoY))) {
        let marker:NearbyMarker = {
          latitude:Number(branch.geoX),
          longitude:Number(branch.geoY),
          label:branch.name,
          branchId:branch.branchId,
          companyId:branch.companyId,
          opacity:1,
        }
          if(this.pDataService.checkIfPointInsideCircle(this.biggerRadius,this.actualPosition.latitude,this.actualPosition.longitude,Number(branch.geoX),Number(branch.geoY)) &&
          !this.pDataService.checkIfPointInsideCircle(this.radius,this.actualPosition.latitude,this.actualPosition.longitude,Number(branch.geoX),Number(branch.geoY))) {
            marker.opacity = 0.5;
          } 
          if(!this.markersArray) {
            this.markersArray = [];
          }
          this.markersArray.push(marker);
        }
      

      });
      
  }
}
