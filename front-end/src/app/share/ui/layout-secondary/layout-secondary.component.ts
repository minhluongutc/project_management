import {Component, EventEmitter, Input, Output, TemplateRef} from '@angular/core';

@Component({
  selector: 'app-layout-secondary',
  templateUrl: './layout-secondary.component.html',
  styleUrl: './layout-secondary.component.scss'
})
export class LayoutSecondaryComponent {
  @Input() title = '';
  @Input() titleTemplate: TemplateRef<any> | null = null;
  @Input() menu: TemplateRef<any> | null = null;
  @Input() content: TemplateRef<any> | null = null;
  @Output() handleBack: EventEmitter<any> = new EventEmitter();

  handleClickBackButton() {
    this.handleBack.emit();
  }
}
