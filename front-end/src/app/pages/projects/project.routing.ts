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
import {UsersComponent} from "./project-detail/users/users.component";
import {StatisticsComponent} from "./project-detail/statistics/statistics.component";
import {CategoryComponent} from "./project-detail/setting/category/category.component";
import {IssueTypeComponent} from "./project-detail/setting/project-type/issue-type.component";
import {StatusIssueComponent} from "./project-detail/setting/status-issue/status-issue.component";

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
        path: '',
        redirectTo: 'kanban-board',
        pathMatch: 'full'
      },
      {
        path: 'kanban-board',
        component: KanbanBoardComponent
      },
      {
        path: 'backlog',
        component: BacklogComponent
      },
      {
        path: 'users',
        component: UsersComponent
      },
      {
        path: 'statistics',
        component: StatisticsComponent
      },
      {
        path: 'category',
        component: CategoryComponent
      },
      {
        path: 'issue-type',
        component: IssueTypeComponent
      },
      {
        path: 'status-issue',
        component: StatusIssueComponent
      },
      {
        path: 'tasks',
        component: TasksComponent,
        children: [
          {
            path: '',
            redirectTo: 'detail-view',
            pathMatch: 'full'
          },
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
