import {Component, Injector, OnInit} from '@angular/core';
import {BaseComponent} from "../../share/ui/base-component/base.component";
import {TreeNodeSelectEvent, TreeNodeUnSelectEvent} from "primeng/tree";
import {CategoryService} from '../../service/category.service';
import {PRIORIES, QUERY_OPERATOR, QUERY_OPERATOR_DATE, SEVERITIES} from "../../share/constants/data.constants";
import {TypeService} from "../../service/type.service";
import {StatusIssueService} from "../../service/status-issue.service";
import {ProjectService} from "../../service/project.service";
import {FilterService} from "../../service/filter.service";
import {Filter} from "../../models/filter.model";
import {ConfirmationService} from "primeng/api";

@Component({
  selector: 'app-tasks',
  templateUrl: './tasks.component.html',
  styleUrls: ['./tasks.component.scss'],
  providers: [ConfirmationService]
})
export class TasksComponent extends BaseComponent implements OnInit {
  isViewList: boolean = false;
  isViewTree: boolean = false;
  routerLink: string = 'list-view'

  showAdvancedFilter: boolean = false;

  editFilterMode: boolean = false;
  editFilterValue: any = '';
  editFilterId: any = '';

  addFilterMode: boolean = false;
  addFilterNameValue: any = '';

  // value dropdown
  listProject: any[] = [];
  issueTypes: any[] = [];
  categories: any[] = [];
  priories: any[] = [];
  severities: any[] = [];
  statuses: any[] = [];
  assignees: any[] = [];
  reviewers: any[] = [];

  filters: any[] = [];

  // value filter
  projectId: any;
  projectValue: any;

  typeIdIsEqual: boolean = true;
  typeId: any;

  priorityIsEqual: boolean = true;
  priority: any;

  severityIsEqual: boolean = true;
  severity: any;

  assignUserIdIsEqual: boolean = true;
  assignUserId: any;

  reviewUserIdIsEqual: boolean = true;
  reviewUserId: any;

  statusIssueIsEqual: boolean = true;
  statusIssueId: any;

  categoryIdIsEqual: boolean = true;
  categoryId: any;

  keyword: any;

  startDateOperator: any = 'lonBang';
  startDate: any;

  endDateOperator: any = 'nhoBang';
  endDate: any;

  constructor(injector: Injector,
              private categoryService: CategoryService,
              private typeService: TypeService,
              private statusIssueService: StatusIssueService,
              private projectService: ProjectService,
              private filterService: FilterService,
              private confirmationService: ConfirmationService) {
    super(injector);
    console.log("route: ", this.route.queryParamMap)
    // this.route.queryParamMap.subscribe((params: ParamMap) => {
    //   this.getFilterByUrl(params);
    // })
  }

  async ngOnInit() {
    this.projectId = this.projectStoreService.id;
    await this.getProjects()
    if (this.projectStoreService.id != '') {
      this.getProjectSelected();
      this.getFilters();
    }
    this.severities = SEVERITIES;
    this.priories = PRIORIES;
    this.getValuesOfProject();
  }

  changeStage(value: boolean) {
    this.isViewTree = false;
    const param = {
      projectId: this.projectId,
      typeIdIsEqual: this.typeIdIsEqual,
      typeId: this.typeId,
      priorityIsEqual: this.priorityIsEqual,
      priority: this.priority,
      severityIsEqual: this.severityIsEqual,
      severity: this.severity,
      assignUserIdIsEqual: this.assignUserIdIsEqual,
      assignUserId: this.assignUserId,
      reviewUserIdIsEqual: this.reviewUserIdIsEqual,
      reviewUserId: this.reviewUserId,
      statusIssueIsEqual: this.statusIssueIsEqual,
      statusIssueId: this.statusIssueId,
      categoryIdIsEqual: this.categoryIdIsEqual,
      categoryId: this.categoryId,
      keyword: this.keyword
    }
    if (value) {
      this.router.navigate(['./list-view'], {relativeTo: this.route, queryParams: param})
    } else {
      this.router.navigate(['./detail-view'], {relativeTo: this.route, queryParams: param})
    }
  }

  changeStageViewList(value: any) {
    if (value) {
      this.router.navigate(['./list-view-tree'], { relativeTo: this.route })
    } else {
      this.router.navigate(['./list-view'], { relativeTo: this.route })
    }
  }

