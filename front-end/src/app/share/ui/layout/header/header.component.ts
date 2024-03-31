import { Component } from '@angular/core';
import {Footer, SharedModule} from "primeng/api";
import {AvatarModule} from "primeng/avatar";
import {ButtonModule} from "primeng/button";
import {InputTextModule} from "primeng/inputtext";
import {ToolbarModule} from "primeng/toolbar";
import {SplitButtonModule} from "primeng/splitbutton";
import {AuthService} from "../../../auth/auth.service";
import {User} from "../../../auth/user.model";
import {Router} from "@angular/router";
import {Subscription} from "rxjs";
import {DialogService, DynamicDialogRef} from "primeng/dynamicdialog";
import {TaskCreateComponent} from "../../../../pages/tasks/task-create/task-create.component";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss'
})
export class HeaderComponent {
  userSub: Subscription;
  isAuthenticated: boolean = false;
  userData: Partial<User> = {};

  ref: DynamicDialogRef | undefined;
  constructor(
    private authService: AuthService,
    private router: Router,
    public dialogService: DialogService
  ) {
    this.userSub = Subscription.EMPTY;
  }

  ngOnInit(): void {
    this.userSub = this.authService.user.subscribe((user) => {
      this.isAuthenticated = !!user;
    });
    // @ts-ignore
    this.userData = JSON.parse(localStorage.getItem('userData'));
    if (!this.isAuthenticated) {
      this.router.navigate(['/login']);
    }
    this.showCreateTask();
  }

  showCreateTask() {
    this.ref = this.dialogService.open(TaskCreateComponent, {
      header: 'Thêm mới công việc',
      width: '60vw',
      contentStyle: { overflow: 'auto', 'margin-bottom': '69px' },
      breakpoints: {
        '960px': '75vw',
        '640px': '90vw'
      },
    })
  }

  onLogout() {
    this.authService.logout();
  }
}
