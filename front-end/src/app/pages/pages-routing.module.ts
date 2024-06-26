import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {LayoutComponent} from "../share/ui/layout/layout.component";
import {DashboardComponent} from "./dashboard/dashboard.component";
import {ProfileComponent} from "./profile/profile.component";
import {PostComponent} from "./post/post.component";
import {CalendarComponent} from "./calendar/calendar.component";
import {TestWSComponent} from "./test-ws/test-ws.component";


const routes: Routes = [
  {
    path: '',
    component: LayoutComponent,
    children: [
      {
        path: '',
        redirectTo: 'dashboard',
        pathMatch: 'full'
      },
      {
        path: 'chat/:userId',
        component: TestWSComponent
      },
      {
        path: 'dashboard',
        component: DashboardComponent,
      },
      {
        path: 'profile',
        component: ProfileComponent,
      },
      {
        path: 'posts',
        component: PostComponent
      },
      {
        path: 'calendar',
        component: CalendarComponent
      },
      {
        path: 'projects',
        loadChildren: () => import('./projects/project.module').then(m => m.ProjectModule)
      },
      {
        path: 'tasks',
        loadChildren: () => import('./tasks/tasks.module').then(m => m.TasksModule)
      },
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class PagesRoutingModule {
}
