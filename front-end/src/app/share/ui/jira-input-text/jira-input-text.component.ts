import {
  AfterViewInit,
  Component, ElementRef,
  EventEmitter,
  forwardRef,
  Injector,
  Input,
  OnChanges,
  OnInit,
  Output, Renderer2,
  SimpleChanges, ViewChild
} from '@angular/core';
import {InputTextModule} from "primeng/inputtext";
import {PaginatorModule} from "primeng/paginator";
import {ControlValueAccessor, NG_VALUE_ACCESSOR, NgControl} from "@angular/forms";
import {noop} from "rxjs";
import {DecimalPipe, NgIf} from "@angular/common";
import {InputTextareaModule} from "primeng/inputtextarea";
import {AutofocusDirective} from "../../directives/auto-focus.directive";

export class ModelInput {
  groupType: 'noIcon' | 'icon' | 'textareaInputSpecial' | 'textarea' = 'noIcon';
  type: 'default' | 'warning' | 'error' | 'success' = 'default';
  labelText?: string;
  textMessageValue?: string;
  showIcon: boolean = true;
  placeholder: string = '';
  rows?: number;
  disabled: boolean = false;
  autofocus: boolean = false;
  showFlexEnd: boolean = true;
  isInputText: boolean = false;
  isInputNumber: boolean = false;
  isDatepicker: boolean = false;
  isYearPicker: boolean = false;
  isTimepicker: boolean = false;
  dateConfig?: any;
  timeConfig?: any;
  showError: boolean = false;
  numberConfig: NumberConfig = new NumberConfig();
}

class NumberConfig {
  max: number = Infinity;
  min: number = -Infinity;
  formatter?: (value: number | string) => string | number;
  suffix?: string;
  prefix?: string;
}

