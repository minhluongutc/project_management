import { Component } from '@angular/core';
import {JiraInputTextComponent} from "../../share/ui/jira-input-text/jira-input-text.component";
import {FormsModule, NgControl} from "@angular/forms";
import {InputTextModule} from "primeng/inputtext";
import {DecimalPipe} from "@angular/common";

@Component({
  selector: 'app-projects',
  standalone: true,
  imports: [
    JiraInputTextComponent,
    FormsModule,
    InputTextModule
  ],
  providers: [DecimalPipe],
  templateUrl: './projects.component.html',
  styleUrl: './projects.component.scss'
})
export class ProjectsComponent {
  constructor() {
  }
}
