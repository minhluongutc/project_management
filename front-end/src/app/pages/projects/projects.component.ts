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
  value = [
    {label: 'Apps', color: '#34d399', value: 0},
    {label: 'Messages', color: '#fbbf24', value: 0},
    {label: 'Media', color: '#60a5fa', value: 0},
    {label: 'System', color: '#c084fc', value: 0}
  ];
  value2 = [
    {label: "Reject", color: "#ff0000", value: 0},
    {label: "Done", color: "#f60008", value: 0},
    {label: "Reopen", color: "#ee0011", value: 0},
    {label: "Resolve", color: "#e50019", value: 0},
    {label: "Deploy waiting", color: "#dd0022", value: 0},
    {label: "Confirmed", color: "#d4002a", value: 0},
    {label: "New", color: "#cc0033", value: 0}
  ]

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
      // if (res?.data?.statusPercents) {
      //   for (let i = 0; i < res.data.statusPercents.length; i++) {
      //     const meterGroup: MeterGroup = {
      //       label: res?.data?.statusPercents[i]?.name,
      //       color: this.generateColor(i),
      //       value: res?.data?.statusPercents[i]?.percent || 0,
      //     }
      //     this.meterGroup.push(meterGroup);
      //   }
      //   console.log(this.meterGroup)
      // }
    } catch (err: any) {
      this.createErrorToast('Lỗi', err.message);
    }
  }

  onDetailProject(id: string) {
    this.projectStoreService.id = id;
    this.router.navigateByUrl(`/projects/${id}`)
  }

  generateColor(i: number): string {
    const red = Math.floor(255 - (i * 8.5));
    const blue = Math.floor(i * 8.5);

    // Convert the RGB values to hexadecimal format
    const redHex = red.toString(16).padStart(2, '0');
    const blueHex = blue.toString(16).padStart(2, '0');

    // Return the color in hexadecimal format
    return '#' + redHex + '00' + blueHex;
  }

  getIndexInRowData(rowData: any): number {
    // console.log(rowData)
    console.log(this.listMeterGroup[this.listDataTree.findIndex((item: any) => item.data.id === rowData.id)])
    return this.listDataTree.findIndex((item: any) => item.data.id === rowData.id) == undefined ? 0 : this.listDataTree.findIndex((item: any) => item.data.id === rowData.id);
  }

}
