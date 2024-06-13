import {ChangeDetectorRef, Component, Injector, OnInit, ViewChild} from '@angular/core';
import {CalendarOptions, DateSelectArg, EventApi, EventChangeArg, EventClickArg, EventInput} from '@fullcalendar/core';
import {UserCalendarService} from "../../service/user-calendar.service";
import {FullCalendarComponent} from "@fullcalendar/angular";
import dayGridPlugin from '@fullcalendar/daygrid';
import timeGridPlugin from "@fullcalendar/timegrid";
import interactionPlugin, {Draggable, DropArg} from '@fullcalendar/interaction';
import listPlugin from '@fullcalendar/list';
import {BaseComponent} from "../../share/ui/base-component/base.component";
import {Validators} from "@angular/forms";
import {ProjectService} from "../../service/project.service";
import {TreeNodeSelectEvent, TreeNodeUnSelectEvent} from "primeng/tree";
import {TaskService} from "../../service/task.service";
import {DynamicDialogRef} from "primeng/dynamicdialog";
import {TaskCreateComponent} from "../tasks/task-create/task-create.component";
import viLocale from '@fullcalendar/core/locales/vi';


@Component({
  selector: 'app-calendar',
  templateUrl: './calendar.component.html',
  styleUrl: './calendar.component.scss'
})
export class CalendarComponent extends BaseComponent implements OnInit {
  listProject: any[] = [];
  listTask: any[] = [];
  isEdit: boolean = false;
  eventIdSelected: any;
  taskIdSelected: any;

  listEvent: EventInput[] = []
  events: EventInput[] = []
  calendarVisible: boolean = true;

  projectIdSelected: string = '';
  taskSelected: any;

  currentEvents: EventApi[] = [];
  @ViewChild('calendar') calendarComponent: FullCalendarComponent | undefined;
  calendarOptions!: CalendarOptions
  visibleModalAddEvent: boolean = false;

  dynamicDialogRef: DynamicDialogRef | undefined;

  constructor(injector: Injector,
              private projectService: ProjectService,
              private taskService: TaskService,
              private changeDetector: ChangeDetectorRef,
              private userCalendarService: UserCalendarService) {
    super(injector);
    this.getUserCalendar(null);
    this.getProjects();
    // this.getTaskInSidebar(this.projectIdSelected);
  }

  async ngOnInit() {
    let draggableEl = document.getElementById('external-events');

    if (draggableEl) {
      new Draggable(draggableEl, {
        itemSelector: '.fc-event-custom',
        eventData: function (eventEl: any) {
          console.log(eventEl);
          // if (self.theCheckbox) {
          //   eventEl.parentNode.removeChild(eventEl);
          // }
          return {
            title: eventEl.innerText
          };
        }
      });
    }

    this.buildForm();
    console.log(this.listEvent)
    this.calendarOptions = {
      plugins: [
        interactionPlugin,
        dayGridPlugin,
        timeGridPlugin,
        listPlugin,
      ],
      headerToolbar: {
        left: 'prev,next today',
        center: 'title',
        right: 'dayGridMonth,timeGridWeek,timeGridDay,listWeek'
      },
      initialView: 'dayGridMonth', // alternatively, use the `events` setting to fetch from a feed
      weekends: true,
      editable: true,
      // selectable: true,
      selectMirror: true,
      dayMaxEvents: true,
      droppable: true,
      locale: viLocale,
      drop: this.onDropEvent.bind(this),
      // select: this.handleDateSelect.bind(this),
      eventClick: this.handleEventClick.bind(this),
      eventsSet: this.handleEvents.bind(this),
      eventChange: this.handleChange.bind(this)
      /* you can update a remote database when these fire:
      eventAdd:
      eventRemove:
      */
    };
    console.log(this.calendarOptions)
  }

  // getTaskInSidebar(projectId: any) {
  //   this.taskService.getTasksNotSchedule(projectId).subscribe({
  //     next: (res: any) => {
  //       this.listData = res?.data || [];
  //       this.totalRecords = this.listData.length || 0;
  //     }, error: (err: any) => {
  //       this.createErrorToast("Lỗi", err.message);
  //     }
  //   })
  // }

  // onSelectFilterProject($event: TreeNodeSelectEvent) {
  //   this.getTaskInSidebar($event.node.key)
  // }
  //
  // onUnselectFilterProject($event: TreeNodeUnSelectEvent) {
  //   this.getTaskInSidebar(null);
  // }

