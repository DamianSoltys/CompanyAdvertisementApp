import { Component, OnInit, AfterViewInit, AfterViewChecked } from '@angular/core';
import { LoaderService } from 'src/app/services/loader.service';
import { BehaviorSubject, Subject } from 'rxjs';

@Component({
  selector: 'app-loader',
  templateUrl: './loader.component.html',
  styleUrls: ['./loader.component.scss']
})
export class LoaderComponent implements OnInit {
  public showLoader = new BehaviorSubject<boolean>(false);

  constructor(private loaderService: LoaderService) { }

  ngOnInit() {
    this.loaderService.showLoaderComponent.subscribe(data => {
      setTimeout(() => this.showLoader.next(data), 0);
    });
  }

}
