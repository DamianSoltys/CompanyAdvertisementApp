import { Injectable } from '@angular/core';
import { storage_Avaliable } from '../classes/storage_checker';
import { Route, Router } from '@angular/router';
import { HttpClient, HttpHeaders, HttpRequest, HttpResponse, HttpParams } from '@angular/common/http';
import { Company, Branch, Address, GetCompany } from '../classes/Company';
import { UserREST } from '../classes/User';
import { BehaviorSubject, Subject, Observable } from 'rxjs';
import { EditRequestData } from '../mainComponents/user/company/company.component';
import { UserService } from './user.service';

@Injectable({
  providedIn: 'root'
})
export class CompanyService {
  public CompanyData: GetCompany[] = [];
  private isCompany = new BehaviorSubject(false);
  public getCompanyData = new Subject<boolean>();
  private isLoaded = new Subject<boolean>();
  public defaultCProfileUrl = '../../../assets/Img/default_logo.png';
  public defaultCListUrl = '../../assets/Img/default_logo.png';
  public userREST:UserREST;

  constructor(private http: HttpClient,private uDataService:UserService) {}

  public addCompany(companyData: Company,logoList?:File[],companyLogo?:File) :Subject<boolean> {
    this.isLoaded.subscribe((data)=>{
      subject.next(data);
    });
    let subject = new Subject<boolean>();
    this.getRidOfPreviewLogo(companyData);
    this.http.post(`http://localhost:8090/api/companies`, companyData).subscribe(response=>{
      console.log(response);
      if(companyLogo) {
        this.companyLogoRequest(response,companyLogo,logoList);
      } else if(!companyLogo && logoList){     
       this.workLogoRequest(response,logoList);
      } else {
        this.isLoaded.next(true);
      }
      
    },error=>{
      console.log(error);
      this.isLoaded.next(false);
    });
    return subject;
  }

  private getRidOfPreviewLogo(companyData:Company) {
    if(companyData.branches) {
      companyData.branches.map(branch=>{
        branch.actualSelectedLogo = undefined;
      });
    }
  }

  public getActualUser() {
    if(storage_Avaliable('localStorage')) {
      if(localStorage.getItem('userREST')) {
        this.userREST = JSON.parse(localStorage.getItem('userREST'));
      } else {
        this.uDataService.updateUser();
      }
    } else {
      this.uDataService.updateUser();
    }
  }

  public getCategoryData() {
    let subject = new Subject<any>();
    this.http.get(`http://localhost:8090/api/categories`,{observe:'response'}).subscribe(response=>{
      subject.next(response);
    },error=>{
      subject.next(false);
    });
    return subject;
  }

  public checkForUserPermission(companyId:number) {
    let haveAcces:boolean = false;
    this.getActualUser();
    if(this.userREST) {
      if(this.userREST.companiesIDs) {
        this.userREST.companiesIDs.forEach(id=>{
          console.log(companyId);
          console.log(id);
          if(companyId == id) {
            haveAcces = true;
          }
        });
      }
      if(haveAcces) {
        return true;
      } else {
        return false;
      }
    }
  }

  private workLogoRequest(response,logoList:File[]) {
    let counter:number = 0;
    logoList.forEach((logo,index)=>{
      let url = response['branchBuildDTOs'][index]['putLogoURL'];
      let logoData = new FormData();
      logoData.append(response['branchBuildDTOs'][index]['logoKey'],logo);
      logoData.append(response['branchBuildDTOs'][index]['logoKey'],'Value');
      this.http.put(url,logoData).subscribe(response=>{
        counter++;
        if(counter === logoList.length) {
          this.isLoaded.next(true);
        }
        console.log(`dodanlo logo brancha ${index}`);
      },error=>{
        console.log(error);
        counter++;
        if(counter === logoList.length) {
          this.isLoaded.next(true);
        }
        console.log(`nie dodano logo brancha ${index}`);
      });
    });
  }

