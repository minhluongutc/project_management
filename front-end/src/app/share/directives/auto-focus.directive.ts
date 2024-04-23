import { Directive, ElementRef, AfterViewInit } from '@angular/core';

@Directive({
  standalone: true,
  selector: '[AutofocusCustom]'
})
export class AutofocusDirective implements AfterViewInit {
  constructor(private el: ElementRef) {}

  ngAfterViewInit() {
    setTimeout(() => {
      this.el.nativeElement.focus();
    }, 200);
  }
}
