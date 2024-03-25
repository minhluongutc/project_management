import { Component } from '@angular/core';
import {ToolbarModule} from "primeng/toolbar";
import {HeaderComponent} from "./header/header.component";
import {FooterComponent} from "./footer/footer.component";
import {RouterOutlet} from "@angular/router";
import {AvatarModule} from "primeng/avatar";
import {SharedModule} from "primeng/api";

@Component({
  selector: 'app-layout',
  standalone: true,
  imports: [
    ToolbarModule,
    HeaderComponent,
    FooterComponent,
    RouterOutlet,
    AvatarModule,
    SharedModule
  ],
  templateUrl: './layout.component.html',
  styleUrl: './layout.component.scss'
})
export class LayoutComponent {

}
