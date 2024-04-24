import {Component, Injector, OnInit} from '@angular/core';
import {BaseComponent} from "../../../share/ui/base-component/base.component";
import {TaskService} from "../../../service/task.service";
import {ParamMap} from "@angular/router";
import {Column} from "../../../models/column.model";
import {PRIORIES, SEVERITIES} from "../../../share/constants/data.constants";
import {TablePageEvent} from "primeng/table";
import FileSaver from 'file-saver';

@Component({
  selector: 'app-list-view',
  templateUrl: './list-view.component.html',
  styleUrl: './list-view.component.scss'
})
export class ListViewComponent extends BaseComponent implements OnInit {

  cols!: Column[];

  exportColumns!: any[];

  constructor(injector: Injector,
              private taskService: TaskService
  ) {
    super(injector);
  }

  ngOnInit(): void {
    this.route.queryParamMap.subscribe((params: ParamMap) => {
      console.log('params:', params);
      this.getTasks(params);
    })
  }

  getTasks(queryParams: any) {
    console.log(queryParams)
    let data = queryParams.params;
    const projectId = this.route.snapshot?.parent?.parent?.paramMap.get('id') || null;
    if (projectId != null) {
      data = { ...data, projectId: projectId }
    }
    this.taskService.getTasks(data).subscribe({
      next: (res: any) => {
        console.log('res:', res);
        this.listData = res?.data || [];
        this.totalRecords = this.listData.length || 0;

        // this.exportColumns = this.cols.map((col) => ({ title: col.header, dataKey: col.field }));
      }, error: (err: any) => {
        this.createErrorToast("Lỗi", err.message);
      }
    })
  }

  exportPdf() {
    // @ts-ignore
    import('jspdf').then((jsPDF) => {
      import('jspdf-autotable').then((x) => {
        const doc = new jsPDF.default('p', 'px', 'a4');
        (doc as any).autoTable(this.exportColumns, this.listData);
        doc.save('tasks.pdf');
      });
    });
  }

  exportExcel() {
    import('xlsx').then((xlsx) => {
      const worksheet = xlsx.utils.json_to_sheet(this.listData);
      const workbook = { Sheets: { data: worksheet }, SheetNames: ['data'] };
      const excelBuffer: any = xlsx.write(workbook, { bookType: 'xlsx', type: 'array' });
      this.saveAsExcelFile(excelBuffer, 'tasks');
    });
  }

  saveAsExcelFile(buffer: any, fileName: string): void {
    let EXCEL_TYPE = 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8';
    let EXCEL_EXTENSION = '.xlsx';
    const data: Blob = new Blob([buffer], {
      type: EXCEL_TYPE
    });
    FileSaver.saveAs(data, fileName + '_export_' + new Date().getTime() + EXCEL_EXTENSION);
  }

  protected readonly PRIORIES = PRIORIES;
  protected readonly SEVERITIES = SEVERITIES;
}
