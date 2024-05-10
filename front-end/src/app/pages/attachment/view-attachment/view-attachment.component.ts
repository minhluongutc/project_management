import {Component, Injector, OnInit} from '@angular/core';
import {MenuFileItem} from "../../../share/ui/sidebar-secondary/sidebar-secondary.component";
import {BaseComponent} from "../../../share/ui/base-component/base.component";
import {TaskService} from "../../../service/task.service";
import {DocumentService} from "../../../service/document.service";
import * as wjcCore from '@mescius/wijmo';
import * as wjcXlsx from '@mescius/wijmo.xlsx';
import '@mescius/wijmo.styles/wijmo.css';
import {ParamMap} from "@angular/router";

@Component({
  selector: 'app-view-attachment',
  templateUrl: './view-attachment.component.html',
  styleUrl: './view-attachment.component.scss',
})
export class ViewAttachmentComponent extends BaseComponent implements OnInit {
  // view excel file
  // @ts-ignore
  workbook: wjcXlsx.Workbook;
  // @ts-ignore
  sheetIndex: number;
  //
  menuItems: MenuFileItem[] = []
  itemSelected: MenuFileItem = {name: '', urlPath: '', filePath: '', id: '', type: '', size: 0}
  objectId: any;
  attachmentId: any;
  fileList: any[] = [];
  type: any;
  constructor(injector: Injector,
              private taskService: TaskService,
              private documentService: DocumentService) {
    super(injector);
    this.objectId = this.route.snapshot.params['id'];
    this.attachmentId = this.route.snapshot.params['attachmentId'];
  }

  ngOnInit() {
    console.log(this.route.queryParamMap)
    this.route.queryParamMap.subscribe((params: ParamMap) => {
      this.type = params.get('type');
      this.getAttachments2Edit(this.objectId, params.get('type'));
    })
  }

  getAttachments2Edit(objectId: string, type: any) {
    console.log(type);
    this.documentService.getAttachmentsByObjectId(objectId, type).toPromise()
      .then((res: any) => {
        this.fileList = res.data;
        this.getValueMenuItems();
      })
      .catch((err: any) => {
        this.createErrorToast('Lỗi', err.message);
      });
  }

  getValueMenuItems() {
    for (let file in this.fileList) {
      const item: MenuFileItem = {
        name: this.fileList[file].name,
        urlPath: `/tasks/${this.objectId}/attachments/${this.fileList[file].id}`,
        filePath: this.fileList[file].filePath,
        id: this.fileList[file].id,
        type: this.fileList[file].type,
        size: this.fileList[file].size
      }
      if (this.fileList[file].id == this.attachmentId) this.getItemSelected(item)
      this.menuItems.push(item);
    }
  }

  downloadAttachment(attachment: MenuFileItem) {
    this.fileService.downloadFile(attachment.name, attachment.filePath)
  }

  getItemSelected(item: MenuFileItem) {
    this.itemSelected = item;
    if (this.getFileType(this.itemSelected.type) == "excel") {
      this._loadWorkbook(this.itemSelected.id);
    }
  }

  getFileUrl(id: string) {
    console.log(this.fileService.getFileUrl(id))
    return this.fileService.getFileUrl(id);
  }

  // view excel file
  tabClicked(e: MouseEvent, index: number) {
    e.preventDefault();
    this._drawSheet(index);
  }

  async _loadWorkbook(id: any) {
    let reader = new FileReader();
    //
    reader.onload = (e) => {
      let workbook = new wjcXlsx.Workbook();
      workbook.loadAsync(<string>reader.result, (result: wjcXlsx.Workbook) => {
        this.workbook = result;
        this._drawSheet(this.workbook.activeWorksheet || 0);
      });
    };
    //
    let fileUrl: string = this.getFileUrl(id);
    if (fileUrl) {
      let response = await fetch(fileUrl);
      let data = await response.blob();
      reader.readAsDataURL(data);
    }
  }

  private _drawSheet(sheetIndex: number) {
    let drawRoot = document.getElementById('tableHost');
    // @ts-ignore
    drawRoot.textContent = '';
    this.sheetIndex = sheetIndex;
    // @ts-ignore
    this._drawWorksheet(this.workbook, sheetIndex, drawRoot, 200, 100);
  }

