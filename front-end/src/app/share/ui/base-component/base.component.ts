import {
  ChangeDetectorRef,
  Component,
  Injector,
  OnDestroy,
} from '@angular/core';
import { ToastModule } from 'primeng/toast';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
// import { TranslateService } from '@ngx-translate/core';
// import { VtsToastService } from '@ui-vts-kit/ng-vts/toast';
import {BehaviorSubject, Subscription} from 'rxjs';
import {MessageService, TreeNode} from "primeng/api";
import {AuthService} from "../../auth/auth.service";
import {User} from "../../auth/user.model";
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
  // tableConfig!: PvnTableConfig;
  listDataTree: TreeNode[] = [];
  listData: any = [];
  user: User;
  form: FormGroup = new FormGroup({});
  // pageIndex = 1;
  // pageSize = 10;
  isSubmitted: boolean = false;

  protected fb: FormBuilder;
  protected router: Router;
  protected route: ActivatedRoute;
  protected ref: ChangeDetectorRef;
  protected state: any;
  protected toast: MessageService;
  protected authService: AuthService;
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

  ngOnDestroy() {
    this.subscriptions?.forEach((sub) => {
      sub.unsubscribe();
    });
    this.handleDestroy();
  }
}
