import {Component, Injector, OnInit} from '@angular/core';
import {BaseComponent} from "../../../../share/ui/base-component/base.component";
import {ConfirmationService} from "primeng/api";
import {ProjectUserService} from "../../../../service/project-user.service";
import {EditableRow} from "primeng/table";
import {GENDER, PERMISSION, PROFESSIONAL_LEVELS} from "../../../../share/constants/data.constants";
import {ProjectUser} from "../../../../models/project-user.model";
import {FormGroup, Validators} from "@angular/forms";
import {existingUsernameValidator} from "../../../../share/validators/existing-username.validator";
import {UserService} from "../../../../service/user.service";
import {existingEmailValidator} from "../../../../share/validators/existing-email.validator";
import {existingEmailInProjectValidator} from "../../../../share/validators/existing-email-in-project.validator";

@Component({
  selector: 'app-permission',
  templateUrl: './users.component.html',
  styleUrl: './users.component.scss',
  providers: [EditableRow, ConfirmationService]
})
export class UsersComponent extends BaseComponent implements OnInit {
  addUserForm: FormGroup = new FormGroup({});

  projectId: any;
  keySearch: string = '';

  visibleDialogCreateUser: boolean = false;
  visibleDialogAddUser: boolean = false;

  clonedValue!: ProjectUser
  contactDefineErrors = [
    {
      errorName: 'pattern',
      errorDescription: 'Số điện thoại không hợp lệ'
    }
  ]
  usernameDefineErrors = [
    {
      errorName: 'existingUsername',
      errorDescription: 'Tên đăng nhập đã tồn tại'
    }
  ]
  emailDefineErrors = [
    {
      errorName: 'existingEmailInProject',
      errorDescription: 'Tài khoản sử dụng email đã có trong dự án'
    },
    {
      errorName: 'existingEmail',
      errorDescription: 'Email đăng ký đã tồn tại'
    },
    {
      errorName: 'email',
      errorDescription: 'Email không hợp lệ'
    }
  ]

  constructor(injector: Injector,
              private confirmationService: ConfirmationService,
              private userService: UserService,
  ) {
    super(injector);
    this.projectId = this.route.snapshot?.parent?.paramMap.get('id') || null;
  }

  ngOnInit() {
    this.getUsers();
    this.buildCreateUserForm();
    this.buildAddUserForm();
  }

  buildCreateUserForm() {
    this.form = this.fb.group({
      email: [
        '',
        {
          validators: [Validators.required, Validators.email],
          asyncValidators: [existingEmailValidator(this.userService)]
        }
      ],
      username: [
        '',
        {
          validators: [Validators.required],
          asyncValidators: [existingUsernameValidator(this.userService)]
        }
      ],
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      gender: ['', Validators.required],
      dateOfBirth: [''],
      address: [''],
      professionalLevel: [1, Validators.required],
      permission: ['USER', Validators.required],
      contact: ['', Validators.pattern('(03|05|07|08|09|01[2|6|8|9])+([0-9]{8})\\b')]
    });
  }

  buildAddUserForm() {
    this.addUserForm = this.fb.group({
      email: [
        '',
        {
          validators: [Validators.required, Validators.email],
          asyncValidators: [existingEmailInProjectValidator(this.userService, this.projectId)]
        }
      ],
      permission: [1, Validators.required],
      professionalLevel: [1, Validators.required]
    })
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
      professionalLevel: item.professionalLevel,
      permission: item.permission,
    };
    if (this.clonedValue.professionalLevel !== item.professionalLevel || this.clonedValue.permission !== item.permission) {
      this.changeProfessionalLevelAndPermissionProject(data);
    }
  }

  onRowEditCancel(item: any) {
    this.getUsers()
  }

  changeProfessionalLevelAndPermissionProject(data: any) {
    this.projectUserService.changeProfessionalLevelAndPermissionProject(data.userId, this.projectId, data.professionalLevel, data.permission).subscribe({
      next: (res: any) => {
        this.getUsers();
        this.createSuccessToast('Thành công', 'Cập nhật thông tin thành viên thành công');
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

  findPermissionById(id: number) {
    return this.PERMISSION.find(x => x.id === id)?.name;
  }

  onVisibleDialogCreateUser() {
    this.visibleDialogCreateUser = true;
  }

  onVisibleDialogAddUser() {
    this.visibleDialogAddUser = true;
  }

  handleCreateUser() {
    console.log(this.form.value)
    this.projectUserService.createAndAddUserToProject(
      [{projectId: this.projectId, ...this.form.value}]
    ).subscribe({
      next: (res: any) => {
        this.getUsers();
        this.resetCreateForm();
        this.createSuccessToast('Thành công', 'Thêm người dùng vào dự án thành công');
        this.visibleDialogCreateUser = false;
      },
      error: (err: any) => {
        this.createErrorToast('Lỗi', 'Thêm người dùng vào dự án thất bại');
      }
    })
  }

  onCloseModalAddUser() {
    this.resetAddForm();
    this.visibleDialogAddUser = false;
  }

  handleAddUser() {
    console.log(this.form.value)
    const data: any = {
      projectId: this.projectId,
      emails: [this.addUserForm.value.email],
      permission: this.addUserForm.value.permission,
      professionalLevel: this.addUserForm.value.professionalLevel
    }
    this.projectUserService.addUserToProject(data)
      .subscribe({
      next: (res: any) => {
        this.getUsers();
        this.resetAddForm();
        this.createSuccessToast('Thành công', 'Thêm người dùng vào dự án thành công');
        this.visibleDialogCreateUser = false;
      },
      error: (err: any) => {
        this.createErrorToast('Lỗi', 'Thêm người dùng vào dự án thất bại');
      }
    })
  }

  onCloseModalCreateUser() {
    this.resetCreateForm();
    this.visibleDialogCreateUser = false;
  }

  resetCreateForm() {
    this.form.reset({
      email: '',
      username: '',
      firstName: '',
      lastName: '',
      gender: '',
      dateOfBirth: '',
      address: '',
      professionalLevel: 1,
      permission: 1,
      contact: ''
    });
  }

  resetAddForm() {
    this.addUserForm.reset({
      email: '',
      professionalLevel: 1,
      permission: 1,
    });
  }

  confirmDelete(item: any, event: Event) {
    console.log(event)
    this.confirmationService.confirm({
      target: event.target as EventTarget,
      message: `bạn có muốn chắc xóa <strong>${item.firstName}</strong> ra khỏi dự án?`,
      header: 'Xác nhận xóa',
      icon: 'pi pi-info-circle',
      acceptButtonStyleClass: "p-button-danger p-button-text",
      rejectButtonStyleClass: "p-button-text p-button-text",
      acceptIcon: "none",
      rejectIcon: "none",

      accept: () => {
        // this.deleteCategory(item.id)
      },
      reject: () => {
        console.log("is reject")
      }
    });
  }

  protected readonly PROFESSIONAL_LEVELS = PROFESSIONAL_LEVELS;
  protected readonly GENDER = GENDER;
  protected readonly event = event;
  protected readonly PERMISSION = PERMISSION;
}
