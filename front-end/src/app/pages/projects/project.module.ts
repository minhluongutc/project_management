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
import {DatePipe, NgForOf, NgIf} from "@angular/common";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {JiraInputTextComponent} from "../../share/ui/jira-input-text/jira-input-text.component";
import {ToastModule} from "primeng/toast";
import { KanbanBoardComponent } from './project-detail/kanban-board/kanban-board.component';
import { BacklogComponent } from './project-detail/backlog/backlog.component';
import { UsersComponent } from './project-detail/users/users.component';
import { StatisticsComponent } from './project-detail/statistics/statistics.component';
import { CategoryComponent } from './project-detail/setting/category/category.component';
import { IssueTypeComponent } from './project-detail/setting/project-type/issue-type.component';
import {TableModule} from "primeng/table";
import {AutofocusDirective} from "../../share/directives/auto-focus.directive";
import {RippleModule} from "primeng/ripple";
import {ConfirmDialogModule} from "primeng/confirmdialog";
import { StatusIssueComponent } from './project-detail/setting/status-issue/status-issue.component';
import {TagModule} from "primeng/tag";
import {DialogModule} from "primeng/dialog";
import {RadioButtonModule} from "primeng/radiobutton";
import {UiModule} from "../../share/ui/ui.module";
import {CalendarModule} from "primeng/calendar";
import { GeneralInformationComponent } from './project-detail/setting/general-information/general-information.component';
import {InputNumberModule} from "primeng/inputnumber";
import {StyleClassModule} from "primeng/styleclass";
import {MeterGroupModule} from "primeng/metergroup";

@NgModule({
  declarations: [
    ProjectsComponent,
    ProjectCreateComponent,
    ProjectDetailComponent,
    KanbanBoardComponent,
    BacklogComponent,
    UsersComponent,
    StatisticsComponent,
    CategoryComponent,
    IssueTypeComponent,
    StatusIssueComponent,
    GeneralInformationComponent,
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
        FormsModule,
        TableModule,
        DatePipe,
        AutofocusDirective,
        RippleModule,
        ConfirmDialogModule,
        TagModule,
        DialogModule,
        RadioButtonModule,
        UiModule,
        CalendarModule,
        InputNumberModule,
        StyleClassModule,
        MeterGroupModule
    ],
})
export class ProjectModule {
}
