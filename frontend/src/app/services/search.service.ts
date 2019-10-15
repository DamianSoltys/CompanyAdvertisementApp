import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class SearchService {
  constructor(private http: HttpClient) {}

  sendSearchData(searchData: Array<string>) {
    let query:string = searchData.join('%');
    return this.http.get(`http://localhost:8090/api/search?q=${query}`,{observe:'response'});
  }
}
