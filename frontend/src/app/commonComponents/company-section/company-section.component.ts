import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-company-section',
  templateUrl: './company-section.component.html',
  styleUrls: ['./company-section.component.scss']
})
export class CompanySectionComponent implements OnInit {
  @Input() companyData:string;

  constructor() { }

  ngOnInit() {
    console.log(this.companyData);
  }

}
