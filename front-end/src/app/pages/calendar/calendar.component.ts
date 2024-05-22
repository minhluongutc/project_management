import {ChangeDetectorRef, Component, Injector, OnInit, ViewChild} from '@angular/core';
import {CalendarOptions, DateSelectArg, EventApi, EventChangeArg, EventClickArg, EventInput} from '@fullcalendar/core';
import {UserCalendarService} from "../../service/user-calendar.service";
import {FullCalendarComponent} from "@fullcalendar/angular";
import dayGridPlugin from '@fullcalendar/daygrid';
import timeGridPlugin from "@fullcalendar/timegrid";
import interactionPlugin from '@fullcalendar/interaction';
import listPlugin from '@fullcalendar/list';
import {BaseComponent} from "../../share/ui/base-component/base.component";
import {Validators} from "@angular/forms";
import {ProjectService} from "../../service/project.service";
import {TreeNodeSelectEvent, TreeNodeUnSelectEvent} from "primeng/tree";
import {TaskService} from "../../service/task.service";

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

  listEvent: EventInput[] = []
  events: EventInput[] = []
  calendarVisible: boolean = true;

  currentEvents: EventApi[] = [];
  @ViewChild('calendar') calendarComponent: FullCalendarComponent | undefined;
  calendarOptions!: CalendarOptions
  visibleModalAddEvent: boolean = false;

  constructor(injector: Injector,
              private projectService: ProjectService,
              private taskService: TaskService,
              private changeDetector: ChangeDetectorRef,
              private userCalendarService: UserCalendarService) {
    super(injector);
    this.getUserCalendar();
    this.getProjects();
  }

  async ngOnInit() {
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
      selectable: true,
      selectMirror: true,
      dayMaxEvents: true,
      select: this.handleDateSelect.bind(this),
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

  getUserCalendar() {
    this.userCalendarService.getUserCalendar({projectId: null})
      .subscribe({
        next: (res) => {
          console.log(res);
          this.listEvent = res.data;
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
  }

  onUnselectProject($event: TreeNodeUnSelectEvent) {
    this.listTask = [];
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
    console.log(new Date(selectInfo.startStr).setHours(0))
    console.log(new Date(selectInfo.startStr))

    this.form.patchValue({
      start: selectInfo.allDay ? new Date(new Date(selectInfo.startStr).setHours(0)) : new Date(selectInfo.startStr),
      end: selectInfo.allDay ? new Date(new Date(selectInfo.endStr).setHours(0)) : new Date(selectInfo.endStr),
      allDay: selectInfo.allDay
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
    this.userCalendarService.getUserCalendarById(clickInfo.event.id).subscribe({
      next: async (res) => {
        console.log(res)
        let projectValue!: any;
        let taskValue!: any;
        const projectId = res.data?.projectId
        const projectSelected = this.findProject(this.listProject, projectId);
        projectValue = {...projectSelected}

        if (projectId) {

          try {
            const data = {
              projectId: projectId,
            }
            const resTask: any = await this.taskService.getTaskAccordingLevel(data).toPromise();
            const listTask = resTask.data;
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

          console.log("list task", this.listTask)
          const taskSelected = this.findProject(this.listTask, res.data?.taskId);
          taskValue = {...taskSelected}
        }

        this.form.patchValue({
          title: res.data?.title,
          description: res.data?.description,
          start: new Date(res.data?.start) || null,
          end: new Date(res.data?.end) || null,
          allDay: res.data?.allDay,
          projectId: projectValue,
          taskId: taskValue,
        })
        this.visibleModalAddEvent = true;
      }
    })
    // if (confirm(`Are you sure you want to delete the event '${clickInfo.event.title}'`)) {
    //   clickInfo.event.remove();
    // }
  }

  handleEvents(events: EventApi[]) {
    console.log(events)
    this.currentEvents = events;
    this.changeDetector.detectChanges(); // workaround for pressionChangedAfterItHasBeenCheckedError
  }

  handleChange(event: EventChangeArg) {
    const data = {
      changeDateOnly: true,
      start: event.event.start,
      end: event.event.end,
      allDay: event.event.allDay
    }
    this.updateEvent(event.event.id, data);
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
    };
    if (this.isEdit) {
      let allDay = data?.allDay;
      if (data?.start.getHours()==0 && data?.end.getHours()==0) {
        allDay = true;
      }
      const data2Edit = {
        ...data,
        allDay
      }
      this.updateEvent(this.eventIdSelected, data2Edit);
    } else {
      this.userCalendarService.addUserCalendar(data)
        .subscribe({
          next: (res) => {
            this.createSuccessToast('Thành công', 'Thêm sự kiện thành công');
            this.getUserCalendar();
            this.form.reset()
            this.visibleModalAddEvent = false;
          }
        })
    }
  }

  updateEvent(id: any, data: any) {
    this.userCalendarService.updateUserCalendar(id, data)
      .subscribe({
        next: (res) => {
          this.createSuccessToast('Thành công', 'Cập nhật sự kiện thành công');
          this.getUserCalendar();
          this.form.reset()
          this.visibleModalAddEvent = false;
        }
      })
  }
}
