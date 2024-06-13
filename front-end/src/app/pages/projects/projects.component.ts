import {Component, Injector, OnInit} from '@angular/core';
import {TreeNode} from "primeng/api";
import {Column} from "../../models/column.model";
import {BaseComponent} from "../../share/ui/base-component/base.component";
import {ProjectService} from "../../service/project.service";
import {ProjectStoreService} from "./project-store.service";
import {MeterGroup} from "../../models/meter-group.model";

@Component({
  selector: 'app-projects',
  templateUrl: './projects.component.html',
  styleUrl: './projects.component.scss'
})
export class ProjectsComponent extends BaseComponent implements OnInit {
  files!: TreeNode[];
  cols!: Column[];
  listMeterGroup: any[] = [];

  constructor(injector: Injector,
              private projectService: ProjectService,
              private projectStoreService: ProjectStoreService
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
      for (let i = 0; i < res.data.length; i++) {
        // console.log(res.data[i])
        if (res.data[i].data?.statusPercents.length > 0) {
          let meterGroup: MeterGroup[] = [];
          for (let j = 0; j < res.data[i].data.statusPercents.length; j++) {
            const meterGroupItem: MeterGroup = {
              label: res.data[i].data.statusPercents[j].name,
              color: this.generateColor(j),
              value: res.data[i].data.statusPercents[j].percent || 0,
            }
            // console.log(meterGroupItem)
            meterGroup.push(meterGroupItem);
          }
          console.log(meterGroup)
          this.listMeterGroup.push(meterGroup);
        }
      }
    } catch (err: any) {
      this.createErrorToast('Lỗi', err.message);
    }
  }

  onDetailProject(id: string) {
    this.projectStoreService.id = id;
    this.router.navigateByUrl(`/projects/${id}`)
  }

  // getIndexInRowData(rowData: any): number {
  //   // console.log(rowData)
  //   console.log('index', this.listDataTree.findIndex((item: any) => item.data.id === rowData.id) == undefined ? 0 : this.listDataTree.findIndex((item: any) => item.data.id === rowData.id));
  //   return this.listDataTree.findIndex((item: any) => item.data.id === rowData.id) == undefined ? 0 : this.listDataTree.findIndex((item: any) => item.data.id === rowData.id);
  // }

  getMeterGroup(rowData: any) {
    const meterList: any[] = [];
    for (let i = 0; i < rowData.statusPercents.length; i++) {
      const meterGroup: MeterGroup = {
        label: rowData.statusPercents[i].name,
        color: this.generateColor(i),
        value: rowData.statusPercents[i].percent || 0,
      }
      meterList.push(meterGroup);
    }
    return meterList;
  }


}
