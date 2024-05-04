import {NgModule} from '@angular/core';
import {CommonModule, DecimalPipe} from '@angular/common';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {PagesRoutingModule} from "./pages-routing.module";
import {DashboardComponent} from './dashboard/dashboard.component';
import {UiModule} from "../share/ui/ui.module";
import {ProjectModule} from "./projects/project.module";
import {TasksModule} from "./tasks/tasks.module";
import {AccordionModule} from "primeng/accordion";
import {LoginComponent} from './login/login.component';
import { ProfileComponent } from './profile/profile.component';
import {ButtonModule} from "primeng/button";
import {RippleModule} from "primeng/ripple";


@NgModule({
  declarations: [
    DashboardComponent,
    LoginComponent,
    ProfileComponent,
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    PagesRoutingModule,
    UiModule,
    ProjectModule,
    TasksModule,
    AccordionModule,
    FormsModule,
    ButtonModule,
    RippleModule
  ],
  providers: [DecimalPipe]
})
export class PagesModule { }
