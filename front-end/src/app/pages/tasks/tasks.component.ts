import {Component, Injector} from '@angular/core';
import {BehaviorSubject} from 'rxjs';
import {BaseComponent} from "../../share/ui/base-component/base.component";
import {SelectButtonChangeEvent} from "primeng/selectbutton";

@Component({
  selector: 'app-tasks',
  templateUrl: './tasks.component.html',
  styleUrls: ['./tasks.component.scss']
})
export class TasksComponent extends BaseComponent {
  stateView: any[] = [{label: 'listView', value: 0}, {label: 'detailView', value: 1}];
  value: number = 0;

  constructor(injector: Injector) {
    super(injector);
  }

  checkChange($event: SelectButtonChangeEvent) {
    if ($event.value === 0) {
      this.router.navigate(['/tasks/list-view'])
    } else {
      this.router.navigate(['/tasks/detail-view'])
    }
  }
}
