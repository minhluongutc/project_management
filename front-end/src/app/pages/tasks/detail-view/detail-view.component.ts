import {Component, Injector, OnInit} from '@angular/core';
import {BaseComponent} from "../../../share/ui/base-component/base.component";
import {Column} from "../../../models/column.model";
import {TaskService} from "../../../service/task.service";
import {ParamMap} from "@angular/router";
import {Attachment} from "../../../models/attachment.model";
import {AccordionModule} from "primeng/accordion";
import {PRIORIES, SEVERITIES} from "../../../share/constants/data.constants";
import {DynamicDialogRef} from "primeng/dynamicdialog";
import {TaskCreateComponent} from "../task-create/task-create.component";
import {ProjectStoreService} from "../../projects/project-store.service";
import {CommentService} from "../../../service/comment.service";
import {UpdateHistoryTaskService} from "../../../service/update-history-task.service";

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
  contentComment: any;
  comments: any[] = [];
  updateHistory: any[] = [];

  tags: string[] = [];

  dynamicDialogRef: DynamicDialogRef | undefined;
  constructor(injector: Injector,
              private taskService: TaskService,
              private commentService: CommentService,
              private updateHistoryService: UpdateHistoryTaskService,
              private projectStoreService: ProjectStoreService
  ) {
    super(injector);
  }

  ngOnInit(): void {
    this.route.queryParamMap.subscribe((params: ParamMap) => {
      this.getTasks(params);
    })
  }

  getTasks(queryParams: any) {
    let data = queryParams.params;
    const projectId = this.route.snapshot?.parent?.parent?.paramMap.get('id') || data.projectId;
    console.log(projectId)
    data = {
      ...data,
      priority: data?.priority?.join(',') || null,
      severity: data?.severity?.join(',') || null,
    }
    if (projectId != null) {
      data = {
        ...data,
        statusIssueId: data?.statusIssueId?.join(',') || null,
        typeId: data?.typeId?.join(',') || null,
        categoryId: data?.categoryId?.join(',') || null,
        reviewUserId: data?.reviewUserId?.join(',') || null,
        assignUserId: data?.assignUserId?.join(',') || null,
        projectId
      }
    }
    console.log("data", data)
    this.taskService.getTasks(data).subscribe({
      next: (res: any) => {
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
    const currentRoute = this.router.url.split('?')[0]
    const currentParams = this.route.snapshot.queryParams
    this.router.navigate([currentRoute], {queryParams: {...currentParams, taskCode: item.taskCode}});

    // get current query params

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

    this.getComments(item.id);

    this.getTaskChildren(item.id);

    this.getUpdateHistoryTask(item.id);
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

  showAddChildTask() {
    this.dynamicDialogRef = this.dialogService.open(TaskCreateComponent, {
      header: 'Thêm mới công việc',
      width: '60vw',
      contentStyle: { overflow: 'auto', 'margin-bottom': '69px' },
      breakpoints: {
        '960px': '75vw',
        '640px': '90vw'
      },
      data: {
        task: {
          parentId: this.taskSelected.id,
          projectId: this.taskSelected.projectId,
          parentSubject: this.taskSelected.subject
        }
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

  filterTaskSelectedById(id: string) {
    this.taskSelected = this.listData.find((task: any) => task.id === id);
    this.getTaskChildren(id);
  }

  openNewTab(id: any) {
    window.open(`/tasks/${id}/attachments/${this.attachments[0].id}?type=1`, '_blank');
  }

  getComments(objectId: any) {
    this.commentService.getComment(objectId).subscribe({
      next: (res: any) => {
        console.log(res)
        this.comments = res.data || [];
      }, error: (err: any) => {
        this.createErrorToast('Lỗi', err.message);
      }
    })
  }

  onComment() {
    this.commentService.createComment(this.taskSelected.id, this.contentComment).subscribe({
      next: (res: any) => {
        this.createSuccessToast('Thành công', 'Thêm mới một bình luận thành công');
        this.contentComment = '';
        this.getComments(this.taskSelected.id);
      }, error: (err: any) => {
        this.createErrorToast('Lỗi', err.message);
      }
    })
  }

  getUpdateHistoryTask(taskId: string) {
    this.updateHistoryService.getUpdateHistoryTask(taskId).subscribe({
      next: (res: any) => {
        console.log(res)
        this.updateHistory = res.data;
      }, error: (err: any) => {
        this.createErrorToast('Lỗi', err.message);
      }
    })
  }

  protected readonly PRIORIES = PRIORIES;
  protected readonly SEVERITIES = SEVERITIES;
}
