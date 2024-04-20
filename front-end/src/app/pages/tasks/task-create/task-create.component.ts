import {Component, Injector, OnInit} from '@angular/core';
import {BaseComponent} from '../../../share/ui/base-component/base.component';
import {Validators} from "@angular/forms";
import {FileRemoveEvent, FileSelectEvent} from "primeng/fileupload";
import {ProjectService} from "../../../service/project.service";
import {TreeNodeSelectEvent, TreeNodeUnSelectEvent} from "primeng/tree";
import {TaskService} from "../../../service/task.service";
import {Task} from "../../../models/task.model";
import {PRIORIES, SEVERITIES} from "../../../share/constants/data.constants";
import {TypeService} from "../../../service/type.service";
import {ProjectUserService} from "../../../service/project-user.service";
import {StatusIssueService} from "../../../service/status-issue.service";
import {CategoryService} from "../../../service/category.service";
import {DynamicDialogConfig} from "primeng/dynamicdialog";
import {DocumentService} from "../../../service/document.service";
import {FileService} from "../../../share/services/file.service";

@Component({
  selector: 'app-task-create',
  templateUrl: './task-create.component.html',
  styleUrl: './task-create.component.scss'
})
export class TaskCreateComponent extends BaseComponent implements OnInit {
  listProject: any[] = [];
  listTask: any[] = [];
  issueTypes: any[] = [];
  categories: any[] = [];
  priories: any[];
  severities: any[];
  statuses: any[] = [];
  assignees: any[] = [];
  reviewers: any[] = [];
  fileList: any[] = [];

  data2edit: any;

  projectIdSelected: string | undefined = undefined;
  constructor(injector: Injector,
              private projectService: ProjectService,
              private taskService: TaskService,
              private typeService: TypeService,
              private projectUserService: ProjectUserService,
              private statusIssueService: StatusIssueService,
              private categoryService: CategoryService,
              private documentService: DocumentService,
              private dynamicDialogConfig: DynamicDialogConfig,
              private fileService: FileService) {
    super(injector);
    this.buildForm();
    this.severities = SEVERITIES;
    this.priories = PRIORIES;
    console.log(this.dynamicDialogConfig)
  }

  async ngOnInit() {
  try {
    await this.getProjects();
    console.log(this.projectIdSelected);
    if (this.projectIdSelected == undefined) {
      this.form.get('typeId')?.disable();
      this.form.get('assignUserId')?.disable();
      this.form.get('reviewUserId')?.disable();
      this.form.get('statusIssueId')?.disable();
      this.form.get('categoryId')?.disable();
      this.form.get('parentId')?.disable();
    }

    if (this.dynamicDialogConfig.data != undefined) {
      const res: any = await this.taskService.getTaskById(this.dynamicDialogConfig.data.task.id).toPromise();
      console.log(res)
      this.data2edit = res.data;
      this.projectIdSelected = this.data2edit.projectId;
      const projectValue = {
        label: this.data2edit.projectName,
        key: this.data2edit.projectId,
      }
      const parentTask = {
        label: this.data2edit.parentSubject,
        key: this.data2edit.parentId,
      }
      console.log("parentTask", parentTask)
      this.getValuesOfProject();
      console.log(projectValue)
      this.form.patchValue({
        projectId: projectValue,
        subject: this.data2edit.subject,
        description: this.data2edit.description,
        time: [new Date(this.data2edit.startDate), new Date(this.data2edit.dueDate)],
        parentId: parentTask,
        typeId: this.data2edit.typeId,
        estimateTime: this.data2edit.estimateTime,
        priority: this.data2edit.priority,
        severity: this.data2edit.severity,
        assignUserId: this.data2edit.assignUserId,
        reviewUserId: this.data2edit.reviewUserId,
        statusIssueId: this.data2edit.statusIssueId,
        categoryId: this.data2edit.categoryId,
        reporter: this.data2edit.createUserName,
        isPublic: this.data2edit.isPublic
      })
      this.getAttachments2Edit(this.data2edit.id);
      console.log("this form", this.form.value)
    }
  } catch (err: any) {
    this.createErrorToast('Lỗi', err.message);
  }
}

