import {NgModule} from "@angular/core";
import {SharedModule} from "primeng/api";
import {AvatarModule} from "primeng/avatar";
import {ButtonModule} from "primeng/button";
import {InputTextModule} from "primeng/inputtext";
import {ToolbarModule} from "primeng/toolbar";
import {SplitButtonModule} from "primeng/splitbutton";
import {FooterComponent} from "./layout/footer/footer.component";
import {LayoutComponent} from "./layout/layout.component";
import {HeaderComponent} from "./layout/header/header.component";
import {ListboxModule} from "primeng/listbox";
import {FormsModule} from "@angular/forms";
import {OverlayPanelModule} from "primeng/overlaypanel";
import {DialogService} from "primeng/dynamicdialog";
import {JiraLabelComponent} from './label/jiraLabel.component';
import {DatePipe, NgForOf, NgIf, NgTemplateOutlet} from "@angular/common";
import {DialogModule} from "primeng/dialog";
import {SidebarSecondaryComponent} from './sidebar-secondary/sidebar-secondary.component';
import {LayoutSecondaryComponent} from './layout-secondary/layout-secondary.component';
import {CardModule} from "primeng/card";
import {BadgeModule} from "primeng/badge";
import {TabViewModule} from "primeng/tabview";
import { NotificationCardComponent } from './notification-card/notification-card.component';

@NgModule({
  declarations: [
    FooterComponent,
    HeaderComponent,
    LayoutComponent,
    JiraLabelComponent,
    SidebarSecondaryComponent,
    LayoutSecondaryComponent,
    NotificationCardComponent
  ],
    imports: [
        SharedModule,
        AvatarModule,
        ButtonModule,
        InputTextModule,
        ToolbarModule,
        SplitButtonModule,
        ListboxModule,
        FormsModule,
        OverlayPanelModule,
        NgIf,
        DialogModule,
        NgForOf,
        NgTemplateOutlet,
        CardModule,
        BadgeModule,
        DatePipe,
        TabViewModule,
    ],
  providers: [DialogService],
    exports: [LayoutComponent, JiraLabelComponent, LayoutSecondaryComponent, SidebarSecondaryComponent]
})
export class UiModule {
}