@Component({
  selector: 'jira-input-text',
  templateUrl: './jira-input-text.component.html',
  styleUrl: './jira-input-text.component.scss',
  standalone: true,
  imports: [
    InputTextModule,
    PaginatorModule,
    NgIf,
    InputTextareaModule,
    AutofocusDirective
  ],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      multi: true,
      useExisting: forwardRef(() => JiraInputTextComponent),
    } /*,
    {
      provide: NG_VALIDATORS,
      useExisting: forwardRef(() => PvnInputTextComponent),
      multi: true
    }*/,
  ],

})
export class JiraInputTextComponent
  implements OnInit, ControlValueAccessor, OnChanges, AfterViewInit {

  // input type
  @Input() jiraIsInputText = false;
  @Input() jiraIsInputNumber = false;
  @Input() jiraIsDatepicker = false;
  @Input() jiraIsYearPicker = false;
  @Input() jiraIsTimepicker = false;

  // config input number
  @Input() jiraPrefix: string = '';
  @Input() jiraSuffix: string = '';
  @Input() jiraMin: number = 0;
  @Input() jiraMax: number = 100000000000000;

  @Input() jiraConfig: ModelInput = new ModelInput();
  @Input() jiraLabelText: string = '';
  @Input() jiraPlaceholder = '';
  @Input() jiraDisabled = false;
  // @Input() jiraIsTextAreaInputSpecial = false;
  // @Input() jiraIsTextAreaAutoResize = false;
  @Input() jiraShowFlexEnd = true;
  @Input() jiraAutofocus = false;
  @Input() pvnId = 'demo';
  @Input() jiraIsTextArea = false;
  @Input() jiraDatepickerConfig: any = null;
  @Input() jiraTimepickerConfig: any = null;
  @Input() jiraErrorDefs: { errorName: string; errorDescription: string }[] = [];
  @Input() jiraShowIconMessage = true;
  @Input() jiraShowError = false;
  @Input() jiraErrors: any = null;
  @Input() minNumber: string = '';
  @Input() maxNumber: number = 10000000000000;
  @Input() jiraMaxLength: number = 10000000000000;
  @Input() jiraMinLength: number = 0;
  @Input() jiraRequired = false;
  @Input() jiraReadOnly = false;
  @Input() removeMinHeight: boolean = false;
  @Input() defaultInput?: boolean = false;
  @Input() isFormatterCurrency = false;
  @Input() jiraTextAreaMaxLength = 500;
  @Input() jiraShowLabel = true;
  @Input() jiraShowHelperText = true;

  @Output() jiraBlur: EventEmitter<any> = new EventEmitter<any>();
  @Output() jiraKeyup: EventEmitter<any> = new EventEmitter<any>();
  @Output() jiraChange: EventEmitter<any> = new EventEmitter<any>();
  @Output() jiraClick: EventEmitter<any> = new EventEmitter<any>();
  @Output() jiraFocus: EventEmitter<any> = new EventEmitter<any>();

  onTouched: () => void = noop;
  onChange: (_: any) => void = noop;
  ngControl?: NgControl;
  value: any = null;
  inputTextClass: string = '';
  textError: string = '';

  @ViewChild('focus') focus!: ElementRef;

  constructor(private inj: Injector,
              private decimalPipe: DecimalPipe,
              private renderer: Renderer2
  ) {}

  ngOnInit(): void {
    this.ngControl = this.inj.get(NgControl);
    this.getInputType();
    this.jiraErrorDefs = [
      {
        errorName: 'required',
        errorDescription: `${this.jiraLabelText} không được để trống`,
      },
      {
        errorName: 'min',
        errorDescription: `${this.jiraLabelText} không được nhỏ hơn `,
      },
      {
        errorName: 'max',
        errorDescription: `${this.jiraLabelText} không được lớn hơn `,
      },
      {
        errorName: 'minlength',
        errorDescription: `Độ dài nhập liệu tối thiểu là `,
      },
      {
        errorName: 'maxlength',
        errorDescription: `Độ dài nhập liệu tối đa là `,
      },
      {
        errorName: 'pattern',
        errorDescription: `Định dạng không hợp lệ`,
      },
      ...this.jiraErrorDefs,
    ];
  }

  ngOnChanges(changes: SimpleChanges): void {
    this.inputTextClass = '';
    this.textError = '';
    this.setJiraConfig();
    this.setErrorMessage();
  }

  ngAfterViewInit() {
    if (this.jiraAutofocus) {
      this.pvnId = 'focus';
      setTimeout(() => {
        let elem = this.renderer.selectRootElement('#focus');
        elem.focus();
      }, 1000);
    }
  }

  writeValue(obj: any): void {
    this.value = obj || ''; // fix ô textarea hiện chứ undefined
    if ((this.jiraIsDatepicker || this.jiraIsTimepicker) && this.value) {
      this.value = new Date(this.value);
    }
    this.onChange(this.value);
  }

  registerOnChange(fn: any): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: any): void {
    this.onTouched = fn;
  }

  setDisabledState?(isDisabled: boolean): void {
    this.inputTextClass = '';
    this.setJiraConfig();
    this.setErrorMessage();
  }

  setJiraConfig() {
    if (this.jiraLabelText) {
      this.jiraConfig.labelText = this.jiraLabelText;
    }
    if (this.jiraPlaceholder) {
      this.jiraConfig.placeholder = this.jiraPlaceholder;
    }
    this.jiraConfig.disabled = this.jiraDisabled;
    if (this.jiraIsTextArea) {
      this.jiraConfig.groupType = 'textarea';
    }
    // if (this.jiraIsTextAreaInputSpecial) {
    //   this.jiraConfig.groupType = 'textareaInputSpecial';
    // }
    if (!this.jiraShowFlexEnd) {
      this.jiraConfig.showFlexEnd = this.jiraShowFlexEnd;
    }
    if (this.jiraIsInputText) {
      this.jiraConfig.isInputText = this.jiraIsInputText;
    }
    if (this.jiraIsInputNumber) {
      this.jiraConfig.isInputNumber = this.jiraIsInputNumber;
      if (this.jiraMin) {
        this.jiraConfig.numberConfig.min = this.jiraMin;
      }
      if (this.jiraMax) {
        this.jiraConfig.numberConfig.max = this.jiraMax;
      }
      if (this.jiraPrefix) {
        this.jiraConfig.numberConfig.prefix = this.jiraPrefix;
      }
      if (this.jiraSuffix) {
        this.jiraConfig.numberConfig.suffix = this.jiraSuffix;
      }
    }
    if (this.jiraIsDatepicker) {
      this.jiraConfig.isDatepicker = this.jiraIsDatepicker;
    }
    if (this.jiraIsYearPicker) {
      this.jiraConfig.isYearPicker = this.jiraIsYearPicker;
    }
    if (this.jiraIsTimepicker) {
      this.jiraConfig.isTimepicker = this.jiraIsTimepicker;
    }
    this.jiraConfig.showError = this.jiraShowError;
    this.jiraConfig.showIcon = this.jiraShowIconMessage;
    if (this.jiraDatepickerConfig) {
      this.jiraConfig.dateConfig = this.jiraDatepickerConfig;
    } else {
      this.jiraConfig.dateConfig = {
        dateInputFormat: 'dd/MM/yyyy',
        returnFocusToInput: true,
      };
    }
    // this.jiraConfig.autofocus = this.jiraAutofocus;
  }

  setErrorMessage() {
    if (this.jiraErrors) {
      for (const error of this.jiraErrorDefs) {
        const key = error.errorName;
        if (this.jiraErrors[key]) {
          this.jiraConfig.textMessageValue = error.errorDescription;

          if (key === 'minlength' && this.useDefault(key, this.jiraErrorDefs)) {
            this.jiraConfig.textMessageValue +=
              this.jiraErrors[key].requiredLength;
          } else if (
            key === 'maxlength' &&
            this.useDefault(key, this.jiraErrorDefs)
          ) {
            this.jiraConfig.textMessageValue +=
              this.jiraErrors[key].requiredLength;
          } else if (key === 'min' && this.useDefault(key, this.jiraErrorDefs)) {
            this.jiraConfig.textMessageValue += this.jiraMin;
          } else if (key === 'max' && this.useDefault(key, this.jiraErrorDefs)) {
            this.jiraConfig.textMessageValue += this.jiraMax;
          }

          this.inputTextClass = 'input__text--error';
          this.textError = 'ng-invalid ng-dirty';
        }
      }
    }
  }

  useDefault(key: string, arr: any[]) {
    let count = 0;

    for (let item of arr) {
      if (item.errorName === key) {
        count++;

        if (count === 2) {
          return false;
        }
      }
    }

    // Return true if count is less than 2
    return true;
  }

  onInputChange($event: any) {
    this.onChange($event);
    this.jiraChange.emit($event);
  }

  inputBlur($event: FocusEvent) {
    this.onTouched();
    this.jiraBlur.emit($event);
  }

  inputClick($event: any, value: string) {
    this.jiraClick.emit($event);
    this.onChange(this.value);
  }

  inputFill($event: any) {
    this.jiraKeyup.emit($event);
  }

  preventTypeSpecialSymbol(event: any) {
    if (+this.minNumber >= 0 && (event.key === '-' || event.key === '+')) {
      event.preventDefault();
      return;
    }
  }

  onPaste(event: any) {
    const clipboardData = event.clipboardData;
    const pastedText = clipboardData.getData('text');
    if (
      +this.minNumber >= 0 &&
      (pastedText.includes('-') || pastedText.includes('+'))
    ) {
      event.preventDefault();
      return;
    }
  }

  inputFocus($event: FocusEvent) {
    this.jiraFocus.emit($event);
  }

  formatterCurrency = (value: number): string => {
    if (!Number.isInteger(value)) {
      return '';
    }
    return this.decimalPipe.transform(value, '1.0-2')!;
  };

  parserCurrency = (value: string): string => {
    if (!value) {
      return '';
    }
    return value.replace(/\D+/g, '');
  }


  getInputType(): void {
  if (this.jiraIsInputText) {
    this.jiraIsInputText = true;
  } else if (this.jiraIsInputNumber) {
    this.jiraIsInputNumber = true;
  } else if (this.jiraIsDatepicker) {
    this.jiraIsDatepicker = true;
  } else if (this.jiraIsYearPicker) {
    this.jiraIsYearPicker = true;
  } else if (this.jiraIsTimepicker) {
    this.jiraIsTimepicker = true;
  } else {
    this.jiraIsInputText = true;
  }

}
}
