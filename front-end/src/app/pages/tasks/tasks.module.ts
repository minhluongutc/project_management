import {NgModule} from "@angular/core";
import {IconFieldModule} from "primeng/iconfield";
import {InputIconModule} from "primeng/inputicon";
import {InputTextModule} from "primeng/inputtext";
import {TreeTableModule} from "primeng/treetable";
import {ButtonModule} from "primeng/button";
import {DropdownModule} from "primeng/dropdown";
import {TreeSelectModule} from "primeng/treeselect";
import {DatePipe, NgForOf, NgIf} from "@angular/common";
import {TasksRoutingModule} from "./tasks.routing";
import {TasksComponent} from "./tasks.component";
import { DetailViewComponent } from './detail-view/detail-view.component';
import { ListViewTreeComponent } from './list-view-tree/list-view-tree.component';
import {SelectButtonModule} from "primeng/selectbutton";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import { TaskCreateComponent } from './task-create/task-create.component';
import {JiraInputTextComponent} from "../../share/ui/jira-input-text/jira-input-text.component";
import {EditorModule} from "primeng/editor";
import {FileUploadModule} from "primeng/fileupload";
import {UiModule} from "../../share/ui/ui.module";
import {CheckboxModule} from "primeng/checkbox";
import {CalendarModule} from "primeng/calendar";
import {TagModule} from "primeng/tag";
import {InputSwitchModule} from "primeng/inputswitch";
import { ListViewComponent } from './list-view/list-view.component';
import {TableModule} from "primeng/table";
import {RippleModule} from "primeng/ripple";
import {AccordionModule} from "primeng/accordion";
import {StyleClassModule} from "primeng/styleclass";
import {TabViewModule} from "primeng/tabview";
import {AvatarModule} from "primeng/avatar";
import {ChipsModule} from "primeng/chips";
import {FileSizePipe} from "../../share/pipes/file-size.pipe";
import {ConfirmDialogModule} from "primeng/confirmdialog";

@NgModule({
  declarations: [
    TasksComponent,
    DetailViewComponent,
    ListViewTreeComponent,
    TaskCreateComponent,
    ListViewComponent
  ],
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
        JiraInputTextComponent,
        EditorModule,
        FileUploadModule,
        UiModule,
        CheckboxModule,
        CalendarModule,
        DatePipe,
        TagModule,
        InputSwitchModule,
        TableModule,
        RippleModule,
        AccordionModule,
        StyleClassModule,
        TabViewModule,
        AvatarModule,
        ChipsModule,
        FileSizePipe,
        ConfirmDialogModule,
    ],
})
export class TasksModule {
}
