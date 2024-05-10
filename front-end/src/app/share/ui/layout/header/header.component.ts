import {Component, OnInit} from '@angular/core';
import {AuthService} from "../../../auth/auth.service";
import {User} from "../../../auth/user.model";
import {ActivatedRoute, Router} from "@angular/router";
import {Subscription} from "rxjs";
import {DialogService, DynamicDialogRef} from "primeng/dynamicdialog";
import {TaskCreateComponent} from "../../../../pages/tasks/task-create/task-create.component";
import {FileService} from "../../../services/file.service";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss'
})
export class HeaderComponent implements OnInit {
  userSub: Subscription;
  isAuthenticated: boolean = false;
  userData: Partial<User> = {};

  ref: DynamicDialogRef | undefined;
  constructor(
    protected authService: AuthService,
    private fileService: FileService,
    private router: Router,
    private route: ActivatedRoute,
    public dialogService: DialogService
  ) {
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

  getImage(id: any) {
    return this.fileService.getFileUrl(id) || '/assets/images/image-default-user.jpg';
  }

  onLogout() {
    this.authService.logout();
  }
}
