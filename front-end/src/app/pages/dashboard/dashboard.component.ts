import {Component, Injector, OnDestroy, OnInit} from '@angular/core';
import {BaseComponent} from "../../share/ui/base-component/base.component";
import {ProjectService} from "../../service/project.service";
import {Column} from "../../models/column.model";
import Chart from "chart.js/auto";
import {Subject} from "rxjs";
import {TaskService} from "../../service/task.service";
import {TreeNodeSelectEvent, TreeNodeUnSelectEvent} from "primeng/tree";
import {ProjectUserService} from "../../service/project-user.service";
import {DropdownChangeEvent} from "primeng/dropdown";

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})
export class DashboardComponent extends BaseComponent implements OnInit {
  public chart: any;
  cols!: Column[];
  listProject: any[] = [];
  projectIdSelected: any;
  showChart: boolean = false;

  private onDestroy$ = new Subject<boolean>();
  assignees: any[] = [];

  constructor(injector: Injector,
              private projectService: ProjectService,
              private taskService: TaskService) {
    super(injector);
  }

  async ngOnInit() {
    this.cols = [
      {field: 'name', header: 'Tên dự án'},
      // {field: 'description', header: 'Mô tả'},
    ];
    await this.getProjects();
    await this.getProjectsDropdown();
    // this.getTaskStatistic()
  }

  async getProjects() {
    try {
      const res: any = await this.projectService.getProjects(this.user.id, {}).toPromise();
      console.log('res:', res);
      this.listDataTree = res.data;
    } catch (err: any) {
      this.createErrorToast('Lỗi', err.message);
    }
  }

  onDetailProject(id: any) {
    this.router.navigate([`/projects/${id}`]);
  }

  getTaskStatistic(projectId: any, userId: any) {
    this.taskService.getTaskStatistic({projectId, userId})
      .subscribe({
        next: (res: any) => {
          const data = res.data;
          if (data.taskDoneInTime === 0 && data.taskLateNotDone === 0 && data.taskLateDone === 0 && data.taskToDo === 0) {
            this.showChart = false;
          } else {
            this.createChart(data);
          }
        }
      })
  }

  async getProjectsDropdown() {
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

  getUserByProject(projectId: string | undefined) {
    if (projectId == undefined) {
      this.assignees = [];
    }
    this.projectUserService
      .getUserByProject({projectId: projectId})
      .subscribe({
        next: (res: any) => {
          this.assignees = res.data;
        }
      })
  }

  onSelectProject($event: TreeNodeSelectEvent) {
    this.showChart = true;
    this.projectIdSelected = $event.node.key;
    this.getTaskStatistic($event.node.key, null);
    this.getUserByProject($event.node.key);
  }

  onUnselectProject($event: TreeNodeUnSelectEvent) {
    this.showChart = false;
    this.assignees = [];
    this.projectIdSelected = null;
  }

  createChart(data: any) {
    if (this.chart) {
      this.chart.destroy();
    }
    this.chart = new Chart("MyChart", {
      type: 'doughnut', //this denotes tha type of chart
      data: {
        labels: ['Hoàn thành đúng hạn', 'Trễ hạn (chưa hoàn thành)', 'Hoàn thành trễ hạn', 'Đang thực hiện'],
        datasets: [{
          label: 'Số lượng',
          data: [data.taskDoneInTime, data.taskLateNotDone, data.taskLateDone, data.taskToDo],
          backgroundColor: [
            'rgb(99,255,148)',
            'rgb(255, 99, 132)',
            'rgb(255, 205, 86)',
            'rgb(54, 162, 235)'
          ],
          hoverOffset: 4
        }]
      },
      options: {
        aspectRatio: 2.5
      }
    });
  }

  // override ngOnDestroy() {
  //   super.ngOnDestroy();
  //   this.onDestroy$.next(true);
  //   this.onDestroy$.complete();
  //   this.chart.destroy();
  // }

  onChoseUser($event: DropdownChangeEvent) {
    this.showChart = true;
    this.getTaskStatistic(this.projectIdSelected, $event.value);
  }
}
