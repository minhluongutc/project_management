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
    this.getProjects();
  }

  ngOnInit() {
    // this.files = [];
    // for (let i = 0; i < 50; i++) {
    //   let node = {
    //     data: {
    //       name: 'Item ' + i,
    //       size: Math.floor(Math.random() * 1000) + 1 + 'kb',
    //       type: 'Type ' + i
    //     },
    //     children: [
    //       {
    //         data: {
    //           name: 'Item ' + i + ' - 0',
    //           size: Math.floor(Math.random() * 1000) + 1 + 'kb',
    //           type: 'Type ' + i
    //         }
    //       }
    //     ]
    //   };

      // this.files.push(node);
    // }

    this.cols = [
      {field: 'name', header: 'Name'},
      {field: 'description', header: 'Chi tiết'}
    ];
  }

  getProjects() {
    this.projectService.getProjects(this.user.id, {})
      .subscribe({
        next: (res: any) => {
          console.log('res:', res)
          this.listData = res.data;
        }, error: (err: any) => {
          this.createErrorToast('Error', err.message)
        }
      })
  }
}
