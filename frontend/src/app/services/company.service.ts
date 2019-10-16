import { Injectable } from '@angular/core';
import { storage_Avaliable } from '../classes/storage_checker';
import { Route, Router } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Company, Branch, Address, GetCompany } from '../classes/Company';
import { UserREST } from '../classes/User';
import { BehaviorSubject, Subject } from 'rxjs';


@Injectable({
  providedIn: 'root'
})
export class CompanyService {
  public CompanyData: GetCompany[] = [];
  private isCompany = new BehaviorSubject(false);
  public getCompanyData = new Subject<boolean>();

  constructor(private http: HttpClient) {}

  public addCompany(companyData: Company,logoList?:File[]) {

    let logoData = new FormData();
    if(logoList) {
    }
    return this.http.post(`http://localhost:8090/api/companies`, companyData);

  }

  public putFile(Url: string, file: File[]) {
    let data = new FormData();
    file.forEach((file, index) => {
      data.append(`Logo${index}`, file, file.name);
    });
    return this.http.put(Url, data);
  }
  public deleteStorageData() {
    if(storage_Avaliable('localStorage')) {
      localStorage.removeItem('companyData');
    }
  }

  public storeCompanyData(company: GetCompany) {
    if (storage_Avaliable('localStorage')) {
      if (localStorage.getItem('companyData')) {
        let companyData: GetCompany[] = JSON.parse(
          localStorage.getItem('companyData')
        );
        companyData.forEach(companyStorage => {
          if (company.companyId === companyStorage.companyId) {
            this.isCompany.next(true);
          }
        });

        if (!this.isCompany.value) {
          companyData.push(company);
          localStorage.setItem('companyData', JSON.stringify(companyData));
        }
      } else {
        let companyData: GetCompany[] = [];
        companyData.push(company);
        localStorage.setItem('companyData', JSON.stringify(companyData));
      }
    } else {
      console.log('Store niedostępny');
    }
  }

  public getCompany(companyId: number) {
    return this.http.get(`http://localhost:8090/api/companies/${companyId}`, {
      observe: 'response'
    });
  }

  public deleteCompany(companyId: number) {
    return this.http.delete(
      `http://localhost:8090/api/companies/${companyId}`,
      { observe: 'response' }
    );
  }

  public editCompany(companyData: Company, companyId: number) {
    return this.http.patch(
      `http://localhost:8090/api/companies/${companyId}`,
      companyData
    );
  }
}
