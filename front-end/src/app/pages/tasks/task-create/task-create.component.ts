import {Component, Injector, OnInit} from '@angular/core';
import {BaseComponent} from '../../../share/ui/base-component/base.component';
import {Validators} from "@angular/forms";
import {FileUploadEvent} from "primeng/fileupload";
import {ProjectService} from "../../../service/project.service";

@Component({
  selector: 'app-task-create',
  templateUrl: './task-create.component.html',
  styleUrl: './task-create.component.scss'
})
export class TaskCreateComponent extends BaseComponent implements OnInit {
  listProject: any[] = []
  issueTypes: any[] = [
    {name: 'Task', value: 'Task'},
    {name: 'Bug', value: 'Bug'},
    {name: 'Story', value: 'Story'}
  ];
  categories: any[] = [
    {name: 'Category 1', value: 'Category 1'},
    {name: 'Category 2', value: 'Category 2'},
    {name: 'Category 3', value: 'Category 3'}
  ];
  priories: any[] = [
    {name: 'High', value: 'High'},
    {name: 'Medium', value: 'Medium'},
    {name: 'Low', value: 'Low'}
  ];
  severities: any[] = [
    {name: 'High', value: 'High'},
    {name: 'Medium', value: 'Medium'},
    {name: 'Low', value: 'Low'}
  ];
  statuses: any[] = [
    {name: 'Open', value: 'Open'},
    {name: 'In Progress', value: 'In Progress'},
    {name: 'Resolved', value: 'Resolved'},
    {name: 'Closed', value: 'Closed'}
  ];
  assignees: any[] = [
    {name: 'User 1', value: 'User 1'},
    {name: 'User 2', value: 'User 2'},
    {name: 'User 3', value: 'User 3'}
  ];
  reviewers: any[] = [
    {name: 'User 1', value: 'User 1'},
    {name: 'User 2', value: 'User 2'},
    {name: 'User 3', value: 'User 3'}
  ];

  constructor(injector: Injector,
              private projectService: ProjectService) {
    super(injector);
  }

  async ngOnInit() {
    await this.getProjects();
    this.changeStructureListProject();
    this.buildForm();
  }

  buildForm() {
    this.form = this.fb.group({
      subject: ['', Validators.required],
      reporter: ['', Validators.required],
      parentId: [''],
    });
  }

  async getProjects() {
    try {
      const res: any = await this.projectService.getProjects(this.user.id, {}).toPromise();
      console.log('res:', res);
      this.listProject = res.data;
    } catch (err: any) {
      this.createErrorToast('Lỗi', err.message);
    }
  }

  changeStructureListProject() {
    this.listProject = this.listProject.map((item: any) => {
      return {
        label: item.data.name,
        key: item.data.id,
        children: this.setChildProject(item)
      }
    })
  }

  setChildProject(item: any): any {
    console.log(item.children)
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


  onUpload($event: FileUploadEvent) {

  }
}
