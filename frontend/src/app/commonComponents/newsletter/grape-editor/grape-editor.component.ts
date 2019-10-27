import { Component, OnInit, AfterViewInit, OnDestroy, Input, AfterViewChecked } from '@angular/core';
import  grapesjs from 'grapesjs';
import customPreset from 'grapesjs-preset-newsletter';
import { NewsletterService } from 'src/app/services/newsletter.service';

@Component({
  selector: 'app-grape-editor',
  templateUrl: './grape-editor.component.html',
  styleUrls: ['./grape-editor.component.scss']
})
export class GrapeEditorComponent implements OnInit,AfterViewInit,OnDestroy {
  public editor;
  @Input('editorName') nameOfEditor:string;
  constructor(private nDataService:NewsletterService) { }

  ngOnInit() {
    this.nDataService.destroyEditor.subscribe(()=>{

    })
  }

  ngAfterViewInit(): void {
    this.getGrapeJsObject();
  }


  private getGrapeJsObject() {
    this.editor = grapesjs.init({
      container: `#${this.nameOfEditor}`,
      plugins:[customPreset],
      pluginsOpts: {
        [customPreset]: {
          modalTitleImport: 'Import template',
          // ... other options
        }
      },
      storageManager: {
        type: 'local',          // Type of the storage
        autosave: false,         // Store data automatically
        autoload: false,         // Autoload stored data on init
        stepsBeforeSave: 1,     // If autosave enabled, indicates how many changes are necessary before store method is triggered
      },
      width:'auto',
    });
    console.log(this.editor)
  }

  ngOnDestroy() {
    this.editor.destroy();
  }

  public getHtmlTemplate() {
    let htmlWithCss = this.editor.runCommand('gjs-get-inlined-html');
    console.log(htmlWithCss);
  }

}
