import { Injectable } from '@angular/core';
import { storage_Avaliable } from '../classes/storage_checker';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Subject } from 'rxjs';
export interface RecommendationData {
  category:string,
  branchId:number,
}

export interface RecommendationCount {
  category:string,
  count:number,
}
@Injectable({
  providedIn: 'root'
})
export class RecommendationService {

  constructor(private http:HttpClient) { }

  public getRecomendationData(recomendationData:RecommendationCount[]) {
    let subject = new Subject<any>();
    this.sortData(recomendationData);
    let httpParams = new HttpParams();
    for(let i=0;i<3;i++) {
      if(recomendationData[i]) {
        httpParams = httpParams.append('category',recomendationData[i].category);
      }
    }
    console.log(httpParams.toString())
    this.http.get(`http://localhost:8090/api/recommendation`,{observe:'response',params:httpParams}).subscribe(response=>{
      console.log(response);
      subject.next(response.body);
    },error=>{
      console.log(error);
      subject.next(false);
    });
    return subject;
  }

  public pushRecommendationData(category:string,branchId:number) {
    console.log('pushrecodata')
    let inArray:boolean = false;
    if(storage_Avaliable('localStorage')) {
      let data:RecommendationData[] = JSON.parse(localStorage.getItem('recommendationData'));
      if(data) {
        if(data.length >= 20) {
          data.pop();
        }
          data.forEach(item=>{
            if(item.branchId === branchId) {
              inArray = true;
            }
          });

          if(!inArray) {
            let recomendationData:RecommendationData = {
              category:category,
              branchId:branchId
            }
            data.push(recomendationData);
            localStorage.removeItem('recommendationData');
            localStorage.setItem('recommendationData',JSON.stringify(data));
          }
      } else {
        let recomendationData =[];
        let data:RecommendationData = {
          category:category,
          branchId:branchId
        }
        recomendationData.push(data);
        localStorage.setItem('recommendationData',JSON.stringify(recomendationData));
      }
    }
  }

  private sortData(recomendationData:RecommendationCount[]) {
    recomendationData.sort(function (a, b) {
      if (a.count > b.count) {
          return -1;
      }
      if (b.count > a.count) {
          return 1;
      }
      return 0;
  });
}

  public countCategories() {
    if(storage_Avaliable('localStorage')) {
      let data:RecommendationData[] = JSON.parse(localStorage.getItem('recommendationData'));
      let countData:RecommendationCount[] = [];
      let inArray:boolean = false;
      if(data) {
        data.forEach(item=>{
          if(countData) {

            let categoryInArray = countData.find(this.categoryInArray(item.category));
            if(!categoryInArray) {
              let data:RecommendationCount = {
                category:item.category,
                count:1
              }
              countData.push(data);
            } else {
              countData.map(countData=>{
                if(countData.category == item.category) {
                  countData.count++;
                }
              });
            }

            
          } else {
            let data:RecommendationCount = {
              category:item.category,
              count:1
            }
            countData.push(data);
          }
        });
        console.log(countData);
        return countData;
      } else {
        return false;
      }
    }
  }

  public categoryInArray(category) {
    return function callBack(element) {
      return element.category === category;
    }
  }
}
