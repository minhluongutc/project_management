import { Component } from '@angular/core';
import {SharedModule} from "primeng/api";
import {AvatarModule} from "primeng/avatar";
import {ButtonModule} from "primeng/button";
import {InputTextModule} from "primeng/inputtext";
import {ToolbarModule} from "primeng/toolbar";
import {SplitButtonModule} from "primeng/splitbutton";

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [
    SharedModule,
    AvatarModule,
    ButtonModule,
    InputTextModule,
    ToolbarModule,
    SplitButtonModule
  ],
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss'
})
export class HeaderComponent {

}
