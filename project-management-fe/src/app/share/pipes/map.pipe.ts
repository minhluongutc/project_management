import { Pipe, PipeTransform } from '@angular/core';

import { DecimalPipe } from "@angular/common";
import { DateFormatPipe } from './date.pipe';
import { FormatCurrencyPipe } from './currency.pipe';

export enum DateFormat {
  Date = 'dd/MM/yyyy',
  DateHour = 'dd/MM/yyyy HH',
  DateTime = 'dd/MM/yyyy HH:mm',
}

export enum MapKeyType {
  String,
  Number,
  Boolean
}

export interface MapItem {
  label: string;
  value: any;
}

@Pipe({
  name: 'map',
  standalone: true
})
export class MapPipe implements PipeTransform {
  private datePipe: DateFormatPipe = new DateFormatPipe();
  private formatCurrencyPipe: FormatCurrencyPipe = new FormatCurrencyPipe();
  private number: DecimalPipe = new DecimalPipe('en-US');

  static transformMapToArray(data: any, mapKeyType: MapKeyType = MapKeyType.Number): MapItem[] {
    return Object.keys(data || {}).map(key => {
      let value: any;
      switch (mapKeyType) {
        case MapKeyType.Number:
          value = Number(key) ?? 0;
          break;
        case MapKeyType.Boolean:
          value = key === 'true';
          break;
        case MapKeyType.String:
        default:
          value = key;
          break;
      }
      return { value, label: data[key] };
    });
  }

  transform(value: any, arg?: any): any {
    if (arg === undefined) {
      return value;
    }
    let type: string = arg;
    let param = '';

    if (arg.indexOf(':') !== -1) {
      type = arg.substring(0, arg.indexOf(':'));
      param = arg.substring(arg.indexOf(':') + 1, arg.length);
    }

    switch (type) {
      case 'date':
        return this.datePipe.transform(value, param) ?? "-";
      case 'currency':
        let currency = this.formatCurrencyPipe.transform(value, param, 'symbol', '1.0-0');
        return currency ? (currency + " VND") : "-";
      case 'currencyNumber':
        let currencyNumber = this.formatCurrencyPipe.transform(value, param, 'symbol', '1.0-0');
        return currencyNumber ? (currencyNumber) : "-";
      case 'number':
        let number = this.number.transform(value, '1.0-0');
        return number ? (number) : "-";
      case 'yn':
        return (value && value == 1) ? "Có" : "Không";
      default:
        // return (this.mapObj[type] ? this.mapObj[type][value] : '');
        return "-";
    }
  }
}
