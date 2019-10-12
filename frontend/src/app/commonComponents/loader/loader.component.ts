import { Component, OnInit } from '@angular/core';
import { LoaderService } from 'src/app/services/loader.service';
import { BehaviorSubject, Subject } from 'rxjs';

@Component({
  selector: 'app-loader',
  templateUrl: './loader.component.html',
  styleUrls: ['./loader.component.scss']
})
export class LoaderComponent implements OnInit {
  public showLoader:Subject<boolean> = this.loaderService.showLoaderComponent;
  constructor(private loaderService:LoaderService) { }

  ngOnInit() {
  }

}
