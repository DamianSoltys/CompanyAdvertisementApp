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

  public getCompany(companyId:number) {
    return this.http.get(`http://localhost:8090/api/companies/${companyId}`,{observe: 'response'});
  }

  public deleteCompany(companyId:number) {
    return this.http.delete(`http://localhost:8090/api/companies/${companyId}`,{observe: 'response'});
  }

  public editCompany(companyData:Company,companyId:number) {
    return this.http.patch(`http://localhost:8090/api/companies/${companyId}`,companyData);
  }
}
