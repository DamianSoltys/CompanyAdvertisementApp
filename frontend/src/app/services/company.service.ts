import { Injectable } from '@angular/core';
import { storage_Avaliable } from '../classes/storage_checker';
import { Route, Router } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Company,Branch,Address } from '../classes/Company';
import { UserREST } from '../classes/User';

@Injectable({
  providedIn: 'root'
})
export class CompanyService {

  constructor(private http:HttpClient) { }

  public addCompany(companyData:Company) {
    return this.http.post(`http://localhost:8090/api/companies`,companyData);
  }

  public getCompany(userRest:UserREST) {
    return this.http.get(`http://localhost:8090/api/companies/${userRest.userID}`,{observe: 'response'});
  }

  public deleteCompany(userRest:UserREST) {
    return this.http.delete(`http://localhost:8090/api/companies/${userRest.userID}`,{observe: 'response'});
  }

  public editCompany(companyData:Company,userRest:UserREST) {
    return this.http.patch(`http://localhost:8090/api/companies/${userRest.userID}`,companyData);
  }
}
