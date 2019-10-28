import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { GrapeEditorComponent } from './grape-editor.component';

@NgModule({
  declarations: [GrapeEditorComponent],
  imports: [
    CommonModule
  ],exports:[GrapeEditorComponent]
})
export class GrapeEditorModule { }
