import {Component, Injector, OnDestroy, OnInit} from '@angular/core';
import {ProjectStoreService} from "../project-store.service";
import {ProjectService} from "../../../service/project.service";
import {BaseComponent} from "../../../share/ui/base-component/base.component";
import {ProjectUserService} from "../../../service/project-user.service";

@Component({
  selector: 'app-project-detail',
  templateUrl: './project-detail.component.html',
  styleUrl: './project-detail.component.scss'
})
export class ProjectDetailComponent extends BaseComponent implements OnInit, OnDestroy{
  openSidebar: boolean = false;

  projectId: any = ''
  project: any = {};

  menuSidebar: any[] = [
    // {
    //   link_name: "Kanban Board",
    //   link: "./kanban-board",
    //   icon: "pi pi-table",
    //   sub_menu: []
    // },
    // {
    //   link_name: "Backlog",
    //   link: "./backlog",
    //   icon: "pi pi-undo",
    //   sub_menu: []
    // },
    {
      link_name: "Công việc",
      link: "./tasks",
      icon: "pi pi-list",
      sub_menu: []
    },
    {
      link_name: "Thành viên",
      link: "./users",
      icon: "pi pi-user",
      sub_menu: []
    },
    // {
    //   link_name: "Thống kê",
    //   link: "./statistics",
    //   icon: "pi pi-chart-line",
    //   sub_menu: []
    // },
  ]

  constructor(injector: Injector,
              private projectService: ProjectService
  ) {
    super(injector);
    this.projectId = this.route.snapshot.paramMap.get('id');
    this.getProject(this.projectId);
    this.projectStoreService.id = this.projectId;
    // this.setRoleInProject(this.projectId, this.user.id);
  }

  async ngOnInit() {
    await this.setRoleInProject(this.projectId, this.user.id);
    console.log(this.projectStoreService.role)
    if (this.isRolePMOrAdmin()) {
      this.menuSidebar.push(
        {
          link_name: "Cấu hình dự án",
          link: null,
          icon: "pi pi-cog",
          sub_menu: [
            {
              link_name: "Thông tin chung",
              link: "./general-information",
            },
            {
              link_name: "Danh mục",
              link: "./category",
            },
            {
              link_name: "Loại công việc",
              link: "./issue-type",
            },
            {
              link_name: "Trạng thái công việc",
              link: "./status-issue",
            }
          ]
        })
    }
  }

  override ngOnDestroy() {
    super.ngOnDestroy();
    this.projectStoreService.resetStore();
  }

  getProject(projectId: string) {
    this.projectService.getProject(projectId).subscribe({
      next: (res: any) => {
        console.log('res:', res);
        this.project = res.data;
      }, error: (err: any) => {
        this.createErrorToast("Lỗi", "Không tìm thấy dự án");
      }
    })
  }

  showSubmenu(itemEl: HTMLElement) {
    itemEl.classList.toggle("showMenu");
  }
}
