import { Component, OnInit } from '@angular/core';
import { RecommendationService, RecommendationCount } from '../services/recommendation.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  constructor(private rDataService:RecommendationService) {}

  ngOnInit() {
    let data:RecommendationCount[] | boolean = this.rDataService.countCategories();
    if(data) {
      this.rDataService.getRecomendationData(data).subscribe(response=>{
        if(response) {
          console.log(response);
        } else {
          console.log('error');
        }
      });
    }
  }
}
