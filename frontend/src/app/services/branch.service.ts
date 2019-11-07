import { Injectable } from '@angular/core';
import { storage_Avaliable } from '../classes/storage_checker';
import { Route, Router } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Company, Branch, Address, GetCompany } from '../classes/Company';
import { UserREST } from '../classes/User';
import { BehaviorSubject, Subject } from 'rxjs';
import { EditRequestData } from '../user/company/company.component';

@Injectable({
  providedIn: 'root'
})
export class BranchService {
  public branchData: Branch[] = [];
  private isBranch = new BehaviorSubject(false);
  public getBranchData = new Subject<boolean>();
  public deletedId:number;
  public defaultLogoUrl = '../../../assets/Img/default_logo.png';

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

  public getBranchLogo(branchData:Branch) {
    let url = branchData.logoURL;
    return this.http.get(url,{observe: 'response',responseType:'blob'});
  }

  public deleteBranch(branchId:number) {
    return this.http.delete(`http://localhost:8090/api/branch/${branchId}`, {
      observe: 'response'
    });
  }

  public addBranches(companyId:number,branches:Branch[],logoList?:File[]):Subject<any> {
    let subject = new Subject<any>();
    this.http.post(`http://localhost:8090/api/companies/${companyId}/branches`,branches).subscribe(response=> {
     if(logoList) {
      let branches:Branch[] = <Branch[]>response;
      let counter:number = 0;
      branches.forEach((branch,index)=>{
        let url = branch.logoPath;
        let logoData = new FormData();
        logoData.append(branch.logoKey,logoList[index]);

        this.http.put(url,logoData).subscribe(response=>{
          counter++;
          console.log('dodano pliczek');
          if(branches.length === counter) {
            subject.next(true);
          }
        },error=> {
          console.log(error);
          subject.next(true);
        })
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

  public editBranch(editRequestData:EditRequestData,branch:Branch,workLogo:File):Subject<any> {
    let subject = new Subject<any>();
    this.http.patch(`http://localhost:8090/api/branch/${editRequestData.workId}`,branch).subscribe(response=>{
      if(workLogo) {
        console.log(response);
        let url = editRequestData.logoURL;
        let logoData = new FormData();
        logoData.append(editRequestData.logoKey,workLogo);
        this.http.put(url,logoData).subscribe(response=>{
          console.log("dodano plcizek");
          subject.next(true);
        },error=>{
          console.log(error)
          subject.next(true);
        });
      } else {
        subject.next(true);
      }
    },error=>{
      subject.next(false);
    });
    return subject;
  }

  public storeBranchData(branch: Branch) {
    if (storage_Avaliable('localStorage')) {
      if (localStorage.getItem('branchData')) {
        let branchData: Branch[] = JSON.parse(
          localStorage.getItem('branchData')
        );
        let data = branchData.find((element)=>{
          return element.branchId === branch.branchId;
         });

        if (!data) {
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
  }

  public deleteStorageData() {
    if(storage_Avaliable('localStorage')) {
      localStorage.removeItem('branchData');
    }
  }
}
