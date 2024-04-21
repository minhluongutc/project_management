import {Component, Injector} from '@angular/core';
import {BehaviorSubject} from 'rxjs';
import {BaseComponent} from "../../share/ui/base-component/base.component";

@Component({
  selector: 'app-tasks',
  templateUrl: './tasks.component.html',
  styleUrls: ['./tasks.component.scss']
})
export class TasksComponent extends BaseComponent {
  isViewList: boolean = false;
  isViewTree: boolean = false;
  routerLink: string = 'list-view'

  constructor(injector: Injector) {
    console.log('init')
    super(injector);
    // this.changeStage(this.isViewList);
    // this.changeStageViewList(this.isViewTree)
  }

  changeStage(value: boolean) {
    this.isViewTree = false;
    if (value) {
      this.router.navigate(['./list-view'], { relativeTo: this.route })
    } else {
      this.router.navigate(['./detail-view'], { relativeTo: this.route })
    }
  }

  changeStageViewList(value: any) {
    if (value) {
      this.router.navigate(['./list-view-tree'], { relativeTo: this.route })
    } else {
      this.router.navigate(['./list-view'], { relativeTo: this.route })
    }
  }
}
