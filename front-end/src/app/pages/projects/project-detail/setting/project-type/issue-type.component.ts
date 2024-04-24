import {Component, Injector, OnInit, ViewChild} from '@angular/core';
import {BaseComponent} from "../../../../../share/ui/base-component/base.component";
import {ConfirmationService} from "primeng/api";
import {IssueType} from "../../../../../models/issue-type.model";
import {TypeService} from "../../../../../service/type.service";
import {EditableRow} from "primeng/table";

@Component({
  selector: 'app-issue-type',
  templateUrl: './issue-type.component.html',
  styleUrl: './issue-type.component.scss',
  providers: [EditableRow, ConfirmationService]
})
export class IssueTypeComponent extends BaseComponent implements OnInit {
  projectId: any;
  keySearch: string = '';

  clonedValue!: IssueType

  @ViewChild('addButton') addButton: any;
  @ViewChild('focusButton') focusButton: any;

  constructor(injector: Injector,
              private typeService: TypeService,
              private confirmationService: ConfirmationService,
  ) {
    super(injector);
    this.projectId = this.route.snapshot?.parent?.paramMap.get('id') || null;
  }

  ngOnInit(): void {
    this.getTypes();
  }

  getTypes() {
    let data: any = {
      projectId: this.projectId
    }
    if (this.keySearch) {
      data = {...data, keySearch: this.keySearch.trim()}
    }
    this.typeService.getTypes(data).subscribe(res => {
      this.listData = res.data;
    });
  }

  onRowEditInit(item: any) {
    this.clonedValue = { ...item };
  }

  onRowEditSave(item: any) {
    console.log(this.clonedValue)
    const data: IssueType = {
      id: item.id,
      name: item.name.trim(),
      description: item.description.trim(),
      projectId: item.projectId
    };
    if (this.clonedValue.name !== item.name || this.clonedValue.description !== item.description) {
      item.id ? this.updateType(data) : this.createType(data);
    }
  }

  confirmDelete(item: any, event: Event) {
    console.log(event)
    this.confirmationService.confirm({
      target: event.target as EventTarget,
      message: 'bạn có muốn xóa loại công việc này?',
      header: 'Xác nhận xóa',
      icon: 'pi pi-info-circle',
      acceptButtonStyleClass: "p-button-danger p-button-text",
      rejectButtonStyleClass: "p-button-text p-button-text",
      acceptIcon: "none",
      rejectIcon: "none",

      accept: () => {
        this.deleteCategory(item.id)
      },
      reject: () => {
        console.log("is reject")
      }
    });
  }

  createType(item: IssueType) {
    this.typeService.createType(item).subscribe({
      next: (res) => {
        this.getTypes();
        this.createSuccessToast('Thành công','Thêm mới loại công việc thành công');
      },
      error: (err) => {
        this.createErrorToast('Lỗi', 'Thêm mới loại công việc thất bại');
      }
    });
  }

  updateType(item: IssueType) {
    this.typeService.updateType(item.id, item).subscribe({
      next: (res) => {
        this.getTypes();
        this.createSuccessToast('Thành công','Chỉnh sửa loại công việc thành công');
      },
      error: (err) => {
        this.createErrorToast('Lỗi', 'Chỉnh sửa loại công việc thất bại');
      }
    });
  }

  deleteCategory(id: string) {
    this.typeService.deleteType(id).subscribe({
      next: (res) => {
        this.getTypes();
        this.createSuccessToast('Thành công','Xóa loại công việc thành công');
      },
      error: (err) => {
        this.createErrorToast('Lỗi', 'Xóa loại công việc thất bại');
      }
    });
  }

  async addRow2Table() {
    const row: IssueType = {
      id: '',
      name: '',
      description: '',
      projectId: this.projectId
    }
    this.listData.unshift(row)
    setTimeout(() => {
      this.addButton.nativeElement.click();
      setTimeout(() => {
        this.focusButton.nativeElement.focus();
      }, 50);
    }, 50);
  }

  onRowEditCancel(item: any) {
    if (!item.id) {
      this.listData.shift();
    }
  }
}
