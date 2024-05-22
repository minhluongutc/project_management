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
import {DialogModule} from "primeng/dialog";
import {CalendarModule} from "primeng/calendar";
import {DropdownModule} from "primeng/dropdown";
import {JiraInputTextComponent} from "../share/ui/jira-input-text/jira-input-text.component";
import {RadioButtonModule} from "primeng/radiobutton";
import {DividerModule} from "primeng/divider";
import {ViewAttachmentComponent} from "./attachment/view-attachment/view-attachment.component";
import {NgxDocViewerModule} from "ngx-doc-viewer";
import {PdfViewerModule} from "ng2-pdf-viewer";
import { PostComponent } from './post/post.component';
import {TooltipModule} from "primeng/tooltip";
import {FileSizePipe} from "../share/pipes/file-size.pipe";
import {MeterGroupModule} from "primeng/metergroup";
import {TreeTableModule} from "primeng/treetable";
import {EditorModule} from "primeng/editor";
import {TreeSelectModule} from "primeng/treeselect";
import {FileUploadModule} from "primeng/fileupload";
import { CalendarComponent } from './calendar/calendar.component';
import {FullCalendarModule} from "@fullcalendar/angular";
import {AutofocusDirective} from "../share/directives/auto-focus.directive";


@NgModule({
  declarations: [
    DashboardComponent,
    LoginComponent,
    ProfileComponent,
    ViewAttachmentComponent,
    PostComponent,
    CalendarComponent
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
        RippleModule,
        DialogModule,
        CalendarModule,
        DropdownModule,
        JiraInputTextComponent,
        RadioButtonModule,
        DividerModule,
        NgxDocViewerModule,
        PdfViewerModule,
        TooltipModule,
        FileSizePipe,
        MeterGroupModule,
        TreeTableModule,
        EditorModule,
        TreeSelectModule,
        FileUploadModule,
        FullCalendarModule,
        AutofocusDirective
    ],
  providers: [DecimalPipe]
})
export class PagesModule { }
