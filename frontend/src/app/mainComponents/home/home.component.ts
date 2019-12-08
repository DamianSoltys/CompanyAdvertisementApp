import { Component, OnInit, AfterViewInit, ViewChild, OnDestroy } from '@angular/core';
import { RecommendationService, RecommendationCount } from '../../services/recommendation.service';
import { RecommendationBranch } from '../../classes/Company';
import { Position } from '../user/company/company.component';
import { storage_Avaliable } from '../../classes/storage_checker';
import { PositionService } from '../../services/position.service';
import { BranchService } from '../../services/branch.service';
import { CarouselComponent } from 'ngx-carousel-lib';
import { Subscription } from 'rxjs';
enum ToggleButton { 
  stop = "Zatrzymaj autoodtwarzanie",
  start = "Wznów autoodtwarzanie"
}
@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit,AfterViewInit,OnDestroy {
  public branchList:RecommendationBranch[];
  public recommendedBranchList:RecommendationBranch[];
  public rateWeight = 2;
  public positionWeight = 1;
  public actualPosition:Position;
  public carouselInterval:any;
  public intervalStopped:boolean = false;
  public toggleText:string = ToggleButton.stop;
  private subscription:Subscription;
  @ViewChild('carousel') carousel:CarouselComponent;
  constructor(private rDataService:RecommendationService,private pDataService:PositionService,private bDataService:BranchService) {}
  
  ngOnInit() {
    let data:RecommendationCount[] | boolean = this.rDataService.countCategories();

    this.subscription = this.pDataService.getActualPosition().subscribe(position=>{
      this.actualPosition = position;
      console.log(this.actualPosition);
    });

    if(data) {
      const subscription = this.rDataService.getRecomendationData(data).subscribe(response=>{
        if(response) {
          this.branchList = response;
          this.sortRecommendedData();
          this.getBranchListLogo();
        } else {
          console.log('error');
        }
      });

      this.subscription.add(subscription);
    }
  }

  ngAfterViewInit() {
    this.setCarouselInterval();
  }

  ngOnDestroy() {
    clearInterval(this.carouselInterval);
    if(this.subscription) {
      this.subscription.unsubscribe();
    }
  }

  public setCarouselInterval() {
    if(this.carousel) {
      this.carouselInterval = setInterval(()=>{
        if(this.carousel.carousel.items.length-1 == this.carousel.carousel.activeIndex) {
          this.carousel.slideTo(0);
        } else {
          this.carousel.slideNext();
        }
        
      },5000);
    } else {
      setTimeout(()=>{
        this.setCarouselInterval();
      },2000);
    }
  }

  public toggleCarousel() {
    if(!this.intervalStopped) {
      clearInterval(this.carouselInterval);
      this.intervalStopped = true;
      this.toggleText = ToggleButton.start;
    } else {
      this.setCarouselInterval();
      this.intervalStopped = false;
      this.toggleText = ToggleButton.stop;
    }
  }

  private sortRecommendedData() {
    this.branchList.map(branch=>{
      if(this.pDataService.isInside(this.actualPosition.latitude,this.actualPosition.longitude,0.025,Number(branch.geoX),Number(branch.geoY))) {
        branch.isInsideArea = 1;     
      } else {
        branch.isInsideArea = 0;
      }

      branch.recommendRate = this.getRecommendRate(branch);
    });
    this.branchList.sort(this.compare);
    console.log(this.branchList);
  }
  private getRecommendRate(branch:RecommendationBranch) {
    if(this.actualPosition) {
      return ((branch.averageRating | 0 * this.rateWeight) + (branch.isInsideArea * this.positionWeight))/(this.rateWeight+this.positionWeight);
    } else {
      return (branch.averageRating | 0 * this.rateWeight)/this.rateWeight;
    }
  }

  private compare(a:RecommendationBranch, b:RecommendationBranch) {
    if (a.recommendRate<b.recommendRate)
       return 1
    if (a.recommendRate>b.recommendRate)
       return -1
    if(a == b) {
      return 0;
    }
 }

 private getBranchListLogo() {
  this.branchList.map(branch=>{
    const subscription = this.bDataService.getBranchLogo(branch).subscribe(response=>{
      if(response.status !=204) {
        let reader = new FileReader();
      reader.addEventListener("load", () => {
        branch.logo = reader.result;           
      }, false);
  
      if (response.body) {
          reader.readAsDataURL(response.body);
      }
      } else {
        branch.logo = this.bDataService.defaultLogoUrl;
      }
      
    },error=>{
      branch.logo = this.bDataService.defaultLogoUrl;
    });
    this.subscription.add(subscription);
  });
 }


}
