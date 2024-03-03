import {Pipe, PipeTransform} from '@angular/core';
import {DatePipe} from '@angular/common';

@Pipe({
  standalone: true,
  name: 'dateFormat'
})
export class DateFormatPipe extends DatePipe implements PipeTransform {
  constructor() {
    super('en-US');
  }

  override transform(value: any, args?: any): any {
    try{
      return super.transform(value, args ? args : 'dd/MM/yyyy' );
    } catch (e) {
      return "-"
    }
  }
}
