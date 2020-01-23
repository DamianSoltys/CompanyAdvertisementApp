import { Component, OnInit, Input } from '@angular/core';
import { SectionData } from '@interfaces/Section';

@Component({
  selector: 'app-search-section',
  templateUrl: './search-section.component.html',
  styleUrls: ['./search-section.component.scss']
})
export class SearchSectionComponent implements OnInit {
  @Input() sectionData: SectionData;

  constructor() { }

  ngOnInit() {
  }

}
