import {Component, Injector, OnInit} from '@angular/core';
import {BaseComponent} from "../../../share/ui/base-component/base.component";
import {Column} from "../../../models/column.model";
import {TaskService} from "../../../service/task.service";
import {ParamMap} from "@angular/router";
import {Attachment} from "../../../models/attachment.model";
import {FileService} from "../../../share/services/file.service";
import {AccordionModule} from "primeng/accordion";
import {PRIORIES, SEVERITIES} from "../../../share/constants/data.constants";
import {DynamicDialogRef} from "primeng/dynamicdialog";
import {TaskCreateComponent} from "../task-create/task-create.component";

@Component({
  selector: 'app-detail-view',
  templateUrl: './detail-view.component.html',
  styleUrl: './detail-view.component.scss',
  providers: [AccordionModule]
})
export class DetailViewComponent extends BaseComponent implements OnInit {
  cols!: Column[];
  taskSelected!: any;
  attachments: Attachment[] = [];

  dynamicDialogRef: DynamicDialogRef | undefined;
  constructor(injector: Injector,
              private taskService: TaskService,
              private fileService: FileService
  ) {
    super(injector);
  }

  ngOnInit(): void {
    this.route.queryParamMap.subscribe((params: ParamMap) => {
      console.log('params:', params);
      this.getTasks(params);
    })
  }

  getTasks(queryParams: any) {
    this.taskService.getTasks(queryParams.params).subscribe({
      next: (res: any) => {
        console.log('res:', res);
        this.listData = res?.data || [];
        this.totalRecords = this.listData.length || 0;

        // this.exportColumns = this.cols.map((col) => ({ title: col.header, dataKey: col.field }));
      }, error: (err: any) => {
        this.createErrorToast("Lỗi", err.message);
      }
    })
  }

  viewDetail(item: any) {
    console.log(item)
    this.taskSelected = item;
    if (item.attachments.length > 0) {
      this.attachments = item.attachments
    } else {
      this.attachments = [];
    }
    this.taskSelected.categoryName == undefined ? this.taskSelected.categoryName = 'N/A' : this.taskSelected.categoryName;
    this.taskSelected.createUserName == undefined ? this.taskSelected.createUserName = 'N/A' : this.taskSelected.createUserName;
    this.taskSelected.assignUserName == undefined ? this.taskSelected.assignUserName = 'N/A' : this.taskSelected.assignUserName;
    this.taskSelected.startDate == undefined ? this.taskSelected.startDate = 'N/A' : this.taskSelected.startDate;
    this.taskSelected.dueDate == undefined ? this.taskSelected.dueDate = 'N/A' : this.taskSelected.dueDate;
    this.taskSelected.typeName == undefined ? this.taskSelected.typeName = 'N/A' : this.taskSelected.typeName;
    this.taskSelected.reviewUserName == undefined ? this.taskSelected.reviewUserName = 'N/A' : this.taskSelected.reviewUserName;
    this.taskSelected.projectParentSubject == undefined ? this.taskSelected.projectParentSubject = 'N/A' : this.taskSelected.projectParentSubject;
  }

  downloadAttachment(attachment: Attachment) {
    this.fileService.downloadFile(attachment.fileName, attachment.filePath)
  }

  showEditTask() {
    this.dynamicDialogRef = this.dialogService.open(TaskCreateComponent, {
      header: 'Chỉnh sửa công việc',
      width: '60vw',
      contentStyle: { overflow: 'auto', 'margin-bottom': '69px' },
      breakpoints: {
        '960px': '75vw',
        '640px': '90vw'
      },
      data: {
        task: this.taskSelected
      }
    });
  }

  protected readonly PRIORIES = PRIORIES;
  protected readonly SEVERITIES = SEVERITIES;
}
