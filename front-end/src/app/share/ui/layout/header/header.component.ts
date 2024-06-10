import {Component, Injector, OnInit} from '@angular/core';
import {AuthService} from "../../../auth/auth.service";
import {User} from "../../../auth/user.model";
import {ActivatedRoute, Router} from "@angular/router";
import {Subscription} from "rxjs";
import {DialogService, DynamicDialogRef} from "primeng/dynamicdialog";
import {TaskCreateComponent} from "../../../../pages/tasks/task-create/task-create.component";
import {FileService} from "../../../services/file.service";
import {NotificationService} from "../../../../service/notification.service";
import {BaseComponent} from "../../base-component/base.component";
import {TaskService} from "../../../../service/task.service";
import {WebsocketService} from "../../../../service/websocket.service";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss'
})
export class HeaderComponent extends BaseComponent implements OnInit {
  notifications: any[] = [];
  notificationNotRead: string = "0";
  userSub: Subscription;
  isAuthenticated: boolean = false;
  userData: Partial<User> = {};

  dynamicDialogRef: DynamicDialogRef | undefined;
  constructor(
    injector: Injector,
    private notificationService: NotificationService,
    private taskService: TaskService,
    private websocketService: WebsocketService
  ) {
    super(injector);
    this.userSub = Subscription.EMPTY;
    // this.showCreateTask();
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
    console.log(this.route?.parent?.snapshot.paramMap.get('id'));
    console.log(this.route.snapshot?.parent?.paramMap.get('id'));
    console.log(this.route.snapshot)
    this.getNotification();
    this.websocketService.joinRoom("ABC");
    this.listenNotification();
  }

  showCreateTask() {
    this.dynamicDialogRef = this.dialogService.open(TaskCreateComponent, {
      header: 'Thêm mới công việc',
      width: '60vw',
      contentStyle: { overflow: 'auto', 'margin-bottom': '69px' },
      breakpoints: {
        '960px': '75vw',
        '640px': '90vw'
      },
    })
  }

  getNotification() {
    const dto = {
      userId: this.userData.id,
    }
    this.notificationService.getNotifications(dto).subscribe({
      next: res => {
        this.notifications = res.data;
        this.notificationNotRead = String(this.notifications.filter((item) => item.isRead == 0).length);
      }
    })
  }

  onLogout() {
    this.authService.logout();
  }

  protected readonly String = String;

  onReadNotify(id: any, taskCode: any) {
    this.notificationService.readNotification(id).subscribe({
      next: res => {
        this.getNotification();
      }
    })
    this.router.navigate(['/tasks'], {queryParams: {taskCode: taskCode}});
  }

  listenNotification() {
    this.websocketService.getMessageSubject().subscribe((messages: any) => {
      if(messages.userId == this.user.id) {
        this.createInfoToast("Thông báo", messages?.optionalContent)
        this.getNotification();
      }
      console.log(messages)
    })
  }
}
