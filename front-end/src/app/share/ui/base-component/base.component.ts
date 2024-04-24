import {ChangeDetectorRef, Component, Injector, OnDestroy,} from '@angular/core';
import {ToastModule} from 'primeng/toast';
import {FormBuilder, FormGroup} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
// import { TranslateService } from '@ngx-translate/core';
// import { VtsToastService } from '@ui-vts-kit/ng-vts/toast';
import {Subscription} from 'rxjs';
import {MessageService, TreeNode} from "primeng/api";
import {AuthService} from "../../auth/auth.service";
import {User} from "../../auth/user.model";
import {DialogService} from "primeng/dynamicdialog";
import {DropdownChangeEvent} from "primeng/dropdown";

// import { PvnTableConfig } from '../pvn-table/pvn-table.component';

@Component({
  template: `
    <ng-content></ng-content>`,
  imports: [
    ToastModule
  ],
  standalone: true
})
export class BaseComponent implements OnDestroy {
  listDataTree: TreeNode[] = [];
  listData: any = [];
  user: User;
  form: FormGroup = new FormGroup({});
  first = 0;
  rows = 10;
  totalRecords = 0;
  isSubmitted: boolean = false;

  protected fb: FormBuilder;
  protected router: Router;
  protected route: ActivatedRoute;
  protected ref: ChangeDetectorRef;
  protected state: any;
  protected toast: MessageService;
  protected authService: AuthService;
  public dialogService: DialogService
  // protected translate: TranslateService;

  subscriptions: Subscription[] = [];

  constructor(private readonly injector: Injector) {
    this.fb = this.injector.get(FormBuilder);
    this.router = this.injector.get(Router);
    this.route = this.injector.get(ActivatedRoute);
    this.ref = this.injector.get(ChangeDetectorRef);
    // this.translate = this.injector.get(TranslateService);
    this.toast = this.injector.get(MessageService);
    this.state = this.router.getCurrentNavigation()?.extras?.state;
    this.authService = this.injector.get(AuthService);
    this.dialogService = this.injector.get(DialogService);

    this.user = this.authService.user.value
  }

  handleDestroy() {}

  createSuccessToast(summary: string, message: string) {
    console.log('ok2');
    this.toast.add({severity: 'success', summary: summary, detail: message});
  }

  createInfoToast(summary: string, message: string) {
    this.toast.add({severity: 'info', summary: summary, detail: message});
  }

  createWarningToast(summary: string, message: string) {
    this.toast.add({severity: 'warn', summary: summary, detail: message});
  }

  createErrorToast(summary: string, message: string) {
    this.toast.add({severity: 'error', summary: summary, detail: message});
  }

  showError(formControlName: string): boolean {
    const control = this.form.controls[formControlName];
    return !!(control.touched && control?.errors);
  }

  getError(formControlName: string) {
    const control = this.form.controls[formControlName];
    return control.touched ? control?.errors : undefined;
  }

  showErrorCustom(formControlName: string, formCustom: FormGroup): boolean {
    const control = formCustom.controls[formControlName];
    return !!(control.touched && control?.errors);
  }

  getErrorCustom(formControlName: string, formCustom: FormGroup) {
    const control = formCustom.controls[formControlName];
    return control.touched ? control?.errors : undefined;
  }

  closeDialog() {
    this.dialogService.dialogComponentRefMap.forEach(dialog => {
      dialog.destroy();
    });
  }

  updateDropdownValue($event: DropdownChangeEvent, formControlName: string) {
    console.log($event)
    this.form.controls[formControlName].setValue($event.value);
    console.log(formControlName)
    console.log(this.form.value)
  }

  convertValueById(id: any, name: any, array: any[]) {
    return array.find((item: any) => item.id === id)?.[name] === undefined ? 'N/A' : array.find((item: any) => item.id === id)?.[name];
  }

  getColorTag(value: any) {
    switch (value) {
      case 1:
        return '#1AADB5';
      case 2:
        return '#2B1AB6';
      case 3:
        return '#FDFD05';
      case 4:
        return '#FB9F00';
      case 5:
        return '#FD3F01';
      default:
        return 'transparent';
    }
  }

  ngOnDestroy() {
    this.subscriptions?.forEach((sub) => {
      sub.unsubscribe();
    });
    this.handleDestroy();
  }
}