  getUserCalendar(projectId: any) {
    this.userCalendarService.getUserCalendar({projectId})
      .subscribe({
        next: (res) => {
          console.log(res);
          this.listEvent = res.data;
          // add color to each event
          this.listEvent = this.listEvent.map((item: any, index: number) => {
            return {
              ...item,
              color: this.generateColor(item.statusCode-1)
            }
          })
        }
      })
  }

  buildForm() {
    this.form = this.fb.group({
      title: [null, [Validators.required]],
      description: [null, []],
      projectId: [null, []],
      taskId: [null, []],
      start: [null, [Validators.required]],
      end: [null, []],
      allDay: [false, []],
    });
  }

  async getProjects() {
    try {
      const res: any = await this.projectService.getProjects(this.user.id, {}).toPromise();
      this.listProject = res.data;
      this.changeStructureListProject();
    } catch (err: any) {
      this.createErrorToast('Lỗi', err.message);
    }
  }

  changeStructureListProject() {
    this.listProject = this.listProject.map((item: any) => {
      return {
        label: item.data.name,
        key: item.data.id,
        children: this.setChildProject(item)
      }
    })
  }

  setChildProject(item: any): any {
    let children: any[] = []
    if (item.children.length !== 0) {
      item.children.forEach((child: any) => {
        children.push({
          label: child.data.name,
          key: child.data.id,
          children: this.setChildProject(child)
        })
      })
    }
    return children;
  }

  onSelectProject($event: TreeNodeSelectEvent) {
    this.getTasks($event.node.key)
    this.getUserCalendar($event.node.key);
  }

  onUnselectProject($event: TreeNodeUnSelectEvent) {
    this.getTasks('')
    this.getUserCalendar(null);
  }

  async getTasks(projectId: string | undefined) {
    try {
      const data = {
        projectId: projectId,
      }
      const res: any = await this.taskService.getTaskAccordingLevel(data).toPromise();
      const listTask = res.data;
      this.listTask = listTask.map((item: any) => {
        return {
          label: item.data.subject,
          key: item.data.id,
          children: this.setChildTask(item)
        }
      })
    } catch (err: any) {
      this.createErrorToast('Lỗi', err.message);
    }
  }

  setChildTask(item: any): any {
    let children: any[] = []
    if (item.children.length !== 0) {
      item.children.forEach((child: any) => {
        children.push({
          label: child.data.subject,
          key: child.data.id,
          children: this.setChildTask(child)
        })
      })
    }
    return children;
  }


  handleCalendarToggle() {
    this.calendarVisible = !this.calendarVisible;
  }

  handleWeekendsToggle() {
    // this.calendarOptions.update((options: any) => {
    //   options.weekends = !options.weekends;
    //   return options
    // });
  }

  handleDateSelect(selectInfo: DateSelectArg) {
    this.visibleModalAddEvent = true;
    console.log(selectInfo);

    this.form.patchValue({
      start: selectInfo.allDay ? new Date(new Date(selectInfo.startStr).setHours(0)) : new Date(selectInfo.startStr),
      end: selectInfo.allDay ? new Date(new Date(selectInfo.endStr).setHours(0)) : new Date(selectInfo.endStr),
      allDay: this.checkAllDay(new Date(selectInfo.startStr), new Date(selectInfo.endStr))
    })
    // const title = '123'
    // const calendarApi = selectInfo.view.calendar;
    //
    // calendarApi.unselect(); // clear date selection
    //
    // if (title) {
    //   calendarApi.addEvent({
    //     id: createEventId(),
    //     title,
    //     start: selectInfo.startStr,
    //     end: selectInfo.endStr,
    //     allDay: selectInfo.allDay
    //   });
    // }
  }

