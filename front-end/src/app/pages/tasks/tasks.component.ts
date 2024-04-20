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
  routerLink: string = './list-view'

  constructor(injector: Injector) {
    console.log('init')
    super(injector);
    // this.changeStage(this.isViewList);
    // this.changeStageViewList(this.isViewTree)
  }

  changeStage(value: boolean) {
    this.isViewTree = false;
    if (value) {
      this.routerLink = './list-view'
      this.router.navigate(['/tasks/list-view'])
    } else {
      this.routerLink = './detail-view'
      this.router.navigate(['/tasks/detail-view'])
    }
  }

  changeStageViewList(value: any) {
    if (value) {
      this.routerLink = './list-view-tree'
      this.router.navigate(['/tasks/list-view-tree'])
    } else {
      this.routerLink = './list-view'
      this.router.navigate(['/tasks/list-view'])
    }
  }
}