  buildForm() {
    this.form = this.fb.group({
      projectId: [null, Validators.required],
      subject: [null, Validators.required],
      description: [null],
      time: [null],
      parentId: [{value: null}],
      typeId: [null, Validators.required],
      estimateTime: [null],
      priority: [null],
      severity: [null],
      assignUserId: [{value: null}],
      reviewUserId: [{value: null}],
      statusIssueId: [{value: null}],
      categoryId: [{value: null}],
      reporter: [this.user.username, Validators.required],
      isPublic: [false],
      continue: [false]
    });
    console.log(this.form.value)
  }

  getTypes(projectId: string | undefined) {
    if (projectId == undefined) this.issueTypes = []
    else {
      this.typeService
        .getTypes({projectId: projectId})
        .subscribe({
          next: (res: any) => {
            // console.log("issueTypes", res.data)
            this.issueTypes = res.data;
          }
        })
    }
  }

  getUserByProject(projectId: string | undefined) {
    if (projectId == undefined) {
      this.assignees = [];
      this.reviewers = [];
    }
    this.projectUserService
      .getUserByProject({projectId: projectId})
      .subscribe({
        next: (res: any) => {
          this.assignees = res.data;
          this.reviewers = res.data;
        }
      })
  }

  getStatusIssueByProject(projectId: string | undefined) {
    if (projectId == undefined) this.statuses = []
    else {
      this.statusIssueService
        .getStatusIssue({projectId: projectId})
        .subscribe({
          next: (res: any) => {
            // console.log("statuses", res.data)
            this.statuses = res.data;
          }
        })
    }
  }

  getCategories(projectId: string | undefined) {
    if (projectId == undefined) this.categories = []
    else {
      this.categoryService
        .getCategories({projectId: projectId})
        .subscribe({
          next: (res: any) => {
            // console.log("categories", res.data)
            this.categories = res.data;
          }
        })
    }
  }

  async getProjects() {
    try {
      const res: any = await this.projectService.getProjects(this.user.id, {}).toPromise();
      this.listProject = res.data;
      this.changeStructureListProject();
    } catch (err: any) {
      this.createErrorToast('Lỗi', err.message);
    }
  }

  async getTasks(projectId: string | undefined) {
    try {
      const res: any = await this.taskService.getTaskAccordingLevel({projectId: projectId}).toPromise();
      const listTask = res.data;
      this.listTask = listTask.map((item: any) => {
        return {
          label: item.data.subject,
          key: item.data.id,
          children: this.setChildTask(item)
        }
      })
    } catch (err: any) {
      this.createErrorToast('Lỗi', err.message);
    }
  }

  setChildTask(item: any): any {
    let children: any[] = []
    if (item.children.length !== 0) {
      item.children.forEach((child: any) => {
        children.push({
          label: child.data.subject,
          key: child.data.id,
          children: this.setChildTask(child)
        })
      })
    }
    return children;
  }

  changeStructureListProject() {
    this.listProject = this.listProject.map((item: any) => {
      return {
        label: item.data.name,
        key: item.data.id,
        children: this.setChildProject(item)
      }
    })
    console.log(this.listProject)
  }

  setChildProject(item: any): any {
    let children: any[] = []
    if (item.children.length !== 0) {
      item.children.forEach((child: any) => {
        children.push({
          label: child.data.name,
          key: child.data.id,
          children: this.setChildProject(child)
        })
      })
    }
    return children;
  }

  onSelectProject($event: TreeNodeSelectEvent) {
    console.log($event)
    this.form.get('projectId')?.setValue($event.node.key)
    this.projectIdSelected = $event.node.key;
    this.getTypes(this.projectIdSelected);
    this.getUserByProject(this.projectIdSelected);
    this.getStatusIssueByProject(this.projectIdSelected);
    this.getCategories(this.projectIdSelected);
    this.getTasks(this.projectIdSelected);
  }

