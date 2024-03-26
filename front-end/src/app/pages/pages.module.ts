import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
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



@NgModule({
  declarations: [
    ProjectsComponent,
    LoginComponent,
    DashboardComponent
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    PagesRoutingModule,
    UiModule,
    IconFieldModule,
    InputIconModule,
    InputTextModule,
    TreeTableModule
  ],
})
export class PagesModule { }
