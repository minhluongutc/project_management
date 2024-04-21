import {NgModule} from "@angular/core";
import {ProjectsComponent} from "./projects.component";
import {ProjectCreateComponent} from "./project-create/project-create.component";
import {ProjectDetailComponent} from "./project-detail/project-detail.component";
import {IconFieldModule} from "primeng/iconfield";
import {InputIconModule} from "primeng/inputicon";
import {InputTextModule} from "primeng/inputtext";
import {TreeTableModule} from "primeng/treetable";
import {ButtonModule} from "primeng/button";
import {DropdownModule} from "primeng/dropdown";
import {TreeSelectModule} from "primeng/treeselect";
import {ProjectRoutingModule} from "./project.routing";
import {NgForOf, NgIf} from "@angular/common";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {JiraInputTextComponent} from "../../share/ui/jira-input-text/jira-input-text.component";
import {ToastModule} from "primeng/toast";
import { KanbanBoardComponent } from './project-detail/kanban-board/kanban-board.component';
import { BacklogComponent } from './project-detail/backlog/backlog.component';
import { SettingComponent } from './project-detail/setting/setting.component';
import { PermissionComponent } from './project-detail/permission/permission.component';
import { StatisticsComponent } from './project-detail/statistics/statistics.component';

@NgModule({
  declarations: [
    ProjectsComponent,
    ProjectCreateComponent,
    ProjectDetailComponent,
    KanbanBoardComponent,
    BacklogComponent,
    SettingComponent,
    PermissionComponent,
    StatisticsComponent,
  ],
    imports: [
        IconFieldModule,
        InputIconModule,
        InputTextModule,
        TreeTableModule,
        ButtonModule,
        DropdownModule,
        TreeSelectModule,
        ProjectRoutingModule,
        NgForOf,
        NgIf,
        ReactiveFormsModule,
        JiraInputTextComponent,
        ToastModule,
        FormsModule
    ],
})
export class ProjectModule {
}
