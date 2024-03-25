import {Component} from '@angular/core';
import {RouterOutlet} from "@angular/router";
import {ButtonModule} from "primeng/button";
import {AutoCompleteCompleteEvent, AutoCompleteModule} from "primeng/autocomplete";
import {FormsModule} from "@angular/forms";
import {CheckboxModule} from "primeng/checkbox";
import {InputTextModule} from "primeng/inputtext";

@Component({
  standalone: true,
  selector: 'app-root',
  templateUrl: './app.component.html',
  imports: [
    RouterOutlet,
    ButtonModule,
    AutoCompleteModule,
    FormsModule,
    CheckboxModule,
    InputTextModule
  ],
  styleUrl: './app.component.scss'
})
export class AppComponent {
}