  // getFilterByUrl(params: any) {
  //   let data = params.params;
  //
  //   // this.showAdvancedFilter = !(Object.keys(data).length === 0 && data.constructor === Object);
  //
  //   this.typeIdIsEqual = data?.typeIdIsEqual == "true" || true;
  //   this.typeId = data?.typeId?.join(',') || null;
  //   this.priorityIsEqual = data?.priorityIsEqual == "true" || true;
  //   this.priority = data?.priority?.join(',') || null;
  //   this.severityIsEqual = data?.severityIsEqual == "true" || true;
  //   this.severity = data?.severity?.join(',') || null;
  //   this.assignUserIdIsEqual = data?.assignUserIdIsEqual == "true" || true;
  //   this.assignUserId = data?.assignUserId?.join(',') || null;
  //   this.reviewUserIdIsEqual = data?.reviewUserIdIsEqual == "true" || true;
  //   this.reviewUserId = data?.reviewUserId?.join(',') || null;
  //   this.statusIssueIsEqual = data?.statusIssueIsEqual == "true" || true;
  //   this.statusIssueId = data?.statusIssueId?.join(',') || null;
  //   this.categoryIdIsEqual = data?.categoryIdIsEqual == "true" || true;
  //   this.categoryId = data?.categoryId?.join(',') || null;
  //   this.keyword = data?.keyword || null;
  //   this.startDateOperator = 'lonBang';
  //   this.startDate = data?.startDate == null ? null : new Date(data?.startDate);
  //   this.endDateOperator = 'lonBang';
  //   this.endDate = data?.endDate == null ? null : new Date(data?.endDate);
  // }

