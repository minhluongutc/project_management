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
import {ProjectStoreService} from "../../projects/project-store.service";

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
  listChildrenTask: any[] = [];

  dynamicDialogRef: DynamicDialogRef | undefined;
  constructor(injector: Injector,
              private taskService: TaskService,
              private fileService: FileService,
              private projectStoreService: ProjectStoreService
  ) {
    super(injector);
  }

  ngOnInit(): void {
    this.route.queryParamMap.subscribe((params: ParamMap) => {
      console.log('params:', params);
      console.log(params.get('taskCode'))
      this.getTasks(params);
    })
  }

  getTasks(queryParams: any) {
    console.log(queryParams)
    let data = queryParams.params;
    const projectId = this.route.snapshot?.parent?.parent?.paramMap.get('id') || null;
    if (projectId != null) {
      data = { ...data, projectId: projectId }
    }
    this.taskService.getTasks(data).subscribe({
      next: (res: any) => {
        console.log('res:', res);
        this.listData = res?.data || [];
        this.totalRecords = this.listData.length || 0;
        if (queryParams.get('taskCode')) {
          this.taskSelected = this.listData.find((task: any) => task.taskCode == queryParams.get('taskCode'));
          console.log(this.taskSelected)
          this.viewDetail(this.taskSelected);
        }
      }, error: (err: any) => {
        this.createErrorToast("Lỗi", err.message);
      }
    })
  }

  viewDetail(item: any) {
    console.log(item)
    const currentRoute = this.router.url.split('?')[0];
    console.log(currentRoute)
    this.router.navigate([currentRoute], { queryParams: { taskCode: item.taskCode } });

    this.taskSelected = item;
    if (item.attachments.length > 0) {
      this.attachments = item.attachments
    } else {
      this.attachments = [];
    }
    this.taskSelected.categoryName = this.taskSelected.categoryName || 'N/A';
    this.taskSelected.createUserName = this.taskSelected.createUserName || 'N/A';
    this.taskSelected.assignUserName = this.taskSelected.assignUserName || 'N/A';
    this.taskSelected.startDate = this.taskSelected.startDate || 'N/A';
    this.taskSelected.dueDate = this.taskSelected.dueDate || 'N/A';
    this.taskSelected.typeName = this.taskSelected.typeName || 'N/A';
    this.taskSelected.reviewUserName = this.taskSelected.reviewUserName || 'N/A';
    this.taskSelected.projectParentSubject = this.taskSelected.projectParentSubject || 'N/A';

    this.getTaskChildren(item.id);
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

  getTaskChildren(parentId: string) {
    this.taskService.getTaskChildren(parentId).subscribe({
      next: (res: any) => {
        console.log(res)
        this.listChildrenTask = res.data;
      }, error: (err: any) => {
        this.createErrorToast('Lỗi', err.message);
      }
    })
  }

  protected readonly PRIORIES = PRIORIES;
  protected readonly SEVERITIES = SEVERITIES;

  filterTaskSelectedById(id: string) {
    this.taskSelected = this.listData.find((task: any) => task.id === id);
    this.getTaskChildren(id);
  }

  openNewTab(id: any) {
    window.open(`/tasks/${id}/attachments/null`, '_blank');
  }
}
