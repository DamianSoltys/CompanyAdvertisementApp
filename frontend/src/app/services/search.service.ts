import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { SearchResponse, SectionData } from '../classes/Section';

@Injectable({
  providedIn: 'root'
})
export class SearchService {
  public defaultSearchLogo:string = '../../assets/Img/default_logo.png';
  constructor(private http: HttpClient) {}

  sendSearchData(searchData: Array<string>) {
    let query:string = searchData.join('%');
    return this.http.get(`http://localhost:8090/api/search?q=${query}`,{observe:'response'});
  }

  getSearchSectionLogo(searchData:SectionData) {
    return this.http.get(searchData.logoPath,{observe:'response',responseType: 'blob'});
  }

  getCitiesByVoivodeship() {
    return this.http.get(`http://localhost:8090/api/cities`,{observe:'response'});
    
  }
}
