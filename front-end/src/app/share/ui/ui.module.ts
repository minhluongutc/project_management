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
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";

@NgModule({
  declarations: [
    FooterComponent,
    HeaderComponent,
    LayoutComponent
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

  ],
  providers: [],
  exports: [LayoutComponent]
})
export class UiModule {
}
