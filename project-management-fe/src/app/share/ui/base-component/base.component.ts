import {
  ChangeDetectorRef,
  Component,
  Injector,
  OnDestroy,
} from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
// import { TranslateService } from '@ngx-translate/core';
// import { VtsToastService } from '@ui-vts-kit/ng-vts/toast';
import { Subscription } from 'rxjs';
// import { PvnTableConfig } from '../pvn-table/pvn-table.component';

@Component({
  template: `
    <ng-content></ng-content>`,
  standalone: true
})
export class BaseComponent implements OnDestroy {
  // tableConfig!: PvnTableConfig;
  listData: any = [];
  formSearch: FormGroup = new FormGroup({});
  // pageIndex = 1;
  // pageSize = 10;
  isSubmitted: boolean = false;

  protected fb: FormBuilder;
  protected router: Router;
  protected route: ActivatedRoute;
  protected ref: ChangeDetectorRef;
  protected state: any;
  // protected translate: TranslateService;
  // protected toast: VtsToastService;

  subscriptions: Subscription[] = [];

  constructor(private readonly injector: Injector) {
    this.fb = this.injector.get(FormBuilder);
    this.router = this.injector.get(Router);
    this.route = this.injector.get(ActivatedRoute);
    this.ref = this.injector.get(ChangeDetectorRef);
    // this.translate = this.injector.get(TranslateService);
    // this.toast = this.injector.get(VtsToastService);
    this.state = this.router.getCurrentNavigation()?.extras?.state;
  }

  handleDestroy() {}

  ngOnDestroy() {
    this.subscriptions?.forEach((sub) => {
      sub.unsubscribe();
    });
    this.handleDestroy();
  }
}
