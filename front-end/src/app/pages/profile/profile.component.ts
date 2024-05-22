import {Component, Injector, OnInit} from '@angular/core';
import {BaseComponent} from "../../share/ui/base-component/base.component";
import {UserService} from "../../service/user.service";
import {GENDER, PERMISSION, PROFESSIONAL_LEVELS} from "../../share/constants/data.constants";
import {Validators} from "@angular/forms";
import {FileService} from "../../share/services/file.service";

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.scss'
})
export class ProfileComponent extends BaseComponent implements OnInit {
  profileUser: any;
  editMode: boolean = false;
  avatar: any;
  previewAvatar: any;
  cover: any;
  previewCover: any;

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
              private userService: UserService) {
    super(injector);
    this.buildForm();
    this.getUserById();
  }

  ngOnInit() {
  }

  buildForm() {
    this.form = this.fb.group({
      username: ['', Validators.required],
      email: ['', Validators.required],
      lastName: ['', Validators.required],
      firstName: ['', Validators.required],
      gender: ['', Validators.required],
      dateOfBirth: [''],
      address: [''],
      contact: ['', Validators.pattern('(03|05|07|08|09|01[2|6|8|9])+([0-9]{8})\\b')]
    });
  }

  getUserById(){
    this.userService.getUserById(this.user.id).subscribe({
      next: (res) => {
        console.log(res)
        this.profileUser = res.data;
        if (this.profileUser.avatarId)
          this.previewAvatar = this.getImage(this.profileUser.avatarId);
        if (this.profileUser.coverId)
          this.previewCover = this.getImage(this.profileUser.coverId);
      }
    })
  }

  startEditProfile() {
    this.form.patchValue(this.profileUser);
    this.form.controls['dateOfBirth'].setValue(new Date(this.profileUser.dateOfBirth));
    this.editMode = true;
  }

  onUpdateInfo() {
    const data: any = {
      ...this.form.value,
      dateOfBirth: this.form.value.dateOfBirth.toISOString()
    }

    const formData = new FormData();
    if (this.avatar != undefined)
      formData.append('avatar', this.avatar)
    if (this.cover != undefined)
      formData.append('cover', this.cover)
    formData.append(
      'dto',
      new Blob([JSON.stringify(data)], {type: 'application/json'})
    )
    this.userService.updateProfile(this.profileUser.id, formData).subscribe({
      next: (res) => {
        console.log(res)
        this.createSuccessToast('Thành công', 'Cập nhật thông tin thành công');
        this.editMode = false;
        this.getUserById();
      }, error: (err) => {
        console.log(err)
        this.createErrorToast('Lỗi', 'Cập nhật thông tin thất bại');
      }
    })
  }

  onChoseAvatar($event: Event) {
    // @ts-ignore
    this.avatar = (event.target as HTMLInputElement).files?.[0] || null;

    const reader = new FileReader();
    reader.readAsDataURL(this.avatar as File);
    reader.onload = (e: any) => {
      this.previewAvatar = e.target.result;
    };
  }

  createImage(file: File): any {
    const reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = (e: any) => {
      return e.target.result;
    };
  }

  onChoseCover($event: Event) {
    // @ts-ignore
    this.cover = (event.target as HTMLInputElement).files?.[0] || null;

    const reader = new FileReader();
    reader.readAsDataURL(this.cover as File);
    reader.onload = (e: any) => {
      this.previewCover = e.target.result;
    };
  }

  onCloseEditModal() {
    this.editMode = false;
  }

  protected readonly PERMISSION = PERMISSION;
  protected readonly PROFESSIONAL_LEVELS = PROFESSIONAL_LEVELS;
  protected readonly GENDER = GENDER;
}
