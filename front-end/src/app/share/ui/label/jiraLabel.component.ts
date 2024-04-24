import {Component, Input} from '@angular/core';

@Component({
  selector: 'jira-label',
  templateUrl: './jiraLabel.component.html',
  styleUrl: './jiraLabel.component.scss'
})
export class JiraLabelComponent {
  @Input() jiraLabel: string = 'default label';
  @Input() jiraRequired: boolean = false;
}
