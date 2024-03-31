import {Component, Injector, OnInit} from '@angular/core';
import {TreeNode} from "primeng/api";
import {Column} from "../../models/column.model";
import {BaseComponent} from "../../share/ui/base-component/base.component";
import {ProjectService} from "../../service/project.service";

@Component({
  selector: 'app-projects',
  templateUrl: './projects.component.html',
  styleUrl: './projects.component.scss'
})
export class ProjectsComponent extends BaseComponent implements OnInit {
  files!: TreeNode[];

  cols!: Column[];

  constructor(injector: Injector,
              private projectService: ProjectService
  ) {
    super(injector);
  }

  async ngOnInit() {
    this.cols = [
      {field: 'name', header: 'Tên dự án'},
      {field: 'description', header: 'Mô tả'},
    ];
    await this.getProjects();
  }

  async getProjects() {
    try {
      const res: any = await this.projectService.getProjects(this.user.id, {}).toPromise();
      console.log('res:', res);
      this.listDataTree = res.data;
    } catch (err: any) {
      this.createErrorToast('Lỗi', err.message);
    }
  }
}
