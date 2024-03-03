import {Pipe, PipeTransform} from '@angular/core';
import {CurrencyPipe} from '@angular/common';

@Pipe({
  standalone: true,
  name: 'formatCurrency'
})
export class FormatCurrencyPipe extends CurrencyPipe implements PipeTransform {
  private decimal_separator: string = '.';
  private thousands_separator: string = ',';

  constructor() {
    super('en-US');
  }

  override transform(value: any, ...args: any): any {
    if (value == null || value.toString().trim() === '') {
      return null;
    }
    let formatByCurrencyPipe;
    if (parseFloat(value) === (value | 0)) {
      formatByCurrencyPipe = super.transform(value, ...args);
    } else {
      try {
        formatByCurrencyPipe = super.transform(value, ...args, 'symbol', '1.2-3');
      } catch (error) {
        return null;
      }
    }
    if (formatByCurrencyPipe) {
      let cleanString = formatByCurrencyPipe.replace(/([^0-9,.]+)/gi, '');
      if (parseFloat(value) < 0) {
        cleanString = "-" + cleanString;
      }
      return cleanString;
    }
    return null;
  }

  parse(value: any): any {
    if (value) {
      let number = value.split(this.decimal_separator)[0];
      number = number.replace(new RegExp(this.thousands_separator, "g"), "");
      return number;
    }
    return null;
  }
}
