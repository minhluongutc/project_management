import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {LayoutComponent} from "../share/ui/layout/layout.component";
import {DashboardComponent} from "./dashboard/dashboard.component";
import {AuthGuardService} from "../share/auth/auth-guard.service";


const routes: Routes = [
  {
    path: '',
    component: LayoutComponent,
    canActivate: [AuthGuardService],
    children: [
      {path: '', redirectTo: 'dashboard', pathMatch: 'full'},
      {path: 'dashboard', component: DashboardComponent},
      {
        path: 'projects',
        loadChildren: () => import('./projects/project.module').then(m => m.ProjectModule)
      },
      {
        path: 'tasks',
        loadChildren: () => import('./tasks/tasks.module').then(m => m.TasksModule)
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class PagesRoutingModule {
}
