import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {TasksComponent} from "./tasks.component";
import {DetailViewComponent} from "./detail-view/detail-view.component";
import {ListViewTreeComponent} from "./list-view-tree/list-view-tree.component";
import {ListViewComponent} from "./list-view/list-view.component";

const routes: Routes = [
  {
    path: '',
    component: TasksComponent,
    children: [
      {
        path: '',
        component: DetailViewComponent
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
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class TasksRoutingModule {
}
