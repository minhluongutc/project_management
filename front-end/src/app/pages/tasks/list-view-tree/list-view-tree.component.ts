import {Component, Injector, OnInit} from '@angular/core';
import {BaseComponent} from "../../../share/ui/base-component/base.component";
import {TaskService} from "../../../service/task.service";
import {Column} from "../../../models/column.model";
import {last} from "rxjs";
import {PRIORIES, SEVERITIES} from "../../../share/constants/data.constants";
import {ParamMap} from "@angular/router";
import {TaskCreateComponent} from "../task-create/task-create.component";
import {DynamicDialogRef} from "primeng/dynamicdialog";
import {ConfirmationService} from "primeng/api";

@Component({
  selector: 'app-list-view-tree',
  templateUrl: './list-view-tree.component.html',
  styleUrl: './list-view-tree.component.scss'
})
export class ListViewTreeComponent extends BaseComponent implements OnInit {
  cols!: Column[];
  dynamicDialogRef: DynamicDialogRef | undefined;

  constructor(injector: Injector,
              private taskService: TaskService,
              private confirmationService: ConfirmationService,
  ) {
    super(injector);
  }

  async ngOnInit() {
    this.cols = [
      {
        field: 'taskCode',
        header: 'Mã công việc'
      },
      {
        field: 'projectName',
        header: 'Dự án'
      },
      {
        field: 'subject',
        header: 'Tổng quan'
      },
      {
        field: 'statusIssueName',
        header: 'Trạng thái'
      },
      {
        field: 'priority',
        header: 'Độ ưu tiên'
      },
      {
        field: 'severity',
        header: 'Độ nghiêm trọng'
      },
      {
        field: 'assignUserName',
        header: 'Người được giao'
      },
      {
        field: 'reviewUserName',
        header: 'Người giám sát'
      },
      {
        field: 'categoryName',
        header: 'Danh mục'
      },
      {
        field: 'startDate',
        header: 'Ngày bắt đầu'
      },
      {
        field: 'dueDate',
        header: 'Ngày kết thúc'
      },
      // {
      //   field: 'createTime',
      //   header: 'Ngày tạo'
      // },
      // {
      //   field: 'updateTime',
      //   header: 'Ngày cập nhật'
      // },
      // {
      //   field: 'createUserName',
      //   header: 'Người tạo'
      // },
      // {
      //   field: 'updateUserName',
      //   header: 'Người cập nhật'
      // },
      {
        field: '',
        header: 'Thao tác'
      }
    ];
    this.route.queryParamMap.subscribe((params: ParamMap) => {
      console.log('params:', params);
      this.getTasks(params);
    })
  }

  async getTasks(queryParams: any) {
    try {
      console.log(queryParams.params)
      const res: any = await this.taskService.getTaskAccordingLevel(queryParams.params).toPromise();
      console.log('res:', res);
      this.listDataTree = res.data;
    } catch (err: any) {
      this.createErrorToast('Lỗi', err.message);
    }
  }

  protected readonly PRIORIES = PRIORIES;
  protected readonly SEVERITIES = SEVERITIES;

  confirmDelete(rowData: any, event: MouseEvent) {
    this.confirmationService.confirm({
      target: event.target as EventTarget,
      message: 'bạn có muốn xóa công việc này?',
      header: 'Xác nhận xóa',
      icon: 'pi pi-info-circle',
      acceptButtonStyleClass: "p-button-danger p-button-text",
      rejectButtonStyleClass: "p-button-text p-button-text",
      acceptIcon: "none",
      rejectIcon: "none",

      accept: () => {
        this.onDeleteTask(rowData.id)
      },
      reject: () => {
        console.log("is reject")
      }
    });
  }

  onDeleteTask(id: any) {
    this.taskService.deleteTask(id).subscribe(
      {
        next: (res: any) => {
          this.createSuccessToast('Thành công', 'Xóa công việc thành công');
          this.ngOnInit();
        }, error: (err: any) => {
          this.createErrorToast('Lỗi', err.message);
        }
      }
    )
  }

  async onEdit(rowData: any) {
    const taskRes = await this.taskService.getTaskById(rowData.id).toPromise();
    const task = taskRes.data;
    this.dynamicDialogRef = this.dialogService.open(TaskCreateComponent, {
      header: 'Chỉnh sửa công việc',
      width: '60vw',
      contentStyle: { overflow: 'auto', 'margin-bottom': '69px' },
      breakpoints: {
        '960px': '75vw',
        '640px': '90vw'
      },
      data: {
        task: task
      }
    });
  }
}