  async handleEventClick(clickInfo: EventClickArg) {
    console.log(clickInfo)
    this.eventIdSelected = clickInfo.event.id;
    this.isEdit = true;

    const taskRes = await this.taskService.getTaskById(clickInfo.event.id).toPromise();
    const task = taskRes.data;
    this.dynamicDialogRef = this.dialogService.open(TaskCreateComponent, {
      header: 'Chỉnh sửa công việc',
      width: '60vw',
      contentStyle: { overflow: 'auto', 'margin-bottom': '69px' },
      breakpoints: {
        '960px': '75vw',
        '640px': '90vw'
      },
      data: {
        task: task
      }
    });
    // this.userCalendarService.getUserCalendarById(clickInfo.event.id).subscribe({
    //   next: async (res) => {
    //     console.log(res)
    //     let projectValue!: any;
    //     let taskValue!: any;
    //     const projectId = res.data?.projectId
    //     const projectSelected = this.findProject(this.listProject, projectId);
    //     projectValue = {...projectSelected}
    //
    //     if (projectId) {
    //
    //       try {
            // const data = {
            //   projectId: projectId,
            // }
          //   const resTask: any = await this.taskService.getTaskAccordingLevel({}).toPromise();
          //   const listTask = resTask.data;
          //   this.listTask = listTask.map((item: any) => {
          //     return {
          //       label: item.data.subject,
          //       key: item.data.id,
          //       children: this.setChildTask(item)
          //     }
          //   })
          // } catch (err: any) {
          //   this.createErrorToast('Lỗi', err.message);
          // }

          // console.log("list task", this.listTask)
          // const taskSelected = this.findProject(this.listTask, res.data?.taskId);
          // taskValue = {...taskSelected}
        // }
    //
    //     this.form.patchValue({
    //       title: res.data?.title,
    //       description: res.data?.description,
    //       start: new Date(res.data?.start) || null,
    //       end: new Date(res.data?.end) || null,
    //       allDay: res.data?.allDay,
    //       projectId: projectValue,
    //       taskId: taskValue,
    //     })
    //     this.visibleModalAddEvent = true;
    //   }
    // })
  }

  handleEvents(events: EventApi[]) {
  }

  handleChange(event: EventChangeArg) {
    const data = {
      changeDateOnly: true,
      start: event.event.start,
      end: event.event.end,
      allDay: event.event.allDay
    }
    this.updateEvent(event.event.id, data);
    console.log("is change")
  }

  onCloseAddModal() {
    this.visibleModalAddEvent = false;
    this.isEdit = false;
    this.eventIdSelected = ''
    this.listTask = []
    this.form.reset()
  }

  onSubmitForm() {
    const data = {
      ...this.form.value,
      projectId: this.form.value.projectId?.key,
      taskId: this.form.value.taskId?.key,
      allDay: this.checkAllDay(this.form.value.start, this.form.value.end)
    };
    if (this.isEdit) {
      this.updateEvent(this.eventIdSelected, data);
    } else {
      this.createEvent(data);
    }
  }

  updateEvent(id: any, data: any) {
    this.userCalendarService.updateUserCalendar(id, data)
      .subscribe({
        next: (res) => {
          this.createSuccessToast('Thành công', 'Cập nhật sự kiện thành công');
          this.getUserCalendar(null);
          this.form.reset()
          this.visibleModalAddEvent = false;
        }
      })
  }

  createEvent(data: any) {
    this.userCalendarService.addUserCalendar(data)
      .subscribe({
        next: (res) => {
          this.createSuccessToast('Thành công', 'Thêm sự kiện thành công');
          this.getUserCalendar(null);
          this.form.reset();
          this.visibleModalAddEvent = false;
        }
      })
  }

  onDropEvent(event: DropArg) {
    console.log("ondrop", event)
    let date = new Date(event.dateStr);
    const data = {
      title: this.taskSelected?.subject,
      start: event.date,
      end: event.allDay ? new Date(new Date(date.setDate(date.getDate() + 1)).setHours(0, 0, 0)) : new Date(event.date.setHours(event.date.getHours() + (this.taskSelected?.estimateTime || 1))),
      allDay: event.allDay,
      taskId: this.taskSelected?.id,
      projectId: this.taskSelected?.projectId
    }
    console.log(event.dateStr)
    console.log(new Date(date.setDate(date.getDate() + 1)));
    this.createEvent(data);
    // this.getTaskInSidebar(null);
    console.log("data", data)
  }

  checkAllDay = (start: Date, end: Date): boolean => {
    if (this.isEdit) {
      return (start.getHours()) == 0 && (end.getHours()) == 0 && start.getMinutes() == 0 && end.getMinutes() == 0;
    }
    console.log((start.getHours() - 7) == 0 && (end.getHours() - 7) == 0 && start.getMinutes() == 0 && end.getMinutes() == 0)
    return (start.getHours() - 7) == 0 && (end.getHours() - 7) == 0 && start.getMinutes() == 0 && end.getMinutes() == 0;
  };

  onMouseDownTask(item: any) {
    this.taskSelected = item;
  }
}
