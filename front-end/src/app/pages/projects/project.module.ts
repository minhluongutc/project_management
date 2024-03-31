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
import {ReactiveFormsModule} from "@angular/forms";
import {JiraInputTextComponent} from "../../share/ui/jira-input-text/jira-input-text.component";
import {ToastModule} from "primeng/toast";

@NgModule({
  declarations: [
    ProjectsComponent,
    ProjectCreateComponent,
    ProjectDetailComponent,
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
    ToastModule
  ],
})
export class ProjectModule {
}
