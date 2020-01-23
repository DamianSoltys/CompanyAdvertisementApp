import { Component, OnInit, AfterViewInit, OnDestroy, Input, AfterViewChecked } from '@angular/core';
import grapesjs from 'grapesjs';
import customPreset from 'grapesjs-preset-newsletter';
import { NewsletterService } from '@services/newsletter.service';

@Component({
  selector: 'app-grape-editor',
  templateUrl: './grape-editor.component.html',
  styleUrls: ['./grape-editor.component.scss']
})
export class GrapeEditorComponent implements OnInit, AfterViewInit, OnDestroy {
  @Input('editorName') nameOfEditor: string;
  public editor;

  constructor(private nDataService: NewsletterService) { }

  ngOnInit() {
    this.nDataService.getHtmlTemplate.subscribe(name => {
      if (this.nameOfEditor === name) {
        this.getHtmlTemplate();
      }
    });
  }

  ngAfterViewInit(): void {
    this.getGrapeJsObject();
  }


  private getGrapeJsObject() {
    this.editor = grapesjs.init({
      container: `#${this.nameOfEditor}`,
      plugins: [customPreset],
      pluginsOpts: {
        [customPreset]: {
          modalTitleImport: 'Import template',
          // ... other options
        }
      },
      storageManager: {
        type: 'local',          // Type of the storage
        autosave: true,         // Store data automatically
        autoload: true,         // Autoload stored data on init
        stepsBeforeSave: 1,     // If autosave enabled, indicates how many changes are necessary before store method is triggered
      },
      width: 'auto',
    });
  }

  ngOnDestroy() {
    this.editor.destroy();
  }

  public getHtmlTemplate() {
    let htmlWithCss = this.editor.runCommand('gjs-get-inlined-html');
    this.nDataService.template.next(htmlWithCss);
  }

}