  getProjectSelected() {
    const project = this.findProject(this.listProject, this.projectId);
    this.projectValue = {...project}
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
    // console.log(this.severity)
    // add query params
    this.router.navigate([], {
      queryParams: {
        projectId: this.projectId,
        typeIdIsEqual: this.typeIdIsEqual,
        typeId: this.typeId,
        priorityIsEqual: this.priorityIsEqual,
        priority: this.priority,
        severityIsEqual: this.severityIsEqual,
        severity: this.severity,
        assignUserIdIsEqual: this.assignUserIdIsEqual,
        assignUserId: this.assignUserId,
        reviewUserIdIsEqual: this.reviewUserIdIsEqual,
        reviewUserId: this.reviewUserId,
        statusIssueIsEqual: this.statusIssueIsEqual,
        statusIssueId: this.statusIssueId,
        categoryIdIsEqual: this.categoryIdIsEqual,
        categoryId: this.categoryId,
        keyword: this.keyword,
        startDateOperator: this.startDateOperator,
        startDate: this.startDate,
        endDateOperator: this.endDateOperator,
        endDate: this.endDate
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
    this.getFilters();
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
    this.addQueryParams()
  }

  resetFilter() {
    console.log(this.projectStoreService.id)
    this.projectId = this.projectStoreService.id || null;
    if (this.projectId) {
      const project = this.findProject(this.listProject, this.projectId);
      this.projectValue = {...project}
    } else {
      this.projectValue = null;
    }
    this.typeIdIsEqual = true;
    this.typeId = null;
    this.priorityIsEqual = true;
    this.priority = null;
    this.severityIsEqual = true;
    this.severity = null;
    this.assignUserIdIsEqual = true;
    this.assignUserId = null;
    this.reviewUserIdIsEqual = true;
    this.reviewUserId = null;
    this.statusIssueIsEqual = true;
    this.statusIssueId = null;
    this.categoryIdIsEqual = true;
    this.categoryId = null;
    this.keyword = null;
    this.startDateOperator = 'lonBang';
    this.startDate = null;
    this.endDateOperator = 'lonBang';
    this.endDate = null;
    this.addQueryParams();
  }

  getFilters() {
    this.filterService
      .getFilters(this.projectId)
      .subscribe({
        next: (res: any) => {
          console.log("filters: ", res.data)
          this.filters = res.data || [];
        }
      })
  }

  saveFilter() {

  }

  onEditFilterMode(filterName: any, id: any) {
    this.editFilterValue = filterName;
    this.editFilterId = id;
    this.addFilterMode = false;
    this.editFilterMode = true;
  }

  onUpdateFilter(id: any) {
    this.filterService.updateFilterName(id, this.editFilterValue)
      .subscribe({
        next: (res: any) => {
          this.getFilters();
        }
      })
    this.editFilterMode = false;
  }

  onAddFilter() {
    const data: Filter = {
      assignUserId: this.assignUserId?.join(','),
      assignUserIdIsEqual: this.assignUserIdIsEqual,
      categoryId: this.categoryId?.join(','),
      categoryIdIsEqual: this.categoryIdIsEqual,
      endDate: this.endDate,
      endDateOperator: this.endDateOperator,
      name: this.addFilterNameValue,
      priority: this.priority?.join(','),
      priorityIsEqual: this.priorityIsEqual,
      projectId: this.projectId,
      reviewUserId: this.reviewUserId?.join(','),
      reviewUserIdIsEqual: this.reviewUserIdIsEqual,
      severity: this.severity?.join(','),
      severityIsEqual: this.severityIsEqual,
      startDate: this.startDate,
      startDateOperator: this.startDateOperator,
      statusIssueId: this.statusIssueId?.join(','),
      statusIssueIdIsEqual: this.statusIssueIsEqual,
      subject: this.keyword,
      typeId: this.typeId?.join(','),
      typeIdIsEqual: this.typeIdIsEqual
    }
    this.filterService.addFilter(data)
      .subscribe({
        next: (res: any) => {
          this.createSuccessToast('Thành công', 'Thêm bộ lọc thành công');
          this.addFilterMode = false;
          this.addFilterNameValue = '';
          this.getFilters();
        }, error: (err: any) => {
          this.createErrorToast('Lỗi', err.message);
        }
      })
  }

  onCancelAddFilter() {
    this.addFilterMode = false
    this.addFilterNameValue = '';
  }

  choseFilterName() {
    this.editFilterMode = false;
    this.addFilterMode = true;
  }

  onQuickFilter(filter: any) {
    this.showAdvancedFilter = true;

    this.keyword = filter?.keyword;
    this.assignUserId = filter?.assignUserId?.split(',');
    this.assignUserIdIsEqual = filter?.assignUserIdIsEqual == 1;
    this.categoryId = filter?.categoryId?.split(',');
    this.categoryIdIsEqual = filter?.categoryIdIsEqual == 1;
    this.endDate = filter?.endDate == null ? null : new Date(filter?.endDate);
    this.endDateOperator = filter?.endDateOperator;
    this.addFilterNameValue = filter?.name;
    this.priority = filter?.priority?.split(',')?.map((item: any) => parseInt(item));
    this.priorityIsEqual = filter?.priorityIsEqual == 1;
    this.projectId = filter?.projectId;
    this.reviewUserId = filter?.reviewUserId?.split(',');
    this.reviewUserIdIsEqual = filter?.reviewUserIdIsEqual == 1;
    this.severity = filter?.severity?.split(',')?.map((item: any) => parseInt(item));
    this.severityIsEqual = filter?.severityIsEqual == 1;
    this.startDate = filter?.startDate == null ? null : new Date(filter?.startDate);
    this.startDateOperator = filter?.startDateOperator;
    this.statusIssueId = filter?.statusIssueId?.split(',');
    this.statusIssueIsEqual = filter?.statusIssueIdIsEqual == 1;
    this.keyword = filter?.subject;
    this.typeId = filter?.typeId?.split(',');
    this.typeIdIsEqual = filter?.typeIdIsEqual == 1;

    this.addQueryParams();
  }

  onDeleteFilter(id: any) {
    this.filterService.deleteFilter(id)
      .subscribe({
        next: (res: any) => {
          this.createSuccessToast('Thành công', 'Xóa bộ lọc thành công');
          this.getFilters();
        }, error: (err: any) => {
          this.createErrorToast('Lỗi', err.message);
        }
      })
  }

  confirmDeleteFilter(id: any, event: Event) {
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
        this.onDeleteFilter(id)
      },
      reject: () => {
        console.log("is reject")
      }
    });
  }

  protected readonly QUERY_OPERATOR = QUERY_OPERATOR;
  protected readonly QUERY_OPERATOR_DATE = QUERY_OPERATOR_DATE;
}
