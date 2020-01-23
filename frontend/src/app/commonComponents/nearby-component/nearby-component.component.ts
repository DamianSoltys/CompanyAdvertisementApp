import { Component, OnInit, Input, ViewChild, AfterViewInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { storage_Avaliable } from '@interfaces/storage_checker';
import { Position, Marker } from '@mainComponents/user/company/company.component';
import { SearchService } from '@services/search.service';
import { BranchService } from '@services/branch.service';
import { Branch } from '@interfaces/Company';
import { Router } from '@angular/router';
import { AgmMap } from '@agm/core';
import { Subject } from 'rxjs';
import { PositionService } from '@services/position.service';

export interface NearbyMarker extends Marker {
  branchId?: number,
  companyId?: number
}
@Component({
  selector: 'app-nearby-component',
  templateUrl: './nearby-component.component.html',
  styleUrls: ['./nearby-component.component.scss']
})
export class NearbyComponent implements OnInit {
  @Input() name;
  @ViewChild('googleMap') googleMap: AgmMap;
  public zoom = 9;
  public draggable = false;
  public actualPosition: Position;
  public radius: number;
  public biggerRadius: number;
  public markersArray: NearbyMarker[];
  public branches: Branch[];

  constructor(public activeModal: NgbActiveModal, private sDataService: SearchService, private bDataService: BranchService, private router: Router, private pDataService: PositionService) { }

  ngOnInit() {
    this.pDataService.getActualPosition().subscribe(position => {
      if (position) {
        this.actualPosition = position;
        this.radius = this.pDataService.calculatePointDistance(this.actualPosition.latitude, this.actualPosition.longitude - 0.48, this.actualPosition.latitude, this.actualPosition.longitude + 0.48) / 2;
        this.biggerRadius = this.pDataService.calculatePointDistance(this.actualPosition.latitude, this.actualPosition.longitude - 0.95, this.actualPosition.latitude, this.actualPosition.longitude + 0.95) / 2;
        this.getNearbyBranches();
      }
    });

  }

  public getNearbyBranches() {
    this.bDataService.getBranches().subscribe(response => {
      this.branches = <Branch[]>response.body['content'];
      this.getMarkerData();
    }, error => {
      console.log(error);
    })
  }

  public goToBranchProfile(branchId: number, companyId: number) {
    this.router.navigate(['/home']).then(() => {
      this.router.navigate(['/branchProfile', companyId, branchId]);
    });
  }

  public getMarkerData() {
    this.branches.forEach(branch => {
      if (this.pDataService.checkIfPointInsideCircle(this.biggerRadius, this.actualPosition.latitude, this.actualPosition.longitude, Number(branch.geoX), Number(branch.geoY))) {
        let marker: NearbyMarker = {
          latitude: Number(branch.geoX),
          longitude: Number(branch.geoY),
          label: branch.name,
          branchId: branch.branchId,
          companyId: branch.companyId,
          opacity: 1,
        }

        if (this.pDataService.checkIfPointInsideCircle(this.biggerRadius, this.actualPosition.latitude, this.actualPosition.longitude, Number(branch.geoX), Number(branch.geoY)) &&
          !this.pDataService.checkIfPointInsideCircle(this.radius, this.actualPosition.latitude, this.actualPosition.longitude, Number(branch.geoX), Number(branch.geoY))) {
          marker.opacity = 0.5;
        }

        if (!this.markersArray) {
          this.markersArray = [];
        }
        this.markersArray.push(marker);
      }
    });
  }
}