  //
  private _drawWorksheet(workbook: wjcXlsx.IWorkbook, sheetIndex: number, rootElement: HTMLElement, maxRows: number, maxColumns: number) {
    //NOTES:
    //Empty cells' values are numeric NaN, format is "General"
    //
    //Excessive empty properties:
    //fill.color = undefined
    //
    // netFormat should return '' for ''. What is 'General'?
    // font.color should start with '#'?
    // Column/row styles are applied to each cell style, this is convenient, but Column/row style info should be kept,
    // for column/row level styling
    // formats conversion is incorrect - dates and virtually everything; netFormat - return array of formats?
    // ?row heights - see hello.xlsx
    if (!workbook || !workbook.sheets || sheetIndex < 0 || workbook.sheets.length == 0) {
      return;
    }
    //
    sheetIndex = Math.min(sheetIndex, workbook.sheets.length - 1);
    //
    if (maxRows == null) {
      maxRows = 200;
    }
    //
    if (maxColumns == null) {
      maxColumns = 100;
    }
    //
    // Namespace and XlsxConverter shortcuts.
    let sheet = workbook.sheets[sheetIndex],
      defaultRowHeight = 20,
      defaultColumnWidth = 60,
      tableEl = document.createElement('table');
    //
    tableEl.border = '1';
    tableEl.style.borderCollapse = 'collapse';
    //
    let maxRowCells = 0;
    for (let r = 0; sheet.rows && r < sheet.rows.length; r++) {
      if (sheet.rows[r] && sheet.rows[r].cells) {
        // @ts-ignore
        maxRowCells = Math.max(maxRowCells, sheet.rows[r].cells.length);
      }
    }
    //
    // add columns
    let columns = sheet.columns || [],
      invisColCnt = columns.filter(col => col.visible === false).length;
    //
    if (sheet.columns) {
      maxRowCells = Math.min(Math.max(maxRowCells, columns.length), maxColumns);
      //
      for (let c = 0; c < maxRowCells; c++) {
        let col = columns[c];
        //
        if (col && !col.visible) {
          continue;
        }
        //
        let colEl = document.createElement('col');
        tableEl.appendChild(colEl);
        let colWidth = defaultColumnWidth + 'px';
        if (col) {
          // @ts-ignore
          this._importStyle(colEl.style, col.style);
          if (col.autoWidth) {
            colWidth = '';
          } else if (col.width != null) {
            colWidth = col.width + 'px';
          }
        }
        colEl.style.width = colWidth;
      }
    }
    //
    // generate rows
    // @ts-ignore
    let rowCount = Math.min(maxRows, sheet.rows.length);
    for (let r = 0; sheet.rows && r < rowCount; r++) {
      let row = sheet.rows[r],
        cellsCnt = 0; // including colspan
      //
      if (row && !row.visible) {
        continue;
      }
      //
      let rowEl = document.createElement('tr');
      tableEl.appendChild(rowEl);
      //
      if (row) {
        // @ts-ignore
        this._importStyle(rowEl.style, row.style);
        if (row.height != null) {
          rowEl.style.height = row.height + 'px';
        }
        //
        for (let c = 0; row.cells && c < row.cells.length; c++) {
          let cell = row.cells[c],
            cellEl = document.createElement('td'),
            col = columns[c];
          //
          if (col && !col.visible) {
            continue;
          }
          //
          cellsCnt++;
          //
          rowEl.appendChild(cellEl);
          if (cell) {
            // @ts-ignore
            this._importStyle(cellEl.style, cell.style);
            let value = cell.value;
            //
            if (!(value == null || value !== value)) { // TBD: check for NaN should be eliminated
              if (wjcCore.isString(value) && value.charAt(0) == "'") {
                value = value.substr(1);
              }
              let netFormat = '';
              if (cell.style && cell.style.format) {
                netFormat = wjcXlsx.Workbook.fromXlsxFormat(cell.style.format)[0];
              }
              let fmtValue = netFormat ? wjcCore.Globalize.format(value, netFormat) : value;
              cellEl.innerHTML = wjcCore.escapeHtml(fmtValue);
            }
            //
            if (cell.colSpan && cell.colSpan > 1) {
              cellEl.colSpan = this._getVisColSpan(columns, c, cell.colSpan);
              cellsCnt += cellEl.colSpan - 1;
              c += cell.colSpan - 1;
            }
            //
            if (cell.note) {
              wjcCore.addClass(cellEl, 'cell-note');
              // @ts-ignore
              cellEl.title = cell.note.text;
            }
          }
        }
      }
      //
      // pad with empty cells
      let padCellsCount = maxRowCells - cellsCnt - invisColCnt;
      for (let i = 0; i < padCellsCount; i++) {
        rowEl.appendChild(document.createElement('td'));
      }
      //
      if (!rowEl.style.height) {
        rowEl.style.height = defaultRowHeight + 'px';
      }
    }
    //
    // do it at the end for performance
    rootElement.appendChild(tableEl);
  }

  private _getVisColSpan(columns: wjcXlsx.IWorkbookColumn[], startFrom: number, colSpan: number) {
    let res = colSpan;
    //
    for (let i = startFrom; i < columns.length && i < startFrom + colSpan; i++) {
      let col = columns[i];
      if (col && !col.visible) {
        res--;
      }
    }
    //
    return res;
  }

  private _importStyle(cssStyle: CSSStyleDeclaration, xlsxStyle: wjcXlsx.IWorkbookStyle) {
    if (!xlsxStyle) {
      return;
    }
    //
    if (xlsxStyle.fill) {
      if (xlsxStyle.fill.color) {
        cssStyle.backgroundColor = xlsxStyle.fill.color;
      }
    }
    //
    if (xlsxStyle.hAlign && xlsxStyle.hAlign != wjcXlsx.HAlign.Fill) {
      cssStyle.textAlign = wjcXlsx.HAlign[xlsxStyle.hAlign].toLowerCase();
    }
    //
    let font = xlsxStyle.font;
    if (font) {
      if (font.family) {
        cssStyle.fontFamily = font.family;
      }
      if (font.bold) {
        cssStyle.fontWeight = 'bold';
      }
      if (font.italic) {
        cssStyle.fontStyle = 'italic';
      }
      if (font.size != null) {
        cssStyle.fontSize = font.size + 'px';
      }
      if (font.underline) {
        cssStyle.textDecoration = 'underline';
      }
      if (font.color) {
        cssStyle.color = font.color;
      }
    }
  }
}
