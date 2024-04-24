import {Component, Injector, OnInit, ViewChild} from '@angular/core';
import {BaseComponent} from "../../../../share/ui/base-component/base.component";
import {CategoryService} from "../../../../service/category.service";
import {ConfirmationService} from "primeng/api";
import {ProjectUserService} from "../../../../service/project-user.service";
import {EditableRow} from "primeng/table";
import {PROFESSIONAL_LEVELS} from "../../../../share/constants/data.constants";
import {Category} from "../../../../models/category.model";
import {ProjectUser} from "../../../../models/project-user.model";

@Component({
  selector: 'app-permission',
  templateUrl: './users.component.html',
  styleUrl: './users.component.scss',
  providers: [EditableRow, ConfirmationService]
})
export class UsersComponent extends BaseComponent implements OnInit {
  projectId: any;
  keySearch: string = '';

  clonedValue!: ProjectUser

  constructor(injector: Injector,
              private projectUserService: ProjectUserService,
              private confirmationService: ConfirmationService,
  ) {
    super(injector);
    this.projectId = this.route.snapshot?.parent?.paramMap.get('id') || null;
  }

  ngOnInit() {
    this.getUsers();
  }

  getUsers() {
    this.projectUserService.getUserByProject({projectId: this.projectId}).subscribe({
      next: (res: any) => {
        this.listData = res.data;
      }
    })
  }

  deleteRow(item: any) {

  }

  onRowEditInit(item: any) {
    this.clonedValue = { ...item };
  }

  onRowEditSave(item: any) {
    const data: ProjectUser = {
      userId: item.userId,
      firstName: item.firstName,
      lastName: item.lastName,
      projectId: this.projectId,
      professionalLevel: item.professionalLevel
    };
    if (this.clonedValue.professionalLevel !== item.professionalLevel) {
      this.changeProfessionalLevelProject(data);
    }
  }

  onRowEditCancel(item: any) {
    this.getUsers()
  }

  confirmDelete(item: any, $event: MouseEvent) {

  }

  changeProfessionalLevelProject(data: any) {
    this.projectUserService.changeProfessionalLevelProject(data.userId, this.projectId, data.professionalLevel).subscribe({
      next: (res: any) => {
        this.getUsers();
        this.createSuccessToast('Thành công', 'Cập nhật cấp độ chuyên môn thành công');
      },
      error: (err: any) => {
        this.getUsers();
        this.createErrorToast('Lỗi', 'Cập nhật cấp độ chuyên môn thất bại');
      }
    });
  }

  findProfessionalLevelById(id: number) {
    return this.PROFESSIONAL_LEVELS.find(x => x.id === id)?.name;
  }

  protected readonly PROFESSIONAL_LEVELS = PROFESSIONAL_LEVELS;
}
