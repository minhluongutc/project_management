import {NgModule} from "@angular/core";
import {IconFieldModule} from "primeng/iconfield";
import {InputIconModule} from "primeng/inputicon";
import {InputTextModule} from "primeng/inputtext";
import {TreeTableModule} from "primeng/treetable";
import {ButtonModule} from "primeng/button";
import {DropdownModule} from "primeng/dropdown";
import {TreeSelectModule} from "primeng/treeselect";
import {NgForOf, NgIf} from "@angular/common";
import {TasksRoutingModule} from "./tasks.routing";
import {TasksComponent} from "./tasks.component";
import { DetailViewComponent } from './detail-view/detail-view.component';
import { ListViewComponent } from './list-view/list-view.component';
import {SelectButtonModule} from "primeng/selectbutton";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import { TaskCreateComponent } from './task-create/task-create.component';
import {JiraInputTextComponent} from "../../share/ui/jira-input-text/jira-input-text.component";

@NgModule({
  declarations: [TasksComponent, DetailViewComponent, ListViewComponent, TaskCreateComponent],
  imports: [
    IconFieldModule,
    InputIconModule,
    InputTextModule,
    TreeTableModule,
    ButtonModule,
    DropdownModule,
    TreeSelectModule,
    NgForOf,
    NgIf,
    TasksRoutingModule,
    SelectButtonModule,
    FormsModule,
    ReactiveFormsModule,
    JiraInputTextComponent
  ],
})
export class TasksModule {
}