  private companyLogoRequest(response,companyLogo:File,logoList?:File[]) {
    let logoData = new FormData();
    logoData.append(response['logoKey'],companyLogo);
    logoData.append(response['logoKey'],'Value');
    let url = response['putLogoURL'];
    this.http.put(url,logoData).subscribe(response2=>{
      console.log(response2)
      console.log("dodano plik")
      if(logoList) {
        console.log("jest logo list")
        console.log(logoList)
        this.workLogoRequest(response,logoList);
      } else {
        console.log("witam")
        this.isLoaded.next(true);
      }
    },error2=>{
      console.log(error2)
      console.log("niedodano pliku");
      this.isLoaded.next(true);
    });
  }

  public deleteStorageData(profile?:boolean) {
    if(storage_Avaliable('localStorage')) {
      let storeKey = profile?'companyProfileData':'companyData';
      localStorage.removeItem(storeKey);
    }
  }

  public getActualAdvSearchPage(comment:boolean,pageNumber:number,branchId:number) {
    let httpParams = new HttpParams().set('size','3').set('page',pageNumber.toString()).set('branchId',branchId.toString());
    console.log(httpParams)
    if(comment) {
      return this.http.get(
        `http://localhost:8090/api/comment`,
        { observe: 'response' ,params: httpParams}
      );
    } else {
      return this.http.get(
        `http://localhost:8090/api/rating`,
        { observe: 'response' ,params: httpParams}
      );
    }

  }

  public storeCompanyData(company: GetCompany,profile?:boolean) {
    let storeKey = profile?'companyProfileData':'companyData';
    if (storage_Avaliable('localStorage')) {
      if (localStorage.getItem(storeKey)) {
        let companyData: GetCompany[] = JSON.parse(
          localStorage.getItem(storeKey)
        );
       let data = companyData.find((element)=>{
        return element.companyId === company.companyId;
       });

        if (!data) {
          companyData.push(company);
          console.log('data')
          localStorage.setItem(storeKey, JSON.stringify(companyData));
        }

      } else {
        let companyData:GetCompany[] = [];
        companyData.push(company);
        console.log('data')
        localStorage.setItem(storeKey, JSON.stringify(companyData));
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

  public getCompanyLogo(companyData:GetCompany){
    let url = companyData.getLogoURL;
    return this.http.get(url,{ observe: 'response',responseType: 'blob'}); 
  }

  public deleteCompany(companyId: number) {
    return this.http.delete(
      `http://localhost:8090/api/companies/${companyId}`,
      { observe: 'response' }
    );
  }

  public removeCompanyFromLocalStorage(companyId:number) {
    if(storage_Avaliable('localStorage')) {
      let companyData:GetCompany[] = JSON.parse(localStorage.getItem('companyData'));
      companyData.forEach((company,index)=>{
        if(company.companyId === companyId) {
          console.log(index)
          companyData = companyData.filter((company)=>company.companyId != companyId);
          console.log(companyData);
          if(companyData.length) {
            console.log('usuwanko')
            this.deleteStorageData();
            localStorage.setItem('companyData',JSON.stringify(companyData));
          } else {
            console.log('usuniete')
            localStorage.removeItem('companyData');
          }
        }
      });
    }
  }

  public editCompany(companyData: Company, editRequestData:EditRequestData,companyLogo?:File):Subject<any> {
    let subject = new Subject<any>();
    this.http.patch(
      `http://localhost:8090/api/companies/${editRequestData.companyId}`,
      companyData
    ).subscribe(response=>{
      let companyData:GetCompany = <GetCompany>response;
      if(companyLogo) {
        let logoData = new FormData();
        let url = editRequestData.putLogoUrl;
        logoData.append(companyData.logoKey,companyLogo);
        logoData.append(companyData.logoKey,'Value');
        this.http.put(url,logoData).subscribe(response=> {
        subject.next(true);
      },error=>{
        console.log(error);
        subject.next(true);
      });
      } else {
        subject.next(true);
      }   
    },error=>{
      console.log(error);
      subject.next(false);
    });
    return subject;
  }
}
