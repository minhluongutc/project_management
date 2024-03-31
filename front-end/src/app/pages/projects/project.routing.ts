import {ProjectsComponent} from "./projects.component";
import {ProjectCreateComponent} from "./project-create/project-create.component";
import {ProjectDetailComponent} from "./project-detail/project-detail.component";
import {RouterModule, Routes} from "@angular/router";
import {NgModule} from "@angular/core";

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
  }
]

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ProjectRoutingModule {
}
