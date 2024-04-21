import {ProjectsComponent} from "./projects.component";
import {ProjectCreateComponent} from "./project-create/project-create.component";
import {ProjectDetailComponent} from "./project-detail/project-detail.component";
import {RouterModule, Routes} from "@angular/router";
import {NgModule} from "@angular/core";
import {KanbanBoardComponent} from "./project-detail/kanban-board/kanban-board.component";
import {DetailViewComponent} from "../tasks/detail-view/detail-view.component";
import {TasksComponent} from "../tasks/tasks.component";
import {ListViewTreeComponent} from "../tasks/list-view-tree/list-view-tree.component";
import {ListViewComponent} from "../tasks/list-view/list-view.component";
import {BacklogComponent} from "./project-detail/backlog/backlog.component";
import {PermissionComponent} from "./project-detail/permission/permission.component";
import {StatisticsComponent} from "./project-detail/statistics/statistics.component";
import {SettingComponent} from "./project-detail/setting/setting.component";

const routes: Routes = [
  {
    path: '',
    component: ProjectsComponent,
  },
  {
    path: 'create',
    component: ProjectCreateComponent,
  },
  {
    path: ':id',
    component: ProjectDetailComponent,
    children: [
      {
        path: 'kanban-board',
        component: KanbanBoardComponent
      },
      {
        path: 'backlog',
        component: BacklogComponent
      },
      {
        path: 'permissions',
        component: PermissionComponent
      },
      {
        path: 'statistics',
        component: StatisticsComponent
      },
      {
        path: 'setting',
        component: SettingComponent
      },
      {
        path: 'tasks',
        component: TasksComponent,
        children: [
          {
            path: '',
            redirectTo: 'detail-view',
            pathMatch: 'full'},
          {
            path: 'detail-view',
            component: DetailViewComponent
          },
          {
            path: 'list-view-tree',
            component: ListViewTreeComponent
          },
          {
            path: 'list-view',
            component: ListViewComponent
          }
        ]
      }
    ]
  }
]

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ProjectRoutingModule {
}
