import {Component, Injector, OnInit, ViewChild} from '@angular/core';
import {EditableRow} from "primeng/table";
import {ConfirmationService} from "primeng/api";
import {BaseComponent} from "../../../../../share/ui/base-component/base.component";
import {Category} from "../../../../../models/category.model";
import {CategoryService} from "../../../../../service/category.service";
import {StatusIssue} from "../../../../../models/status-issue.model";
import {StatusIssueService} from "../../../../../service/status-issue.service";

@Component({
  selector: 'app-status-issue',
  templateUrl: './status-issue.component.html',
  styleUrl: './status-issue.component.scss',
  providers: [EditableRow, ConfirmationService]
})
export class StatusIssueComponent  extends BaseComponent implements OnInit {
  projectId: any;
  keySearch: string = '';

  clonedValue!: StatusIssue

  @ViewChild('addButton') addButton: any;
  @ViewChild('focusButton') focusButton: any;

  constructor(injector: Injector,
              private statusIssueService: StatusIssueService,
              private confirmationService: ConfirmationService,
  ) {
    super(injector);
    this.projectId = this.route.snapshot?.parent?.paramMap.get('id') || null;
  }

  ngOnInit(): void {
    this.getStatusIssue();
  }

  getStatusIssue() {
    let data: any = {
      projectId: this.projectId
    }
    if (this.keySearch) {
      data = {...data, keySearch: this.keySearch.trim()}
    }
    this.statusIssueService.getStatusIssue(data).subscribe(res => {
      this.listData = res.data;
    });
  }

  onRowEditInit(item: any) {
    this.clonedValue = { ...item };
  }

  onRowEditSave(item: any) {
    console.log(this.clonedValue)
    const data: StatusIssue = {
      id: item.id,
      name: item.name.trim(),
      description: item.description.trim(),
      projectId: item.projectId
    };
    if (this.clonedValue.name !== item.name || this.clonedValue.description !== item.description) {
      item.id ? this.updateStatusIssue(data) : this.createStatusIssue(data);
    }
  }

  confirmDelete(item: any, event: Event) {
    console.log(event)
    this.confirmationService.confirm({
      target: event.target as EventTarget,
      message: 'bạn có muốn trạng thái này?',
      header: 'Xác nhận xóa',
      icon: 'pi pi-info-circle',
      acceptButtonStyleClass: "p-button-danger p-button-text",
      rejectButtonStyleClass: "p-button-text p-button-text",
      acceptIcon: "none",
      rejectIcon: "none",

      accept: () => {
        this.deleteStatusIssue(item.id)
      },
      reject: () => {
        console.log("is reject")
      }
    });
  }

  createStatusIssue(item: StatusIssue) {
    this.statusIssueService.createStatusIssue(item).subscribe({
      next: (res) => {
        this.getStatusIssue();
        this.createSuccessToast('Thành công','Thêm mới trạng thái công việc thành công');
      },
      error: (err) => {
        this.createErrorToast('Lỗi', 'Thêm mới trạng thái công việc thất bại');
      }
    });
  }

  updateStatusIssue(item: StatusIssue) {
    this.statusIssueService.updateStatusIssue(item.id, item).subscribe({
      next: (res) => {
        this.getStatusIssue();
        this.createSuccessToast('Thành công','Chỉnh sửa trạng thái công việc thành công');
      },
      error: (err) => {
        this.createErrorToast('Lỗi', 'Chỉnh sửa trạng thái công việc thất bại');
      }
    });
  }

  deleteStatusIssue(id: string) {
    this.statusIssueService.deleteStatusIssue(id).subscribe({
      next: (res) => {
        this.getStatusIssue();
        this.createSuccessToast('Thành công','Xóa trạng thái công việc thành công');
      },
      error: (err) => {
        this.createErrorToast('Lỗi', 'Xóa trạng thái công việc thất bại');
      }
    });
  }

  async addRow2Table() {
    const row: Category = {
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
