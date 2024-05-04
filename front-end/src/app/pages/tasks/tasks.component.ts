import {Component, Injector, OnInit} from '@angular/core';
import {BaseComponent} from "../../share/ui/base-component/base.component";
import {TreeNodeSelectEvent, TreeNodeUnSelectEvent} from "primeng/tree";
import {ProjectStoreService} from "../projects/project-store.service";
import {CategoryService} from '../../service/category.service';
import {PRIORIES, SEVERITIES} from "../../share/constants/data.constants";
import {ProjectUserService} from "../../service/project-user.service";
import {TypeService} from "../../service/type.service";
import {StatusIssueService} from "../../service/status-issue.service";
import {ProjectService} from "../../service/project.service";

@Component({
  selector: 'app-tasks',
  templateUrl: './tasks.component.html',
  styleUrls: ['./tasks.component.scss']
})
export class TasksComponent extends BaseComponent implements OnInit {
  isViewList: boolean = false;
  isViewTree: boolean = false;
  routerLink: string = 'list-view'

  showAdvancedFilter: boolean = false;

  // value dropdown
  listProject: any[] = [];
  issueTypes: any[] = [];
  categories: any[] = [];
  priories: any[] = [];
  severities: any[] = [];
  statuses: any[] = [];
  assignees: any[] = [];
  reviewers: any[] = [];

  // value filter
  projectId: any;
  projectValue: any;
  subject: any;
  typeId: any;
  priority: any;
  severity: any;
  assignUserId: any;
  reviewUserId: any;
  statusIssueId: any;
  categoryId: any;
  reporter: any;
  keyword: any;

  constructor(injector: Injector,
              protected projectStoreService: ProjectStoreService,
              private categoryService: CategoryService,
              private projectUserService: ProjectUserService,
              private typeService: TypeService,
              private statusIssueService: StatusIssueService,
              private projectService: ProjectService) {
    console.log('init')
    super(injector);
    // this.changeStage(this.isViewList);
    // this.changeStageViewList(this.isViewTree)
  }

  async ngOnInit() {
    this.projectId = this.projectStoreService.id;
    await this.getProjects()
    if (this.projectStoreService.id != '') {
      this.getProjectSelected();
    }
    this.severities = SEVERITIES;
    this.priories = PRIORIES;
    this.getValuesOfProject();
  }

  changeStage(value: boolean) {
    this.isViewTree = false;
    if (value) {
      this.router.navigate(['./list-view'], { relativeTo: this.route })
    } else {
      this.router.navigate(['./detail-view'], { relativeTo: this.route })
    }
  }

  changeStageViewList(value: any) {
    if (value) {
      this.router.navigate(['./list-view-tree'], { relativeTo: this.route })
    } else {
      this.router.navigate(['./list-view'], { relativeTo: this.route })
    }
  }

  getProjectSelected() {
    const project = this.findProject(this.listProject, this.projectId);
    console.log(this.projectId)
    this.projectValue = {...project}
    console.log(this.projectValue)
  }

  getValuesOfProject() {
    this.getTypes(this.projectId);
    this.getUserByProject(this.projectId);
    this.getStatusIssueByProject(this.projectId);
    this.getCategories(this.projectId);
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


  addQueryParams() {
    // add query params
    this.router.navigate([], {
      queryParams: {
        projectId: this.projectId,
        subject: this.subject,
        typeId: this.typeId,
        priority: this.priority,
        severity: this.severity,
        assignUserId: this.assignUserId,
        reviewUserId: this.reviewUserId,
        statusIssueId: this.statusIssueId,
        categoryId: this.categoryId,
        reporter: this.reporter,
        keyword: this.keyword
      }
    });
  }

  onSelectProject($event: TreeNodeSelectEvent) {
    this.projectId = $event.node.key;
    this.projectValue = $event.node;
    this.typeId = null;
    this.assignUserId = null;
    this.reviewUserId = null;
    this.statusIssueId = null;
    this.categoryId = null;
    this.reporter = null;
    this.getValuesOfProject();
    this.addQueryParams()
  }

  onUnselectProject($event: TreeNodeUnSelectEvent) {
    this.projectId = null
    this.typeId = null;
    this.assignUserId = null;
    this.reviewUserId = null;
    this.statusIssueId = null;
    this.categoryId = null;
    this.reporter = null;
    this.addQueryParams()
  }
}
