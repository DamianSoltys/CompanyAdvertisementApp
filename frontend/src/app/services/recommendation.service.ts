import { Injectable } from '@angular/core';
import { storage_Avaliable } from '../classes/storage_checker';
import { HttpClient } from '@angular/common/http';
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

  public getRecomendationData() {

  }
}
