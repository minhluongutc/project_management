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
import {ConfirmationService} from "primeng/api";
import {ProjectStoreService} from "../../projects/project-store.service";

@Component({
  selector: 'app-task-create',
  templateUrl: './task-create.component.html',
  styleUrl: './task-create.component.scss',
  providers: [ConfirmationService]
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

  visibleImportFile: boolean = false;

  projectIdSelected: any;
  constructor(injector: Injector,
              private projectService: ProjectService,
              private taskService: TaskService,
              private typeService: TypeService,
              private projectUserService: ProjectUserService,
              private statusIssueService: StatusIssueService,
              private categoryService: CategoryService,
              private documentService: DocumentService,
              private dynamicDialogConfig: DynamicDialogConfig,
              private confirmationService: ConfirmationService,
              private projectStoreService: ProjectStoreService,
              private fileService: FileService) {
    super(injector);
    if (projectStoreService.id) {
      this.projectIdSelected = projectStoreService.id;
    }
    this.buildForm();
    this.severities = SEVERITIES;
    this.priories = PRIORIES;
    console.log(this.dynamicDialogConfig)
  }

  async ngOnInit() {
    try {
      await this.getProjects();
      console.log(this.projectIdSelected);

      if (this.dynamicDialogConfig.data != undefined) {
        this.projectIdSelected = this.dynamicDialogConfig.data.task.projectId;
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
          time: null,
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
        if (this.data2edit.startDate || this.data2edit.dueDate) {
          this.form.patchValue({
            time: [new Date(this.data2edit.startDate), new Date(this.data2edit.dueDate)]
          })
        } else {
          this.form.patchValue({
            time: null
          })
        }
        this.getAttachments2Edit(this.data2edit.id);
        console.log("this form", this.form.value)
      }

      if (this.projectStoreService.id != '' && this.data2edit == undefined) {
        let projectValue!: any;
        const projectSelected = this.findProject(this.listProject, this.projectIdSelected);
        console.log(this.listProject)
        console.log("projectSelected", projectSelected)
        projectValue = {...projectSelected}
        this.setEnableFields(true);
        this.getValuesOfProject();
        this.form.patchValue({
          projectId: projectValue,
          subject: null,
          description: null,
          time: null,
          parentId: null,
          typeId: null,
          estimateTime: null,
          priority: null,
          severity: null,
          assignUserId: null,
          reviewUserId: null,
          statusIssueId: null,
          categoryId: null,
          reporter: this.user.username,
          isPublic: false
        })
      }
    } catch (err: any) {
      this.createErrorToast('Lỗi', err.message);
    }
    if (this.projectIdSelected) {
      this.setEnableFields(true);
    } else {
      this.setEnableFields(false);
    }
  }

  buildForm() {
    this.form = this.fb.group({
      projectId: [null, Validators.required],
      subject: [null, Validators.required],
      description: [null],
      time: [null],
      parentId: [null],
      typeId: [null, Validators.required],
      estimateTime: [null],
      priority: [null],
      severity: [null],
      assignUserId: [null],
      reviewUserId: [null],
      statusIssueId: [null],
      categoryId: [null],
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
          console.log('users', res.data)
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
            console.log("categories", res.data)
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
      // if (this.dynamicDialogConfig.data == undefined) {
      //   await this.buildForm()
      // }
    } catch (err: any) {
      this.createErrorToast('Lỗi', err.message);
    }
  }

  async getTasks(projectId: string | undefined, taskId: string | undefined) {
    try {
      const data = {
        projectId: projectId,
        otherTaskId: taskId == undefined ? null : taskId
      }
      const res: any = await this.taskService.getTaskAccordingLevel(data).toPromise();
      const listTask = res.data;
      console.log(listTask.map((item: any) => {
        return {
          label: item.data.subject,
          key: item.data.id,
          children: this.setChildTask(item)
        }
      }))
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

    this.setEnableFields(true);

    this.form.get('typeId')?.enable();
    this.form.get('assignUserId')?.enable();
    this.form.get('reviewUserId')?.enable();
    this.form.get('statusIssueId')?.enable();
    this.form.get('categoryId')?.enable();
    this.form.get('parentId')?.enable();

    this.form.get('projectId')?.setValue($event.node.key)
    this.projectIdSelected = $event.node.key;
    console.log("projectIdSelected", this.projectIdSelected)
    this.getTypes(this.projectIdSelected);
    this.getUserByProject(this.projectIdSelected);
    this.getStatusIssueByProject(this.projectIdSelected);
    this.getCategories(this.projectIdSelected);
    this.getTasks(this.projectIdSelected, this.data2edit?.id || null);
  }

  getValuesOfProject() {
    this.getTypes(this.projectIdSelected);
    this.getUserByProject(this.projectIdSelected);
    this.getStatusIssueByProject(this.projectIdSelected);
    this.getCategories(this.projectIdSelected);
    this.getTasks(this.projectIdSelected, this.data2edit?.id || null);
  }

  onUnselectProject($event: TreeNodeUnSelectEvent) {
    console.log($event)
    this.projectIdSelected = undefined;
    this.setEnableFields(false);
  }

  onSelectFile($event: FileSelectEvent) {
    console.log($event)
    this.fileList = $event.currentFiles;
    console.log(this.fileList)
  }

  onRemoveFile($event: FileRemoveEvent) {
    console.log($event)
    const fileRemoveIndex = this.fileList.findIndex((file: any) => file === $event);
    if (this.data2edit) {
      this.documentService.deleteAttachment(this.fileList[fileRemoveIndex].id).subscribe({
        next: (res: any) => {
          this.createSuccessToast('Thành công', 'Xóa tài liệu thành công');
          this.getAttachments2Edit(this.data2edit.id);
        }, error: (err: any) => {
          this.createErrorToast('Lỗi', err.message);
        }
      })
    } else {
      this.fileList.splice(fileRemoveIndex, 1);
    }
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
    console.log("form value insert", this.form.value)
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
        this.form.reset();
        this.fileList = [];
        const currentRoute = this.router.url.split('?')[0];
        const param = this.router.url.split('?')[1];
        console.log(currentRoute)
        this.router.navigateByUrl('/', {skipLocationChange: true}).then(() => {
          this.router.navigate([`${currentRoute}`], {queryParams: {param}});
        });
        this.closeDialog();
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
        this.form.reset();
        this.fileList = [];
        this.closeDialog();
        const currentRoute = this.router.url.split('?')[0];
        const param = this.router.url.split('?')[1];
        console.log(currentRoute)
        this.router.navigateByUrl('/', {skipLocationChange: true}).then(() => {
          this.router.navigate([`${currentRoute}`], {queryParams: {taskCode: this.data2edit.taskCode}});
        });
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
    return this.fileService.getFileUrl(id);
  }

  confirmDeleteFile(file: any, event: Event) {
    console.log(event)
    this.confirmationService.confirm({
      target: event.target as EventTarget,
      message: 'bạn có muốn xóa tệp đính kèm này?',
      header: 'Xác nhận xóa',
      icon: 'pi pi-info-circle',
      acceptButtonStyleClass: "p-button-danger p-button-text",
      rejectButtonStyleClass: "p-button-text p-button-text",
      acceptIcon: "none",
      rejectIcon: "none",

      accept: () => {
        this.onRemoveFile(file)
      },
      reject: () => {
        console.log("is reject")
      }
    });
  }

  setEnableFields(enable: boolean) {
    if (enable) {
      this.form.get('typeId')?.enable();
      this.form.get('assignUserId')?.enable();
      this.form.get('reviewUserId')?.enable();
      this.form.get('statusIssueId')?.enable();
      this.form.get('categoryId')?.enable();
      this.form.get('parentId')?.enable();
    } else {
      this.form.get('typeId')?.disable();
      this.form.get('assignUserId')?.disable();
      this.form.get('reviewUserId')?.disable();
      this.form.get('statusIssueId')?.disable();
      this.form.get('categoryId')?.disable();
      this.form.get('parentId')?.disable();
    }
  }

  showDialogImportFile() {
    this.visibleImportFile = true;
  }
}
