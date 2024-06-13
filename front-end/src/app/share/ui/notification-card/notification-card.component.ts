import {
  Component,
  EventEmitter,
  Injector,
  Input,
  OnChanges,
  OnInit,
  Output,
  SimpleChanges,
  TrackByFunction
} from '@angular/core';
import {NOTIFICATION_VALUE} from "../../constants/data.constants";
import {BaseComponent} from "../base-component/base.component";
import _default from "chart.js/dist/plugins/plugin.legend";
import onClick = _default.defaults.onClick;

@Component({
  selector: 'jira-notification-card',
  templateUrl: './notification-card.component.html',
  styleUrl: './notification-card.component.scss'
})
export class NotificationCardComponent extends BaseComponent implements OnInit, OnChanges{
  @Input() notifications: any;
  @Output() onClickCard: EventEmitter<any> = new EventEmitter<any>();

  constructor(injector: Injector) {
    super(injector);
  }

  ngOnInit(): void {
    throw new Error('Method not implemented.');
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['notifications']) {
      console.log(changes)
      console.log('notifications', this.notifications);
    }
  }

  protected readonly NOTIFICATION_VALUE = NOTIFICATION_VALUE;
  trackByFn: TrackByFunction<any> = (index, item) => item.id;

  eventClick(notification: any) {
    this.onClickCard.emit(notification);
  }
}
