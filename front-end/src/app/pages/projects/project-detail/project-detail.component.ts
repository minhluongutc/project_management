import {Component, Injector, OnInit} from '@angular/core';
import {ProjectStoreService} from "../project-store.service";
import {ProjectService} from "../../../service/project.service";
import {BaseComponent} from "../../../share/ui/base-component/base.component";

@Component({
  selector: 'app-project-detail',
  templateUrl: './project-detail.component.html',
  styleUrl: './project-detail.component.scss'
})
export class ProjectDetailComponent extends BaseComponent implements OnInit{
  openSidebar: boolean = true;

  projectId: any = ''
  project: any = {};

  menuSidebar = [
    {
      link_name: "Kanban Board",
      link: "./kanban-board",
      icon: "pi pi-table",
      sub_menu: []
    },
    {
      link_name: "Backlog",
      link: "./backlog",
      icon: "pi pi-undo",
      sub_menu: []
    },
    {
      link_name: "Công việc",
      link: "./tasks",
      icon: "pi pi-list",
      sub_menu: []
    },
    {
      link_name: "Kiểm soát quền",
      link: "./permissions",
      icon: "pi pi-user",
      sub_menu: []
    },
    {
      link_name: "Thống kê",
      link: "statistics",
      icon: "pi pi-chart-line",
      sub_menu: []
    },
    {
      link_name: "Project setting",
      link: "setting",
      icon: "pi pi-cog",
      sub_menu: []
    },{
      link_name: "demo dropdown",
      link: null,
      icon: "pi pi-check",
      sub_menu: [
        {
          link_name: "HTML & CSS",
          link: "/html-n-css",
        }, {
          link_name: "JavaScript",
          link: "/javascript",
        }, {
          link_name: "PHP & MySQL",
          link: "/php-n-mysql",
        }
      ]
    }
  ]

  constructor(injector: Injector,
              private projectService: ProjectService,
              private projectStoreService: ProjectStoreService
  ) {
    super(injector);
    this.projectId = this.route.snapshot.paramMap.get('id');
    this.getProject(this.projectId);
  }

  ngOnInit() {
  }

  getProject(projectId: string) {
    this.projectService.getProject(projectId).subscribe({
      next: (res: any) => {
        console.log('res:', res);
        this.project = res.data;
      }, error: (err: any) => {
        this.createErrorToast("Lỗi", err.message);
      }
    })
  }

  showSubmenu(itemEl: HTMLElement) {
    itemEl.classList.toggle("showMenu");
  }
}
