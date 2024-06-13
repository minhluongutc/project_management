import {Component, Injector, OnInit, TrackByFunction} from '@angular/core';
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
import {NOTIFICATION_VALUE} from "../../../constants/data.constants";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss'
})
export class HeaderComponent extends BaseComponent implements OnInit {
  notifications: any[] = [];
  notificationsNotRead: any[] = [];
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
        this.notificationsNotRead = this.notifications.filter((item) => item.isRead == 0);
      }
    })
  }

  onLogout() {
    this.authService.logout();
  }

  protected readonly String = String;

  onReadNotify(data: any) {
    this.notificationService.readNotification(data.id).subscribe({
      next: res => {
        this.getNotification();
      }
    })
    this.router.navigate(['/tasks'], {queryParams: {taskCode: data.taskCode}});
  }

  listenNotification() {
    this.websocketService.getMessageSubject().subscribe((messages: any) => {
      console.log("message: ", messages)
      if(messages.userId == this.user.id) {
        this.createInfoToast("Thông báo", this.convertValueById(messages?.type, 'name', this.NOTIFICATION_VALUE))
        this.getNotification();
      }
      console.log(messages)
    })
  }

  protected readonly NOTIFICATION_VALUE = NOTIFICATION_VALUE;
  trackByFn: TrackByFunction<any> = (index, item) => item.id;
}
