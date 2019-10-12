import { Injectable } from '@angular/core';
import { storage_Avaliable } from '../classes/storage_checker';
import { Route, Router } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Company,Branch,Address, GetCompany } from '../classes/Company';
import { UserREST } from '../classes/User';

@Injectable({
  providedIn: 'root'
})
export class BranchService {

  constructor(private http:HttpClient) { }

  public getBranches() {
    return this.http.get(`http://localhost:8090/api/branch`,{observe:'response'});
  }
  public getBranch(branchId:number) {
    return this.http.get(`http://localhost:8090/api/branch/${branchId}`,{observe:'response'});
  }
}
