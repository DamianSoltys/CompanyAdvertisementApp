import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { SearchResponse, SectionData } from '../classes/Section';
import { FormGroup } from '@angular/forms';
import { AdvSearchData } from '../search/advanced-search/advanced-search.component';

@Injectable({
  providedIn: 'root'
})
export class SearchService {
  public defaultSearchLogo: string = '../../assets/Img/default_logo.png';
  constructor(private http: HttpClient) {}

  public sendSearchData(searchData: Array<string>) {
    if(searchData) {
      let query: string = searchData.join('%');
      console.log(query)
      let httpParams:HttpParams = new HttpParams().set('q',query);
      return this.http.get(`http://localhost:8090/api/search`, { observe: 'response' ,params:httpParams});
    }
  }

  public getActualPageData(searchData: Array<string>,pageNumber:number) {
    let query: string = searchData.join('%');
    return this.http.get(`http://localhost:8090/api/search?q=${query}&size=3&page=${pageNumber}`, { observe: 'response' });
  }

  public getSearchSectionLogo(searchData: SectionData) {
    return this.http.get(searchData.getLogoURL, { observe: 'response', responseType: 'blob' });
  }

  public getCitiesByVoivodeship() {
    return this.http.get(`http://localhost:8090/api/cities`, { observe: 'response' });
  }

  public sendAdvSearchData(formData: AdvSearchData) {
    if(formData.type) {
      formData.type = formData.type.map(value=>{  
        if(value == 'Firma') {
          return value = 'company';
        } else {
          return value = 'branch';
        }
      });
    }
    let httpParams = this.setParams(formData);
    console.log(httpParams)
    return this.http.get(
      `http://localhost:8090/api/search-adv`,
      { observe: 'response' ,params: httpParams}
    );

  }

  public paginator(items, page?, per_page?) {
    var page = page || 1,
    per_page = per_page || 10,
    offset = (page - 1) * per_page,
    paginatedItems = items.slice(offset).slice(0, per_page),
    total_pages = Math.ceil(items.length / per_page);
    return {
      page: page,
      per_page: per_page,
      pre_page: page - 1 ? page - 1 : null,
      next_page: (total_pages > page) ? page + 1 : null,
      total: items.length,
      total_pages: total_pages,
      data: paginatedItems
    };
  }
  
  public getActualAdvSearchPage(formData: AdvSearchData, pageNumber:number) {
    if(formData.type) {
      formData.type = formData.type.map(value=>{  
        if(value == 'Firma') {
          return value = 'company';
        } else {
          return value = 'branch';
        }
      });
    }
    let httpParams = this.setParams(formData,pageNumber);
    console.log(httpParams)
    return this.http.get(
      `http://localhost:8090/api/search-adv`,
      { observe: 'response' ,params: httpParams}
    );

  }
  public setParams(formData:AdvSearchData,pageNumber?:number):HttpParams {
    let httpParams:HttpParams = new HttpParams();
    Object.keys(formData).forEach(param => {
      if (formData[param]) {
          httpParams = httpParams.set(param, formData[param]);
      }
  });

    if(pageNumber) {
      httpParams = httpParams.set('page',`${pageNumber}`);
    }
    return httpParams;
  }

}
