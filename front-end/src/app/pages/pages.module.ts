import { NgModule } from '@angular/core';
import {CommonModule, DecimalPipe} from '@angular/common';
import { ProjectsComponent } from './projects/projects.component';
import { LoginComponent } from './login/login.component';
import {ReactiveFormsModule} from "@angular/forms";
import {PagesRoutingModule} from "./pages-routing.module";
import {AuthService} from "../share/auth/auth.service";
import {HTTP_INTERCEPTORS, HttpClient, HttpClientModule, HttpHandler} from "@angular/common/http";
import {AuthInterceptorService} from "../share/auth/auth-interceptor.service";
import { DashboardComponent } from './dashboard/dashboard.component';
import {UiModule} from "../share/ui/ui.module";
import {IconFieldModule} from "primeng/iconfield";
import {InputIconModule} from "primeng/inputicon";
import {InputTextModule} from "primeng/inputtext";
import {TreeTableModule} from "primeng/treetable";
import {ProjectService} from "../service/project.service";
import {BaseService} from "../share/services/base.service";
import {ButtonModule} from "primeng/button";
import { ProjectCreateComponent } from './projects/project-create/project-create.component';
import {DropdownModule} from "primeng/dropdown";
import {TreeSelectModule} from "primeng/treeselect";
import { ProjectDetailComponent } from './projects/project-detail/project-detail.component';
import { TasksComponent } from './tasks/tasks.component';
import {ProjectModule} from "./projects/project.module";
import {TasksModule} from "./tasks/tasks.module";
import {AccordionModule} from "primeng/accordion";



@NgModule({
  declarations: [
    LoginComponent,
    DashboardComponent,
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    PagesRoutingModule,
    UiModule,
    ProjectModule,
    TasksModule,
    AccordionModule
  ],
  providers: [DecimalPipe]
})
export class PagesModule { }
