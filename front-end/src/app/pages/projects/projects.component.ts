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
    console.log(rowData)
    return meterList;
  }

  generateColor(i: number): string {
    switch (i) {
      case 0:
        return '#34d399';
      case 1:
        return '#fbbf24';
      case 2:
        return '#60a5fa';
      case 3:
        return '#c084fc';
      case 4:
        return '#ff0000';
      case 5:
        return 'rgba(234,232,232,0.57)';
      case 6:
        return '#68c3ab';
      case 7:
        return '#399bab';
      case 8:
        return '#43860c';
      case 9:
        return '#68b7ab';
      case 10:
        return '#6653ab';
      case 11:
        return '#17815c';
      case 12:
        return '#5862d0';
      case 13:
        return '#26b928';
      case 14:
        return '#43641f';
      case 15:
        return '#1e2cc4';
      case 16:
        return '#074232';
      case 17:
        return '#4122b2';
      case 18:
        return '#6aec8f';
      case 19:
        return '#0e3405';
      case 20:
        return '#b2eec5';
      case 21:
        return '#2a5730';
      default:
        return '#000000';
    }
  }
}
