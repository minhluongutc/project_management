import {Component, Injector, OnInit} from '@angular/core';
import {BaseComponent} from "../../../share/ui/base-component/base.component";
import {TaskService} from "../../../service/task.service";
import {ParamMap} from "@angular/router";
import {Column} from "../../../models/column.model";
import {PRIORIES, SEVERITIES} from "../../../share/constants/data.constants";
import FileSaver from 'file-saver';
import {ConfirmationService} from "primeng/api";
import {DynamicDialogRef} from "primeng/dynamicdialog";
import {TaskCreateComponent} from "../task-create/task-create.component";

@Component({
  selector: 'app-list-view',
  templateUrl: './list-view.component.html',
  styleUrl: './list-view.component.scss',
  providers: [ConfirmationService]
})
export class ListViewComponent extends BaseComponent implements OnInit {

  cols!: Column[];

  exportColumns!: any[];
  dynamicDialogRef: DynamicDialogRef | undefined;

  constructor(injector: Injector,
              private taskService: TaskService,
              private confirmationService: ConfirmationService,
  ) {
    super(injector);
  }

  async ngOnInit(): Promise<any> {
    this.route.queryParamMap.subscribe((params: ParamMap) => {
      console.log('params:', params);
      this.getTasks(params);
    })
  }

  getTasks(queryParams: any) {
    console.log(queryParams)
    let data = queryParams.params;
    const projectId = this.route.snapshot?.parent?.parent?.paramMap.get('id') || data.projectId;
    data = {
      ...data,
      priority: data?.priority?.join(',') || null,
      severity: data?.severity?.join(',') || null,
    }
    if (projectId != null) {
      data = {
        ...data,
        statusIssueId: data?.statusIssueId?.join(',') || null,
        typeId: data?.typeId?.join(',') || null,
        categoryId: data?.categoryId?.join(',') || null,
        reviewUserId: data?.reviewUserId?.join(',') || null,
        assignUserId: data?.assignUserId?.join(',') || null,
        projectId
      }
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

  onDeleteTask(id: any) {
    this.taskService.deleteTask(id).subscribe(
      {
        next: (res: any) => {
          this.createSuccessToast('Thành công', 'Xóa công việc thành công');
          this.ngOnInit();
        }, error: (err: any) => {
          this.createErrorToast('Lỗi', err.message);
        }
      }
    )
  }

  confirmDelete(item: any, event: Event) {
    console.log(event)
    this.confirmationService.confirm({
      target: event.target as EventTarget,
      message: 'bạn có muốn xóa công việc này?',
      header: 'Xác nhận xóa',
      icon: 'pi pi-info-circle',
      acceptButtonStyleClass: "p-button-danger p-button-text",
      rejectButtonStyleClass: "p-button-text p-button-text",
      acceptIcon: "none",
      rejectIcon: "none",

      accept: () => {
        this.onDeleteTask(item.id)
      },
      reject: () => {
        console.log("is reject")
      }
    });
  }

  async onEdit(item: any) {
    const taskRes = await this.taskService.getTaskById(item.id).toPromise();
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
  }
}
