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
    let query: string = searchData.join('%');
    return this.http.get(`http://localhost:8090/api/search?q=${query}`, { observe: 'response' });
  }

  public getSearchSectionLogo(searchData: SectionData) {
    return this.http.get(searchData.logoPath, { observe: 'response', responseType: 'blob' });
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
  public setParams(formData:AdvSearchData):HttpParams {
    let httpParams:HttpParams = new HttpParams();
    Object.keys(formData).forEach(param => {
      if (formData[param]) {
          httpParams = httpParams.set(param, formData[param]);
      }
  });
    return httpParams;
  }

}
