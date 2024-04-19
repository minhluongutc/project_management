import {Component, Injector, OnInit} from '@angular/core';
import {BaseComponent} from "../../../share/ui/base-component/base.component";
import {TaskService} from "../../../service/task.service";
import {Column} from "../../../models/column.model";
import {last} from "rxjs";
import {PRIORIES, SEVERITIES} from "../../../share/constants/data.constants";
import {ParamMap} from "@angular/router";

@Component({
  selector: 'app-list-view',
  templateUrl: './list-view.component.html',
  styleUrl: './list-view.component.scss'
})
export class ListViewComponent extends BaseComponent implements OnInit {
  cols!: Column[];

  constructor(injector: Injector,
              private taskService: TaskService
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
}
