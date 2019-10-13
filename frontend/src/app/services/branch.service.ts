import { Injectable } from '@angular/core';
import { storage_Avaliable } from '../classes/storage_checker';
import { Route, Router } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Company, Branch, Address, GetCompany } from '../classes/Company';
import { UserREST } from '../classes/User';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class BranchService {
  public branchData: Branch[] = [];
  private isBranch = new BehaviorSubject(false);

  constructor(private http: HttpClient) {}

  public getBranches() {
    return this.http.get(`http://localhost:8090/api/branch`, {
      observe: 'response'
    });
  }
  public getBranch(branchId: number) {
    return this.http.get(`http://localhost:8090/api/branch/${branchId}`, {
      observe: 'response'
    });
  }

  public storeBranchData(branch: Branch) {
    if (storage_Avaliable('localStorage')) {
      if (localStorage.getItem('branchData')) {
        let branchData: Branch[] = JSON.parse(
          localStorage.getItem('branchData')
        );
        branchData.forEach(branchStorage => {
          if (branch.branchId === branchStorage.branchId) {
            this.isBranch.next(true);
          }
        });

        if (!this.isBranch.value) {
          branchData.push(branch);
          localStorage.setItem('branchData', JSON.stringify(branchData));
        }
      } else {
        let branchData: Branch[] = [];
        branchData.push(branch);
        localStorage.setItem('branchData', JSON.stringify(branchData));
      }
    } else {
      console.log('Store niedostępny');
    }
    console.log(JSON.parse(localStorage.getItem('branchData')));
  }
}