  getValuesOfProject() {
    this.getTypes(this.projectIdSelected);
    this.getUserByProject(this.projectIdSelected);
    this.getStatusIssueByProject(this.projectIdSelected);
    this.getCategories(this.projectIdSelected);
    this.getTasks(this.projectIdSelected);
  }

  onUnselectProject($event: TreeNodeUnSelectEvent) {
    console.log($event)
    this.projectIdSelected = undefined;
  }

  onSelectFile($event: FileSelectEvent) {
    console.log($event)
    this.fileList = $event.currentFiles;
    console.log(this.fileList)
  }

  onRemoveFile($event: FileRemoveEvent) {
    console.log($event)
    console.log($event.file)
    const fileRemoveIndex = this.fileList.findIndex((file: any) => file === $event);
    this.fileList.splice(fileRemoveIndex, 1);
    console.log(this.fileList);
  }

  onClearFiles($event: Event) {
    this.fileList = []
  }

  insertTask() {
    const data: Task = {
      projectId: this.form.value.projectId?.key,
      subject: this.form.value.subject,
      description: this.form.value.description,
      startDate: this.form.value.time === null ? null : this.form.value.time[0],
      dueDate: this.form.value.time === null ? null : this.form.value.time[1],
      parentId: this.form.value.parentId?.key,
      typeId: this.form.value.typeId,
      estimateTime: this.form.value.estimateTime,
      priority: this.form.value.priority,
      severity: this.form.value.severity,
      assignUserId: this.form.value.assignUserId,
      reviewUserId: this.form.value.reviewUserId,
      statusIssueId: this.form.value.statusIssueId,
      categoryId: this.form.value.categoryId,
      reporter: this.user.id,//Not use this field
      isPublic: this.form.value.isPublic
    }
    console.log(data)
    const formData = new FormData();
    this.fileList.forEach((file: any) => {
      formData.append('files', file);
    })
    formData.append(
      'dto',
      new Blob([JSON.stringify(data)], {type: 'application/json'})
    )
    console.log(this.form.value)
    this.taskService.insertTask(formData).subscribe({
      next: (res: any) => {
        this.createSuccessToast('Thành công', 'Tạo mới công việc thành công');
        this.buildForm();
        this.fileList = [];
      }, error: (err: any) => {
        this.createErrorToast('Lỗi', err.message);
      }
    })
  }

  updateTask() {
    const data: Task = {
      projectId: this.form.value.projectId?.key,
      subject: this.form.value.subject,
      description: this.form.value.description,
      startDate: this.form.value.time === null ? null : this.form.value.time[0],
      dueDate: this.form.value.time === null ? null : this.form.value.time[1],
      parentId: this.form.value.parentId?.key,
      typeId: this.form.value.typeId,
      estimateTime: this.form.value.estimateTime,
      priority: this.form.value.priority,
      severity: this.form.value.severity,
      assignUserId: this.form.value.assignUserId,
      reviewUserId: this.form.value.reviewUserId,
      statusIssueId: this.form.value.statusIssueId,
      categoryId: this.form.value.categoryId,
      reporter: this.user.id,//Not use this field
      isPublic: this.form.value.isPublic
    }
    console.log(data)
    console.log(this.form.value)
    this.taskService.updateTask(this.data2edit.id, data).subscribe({
      next: (res: any) => {
        this.createSuccessToast('Thành công', 'Chỉnh sửa công việc thành công');
        this.buildForm();
        this.fileList = [];
      }, error: (err: any) => {
        this.createErrorToast('Lỗi', err.message);
      }
    })
  }

  getAttachments2Edit(objectId: string) {
    this.documentService.getAttachmentsByObjectId(objectId).subscribe({
      next: (res: any) => {
        this.fileList = res.data;
        console.log("fileList:", this.fileList)
      }, error: (err: any) => {
        this.createErrorToast('Lỗi', err.message);
      }
    })
  }

  getImage(id: string) {
    console.log(this.fileService.getImageUrl(id))
    return this.fileService.getImageUrl(id);
  }
}
