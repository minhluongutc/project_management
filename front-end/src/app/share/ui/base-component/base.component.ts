import {ChangeDetectorRef, Component, Injector, OnDestroy,} from '@angular/core';
import {ToastModule} from 'primeng/toast';
import {FormBuilder, FormGroup} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {Subscription} from 'rxjs';
import {MessageService, TreeNode} from "primeng/api";
import {AuthService} from "../../auth/auth.service";
import {User} from "../../auth/user.model";
import {DialogService} from "primeng/dynamicdialog";
import {DropdownChangeEvent} from "primeng/dropdown";
import {FileService} from "../../services/file.service";
import {ProjectStoreService} from "../../../pages/projects/project-store.service";
import {PERMISSIONS_ENUM} from "../../enum/permission.enum";
import {ProjectUserService} from "../../../service/project-user.service";


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
  avatarDefault: string = '/assets/images/image-default-user.jpg';

  protected fb: FormBuilder;
  protected router: Router;
  protected route: ActivatedRoute;
  protected ref: ChangeDetectorRef;
  protected state: any;
  protected toast: MessageService;
  protected authService: AuthService;
  protected fileService: FileService;
  protected projectStoreService: ProjectStoreService;
  protected projectUserService: ProjectUserService;
  public dialogService: DialogService;

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
    this.fileService = this.injector.get(FileService);
    this.projectStoreService = this.injector.get(ProjectStoreService);
    this.projectUserService = this.injector.get(ProjectUserService);
    this.dialogService = this.injector.get(DialogService);

    this.user = this.authService.user.value
    if(this.projectStoreService.id) {
      this.setRoleInProject(this.projectStoreService.id, this.user.id);
    }

  }

  findProject(list: any[], projectId: string): any {
    for (let item of list) {
      if (item.key === projectId) {
        return item;
      }

      if (item.children) {
        let found = this.findProject(item.children, projectId);
        if (found) {
          return found;
        }
      }
    }

    return null;
  }

  getFileType(fileType: string) {
    if (fileType == "pdf" || fileType == "application/pdf") {
      return "pdf";
    } else if (fileType == "docx" || fileType == "application/vnd.openxmlformats-officedocument.wordprocessingml.document") {
      return "docx";
    } else if (fileType == "pptx" || fileType == "application/vnd.openxmlformats-officedocument.presentationml.presentation") {
      return "pptx";
    } else if (fileType == "rar" || fileType == "application/x-compressed") {
      return "rar";
    } else if (fileType == "zip" || fileType == "application/x-zip-compressed") {
      return "zip";
    } else if (fileType.startsWith("image/") || fileType == "jpg" || fileType == "png") {
      return "image";
    } else if (fileType == "xlsx" || fileType == "application/vnd.ms-excel" || fileType == "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" || fileType == "xsl") {
      return "excel";
    } else if (fileType == "mp4" || fileType.startsWith("video/") || fileType == "video/mp4") {
      return "video";
    } else {
      return "other"
    }
  }

  compareDateTask(date: Date, warningTime: number, dangerTime: number) {
    const currentDate = new Date();
    const diffTime = Math.round(new Date(date).getTime() - currentDate.getTime());
    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
    if (diffDays <= dangerTime) {
      return 'danger';
    } else if (diffDays <= warningTime) {
      return 'warning';
    } else {
      return 'normal';
    }
  }

  compareCurrentTime(data: Date): boolean {
    const currentDate = new Date();
    const diffTime = Math.round(new Date(data).getTime() - currentDate.getTime());
    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
    return diffDays < 0;
  }

  handleDestroy() {}

  createSuccessToast(summary: string, message: string) {
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

  showErrorCustom(formCustom: FormGroup, formControlName: string): boolean {
    const control = formCustom.controls[formControlName];
    return !!(control.touched && control?.errors);
  }

  getErrorCustom(formCustom: FormGroup, formControlName: string) {
    const control = formCustom.controls[formControlName];
    return control.touched ? control?.errors : undefined;
  }

  closeDialog() {
    this.dialogService.dialogComponentRefMap.forEach(dialog => {
      dialog.destroy();
    });
  }

  updateDropdownValue($event: DropdownChangeEvent, formControlName: string) {
    this.form.controls[formControlName].setValue($event.value);
  }

  updateDropdownValueCustom($event: DropdownChangeEvent, formCustom: FormGroup, formControlName: string) {
    formCustom.controls[formControlName].setValue($event.value);
  }

  convertValueById(id: any, name: any, array: any[]) {
    return array.find((item: any) => item.id === id)?.[name] === undefined ? 'N/A' : array.find((item: any) => item.id === id)?.[name];
  }

  isRoleUser(): boolean {
    return this.projectStoreService.role === PERMISSIONS_ENUM.USER;
  }

  isRoleLeader(): boolean {
    return this.projectStoreService.role === PERMISSIONS_ENUM.LEADER;
  }

  isRoleProjectManager(): boolean {
    return this.projectStoreService.role === PERMISSIONS_ENUM.PROJECT_MANAGER;
  }

  isRoleAdmin(): boolean {
    return this.projectStoreService.role === PERMISSIONS_ENUM.ADMIN;
  }

  isRolePMOrAdmin(): boolean {
    console.log(this.projectStoreService.id)
    console.log(this.isRoleProjectManager(), this.isRoleAdmin());
    return this.isRoleProjectManager() || this.isRoleAdmin();
  }

  async setRoleInProject(projectId: string, userId: string) {
    try {
      const res: any = await this.projectUserService.getRoleInProject(projectId, userId).toPromise();
      this.projectStoreService.role = res.data;
      console.log(this.projectStoreService.role);
    } catch (err) {
      this.createErrorToast("Lỗi", "Không tìm thấy dự án");
    }
  }

  getColorTag(value: any) {
    switch (value) {
      case 1:
        return '#1AADB5';
      case 2:
        return '#2B1AB6';
      case 3:
        return '#2f87f2';
      case 4:
        return '#FB9F00';
      case 5:
        return '#FD3F01';
      default:
        return 'transparent';
    }
  }

  generateColor(i: number): string {
    switch (i) {
      case 0://new
        return '#68c3ab';
      case 1://confirmed
        return 'rgba(234,232,232,0.57)';
      case 2://deploy waiting
        return '#34d399';
      case 3://resolve
        return '#c084fc';
      case 4://reopen
        return '#60a5fa';
      case 5://done
        return '#26b928';
      case 6://reject
        return '#ff0000';
      case 7:
        return '#399bab';
      case 8:
        return '#43860c';
      case 9:
        return '#68b7ab';
      case 10:
        return '#6653ab';
      case 11:
        return '#17815c';
      case 12:
        return '#5862d0';
      case 13:
        return '#fbbf24';
      case 14:
        return '#43641f';
      case 15:
        return '#1e2cc4';
      case 16:
        return '#074232';
      case 17:
        return '#4122b2';
      case 18:
        return '#6aec8f';
      case 19:
        return '#0e3405';
      case 20:
        return '#b2eec5';
      case 21:
        return '#2a5730';
      default:
        return '#000000';
    }
  }

  getImage(id: any): string {
    if (!id) return this.avatarDefault;
    return this.fileService.getFileUrl(id) || this.avatarDefault;
  }

  ngOnDestroy() {
    this.subscriptions?.forEach((sub) => {
      sub.unsubscribe();
    });
    this.handleDestroy();
  }
}
