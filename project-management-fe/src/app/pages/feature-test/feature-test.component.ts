import {Component, Injector, OnInit} from '@angular/core';
import {ButtonModule} from "primeng/button";
import {ToastModule} from "primeng/toast";
import {MessageService} from "primeng/api";
import {BaseComponent} from "../../share/ui/base-component/base.component";
import {FormGroup, FormsModule, NgControl, ReactiveFormsModule, Validators} from "@angular/forms";
import {InputTextModule} from "primeng/inputtext";
import {JiraInputTextComponent} from "../../share/ui/jira-input-text/jira-input-text.component";
import {DecimalPipe} from "@angular/common";
import {InputNumberModule} from "primeng/inputnumber";

@Component({
  selector: 'app-feature-test',
  standalone: true,
  templateUrl: './feature-test.component.html',
  imports: [
    ButtonModule,
    ToastModule,
    FormsModule,
    InputTextModule,
    JiraInputTextComponent,
    ReactiveFormsModule,
    JiraInputTextComponent,
    InputNumberModule
  ],
  styleUrl: './feature-test.component.scss',
  providers: [MessageService, DecimalPipe]
})
export class FeatureTestComponent extends BaseComponent implements OnInit {
  value: string = '';
  value2: string = '';
  jiraForm: FormGroup = new FormGroup({});
  jiraForm2: FormGroup = new FormGroup({});
  blur:boolean = true;
  constructor(injector: Injector) {
    super(injector);
  }

  ngOnInit() {
    this.jiraForm2 = this.fb.group({
      fieldText: [null, [Validators.required, Validators.minLength(10)]],
      fieldNumber: [null, [Validators.required, Validators.min(10)]]
    })
    this.jiraForm = this.fb.group({
      field: ['', Validators.required]
    })
    console.log('login page');
  }
  showError(formControlName: string): boolean {
    console.log('showError', this.jiraForm2.controls['fieldNumber'])
    const control = this.jiraForm2.controls[formControlName];
    return !!(control.touched && control?.errors);
  }

  getError(formControlName: string) {
    const control = this.jiraForm2.controls[formControlName];
    console.log('getError', control.touched)
    // debugger
    // console.log('getError', control.touched ? control?.errors : undefined)
    return control.touched ? control?.errors : undefined;
  }

  checkBlur() {
    this.blur = true;
  }
}
