import { Component, OnInit } from '@angular/core';
import { RecommendationService } from '../services/recommendation.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  constructor(private rDataService:RecommendationService) {}

  ngOnInit() {
    console.log(this.rDataService.countCategories());
  }
}
